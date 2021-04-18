package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.singletons.SoundSystem;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.LocalRandom.*;

//This is owned by a player or it is in an inventory (like a shop).

public class Gun extends Item {
    public enum FiringMode {
        AUTO,
        SEMI_AUTO,
        BURST,
        CHARGE
    }

    public enum GunType {
        PISTOL,
        RIFLE,
        SMG,
        LMG,
        SHOTGUN
    }

    private final GunInfo info;

    private static TextureAtlas texAtl = null;
    private final ArrayList<Object> bulletPackets = new ArrayList<>();
    private Sprite gunSprite = null;
    private Sprite gunIcon = null;
    private Sound fireSound = null;
    private float firingTimer;
    private float burstDelayTimer = 0;
    private float reloadTimer = 0;
    private boolean bursting = false;
    private int burstShotsFired = 0;
    private int currentClip;

    //Client Constructor
    public Gun(CreateInventoryGun packet) {
        this.info = packet.info;
        this.ownerNetId = packet.ownerNetId;
        this.networkID = packet.netId;

        currentClip = info.clipSize;

        loadTextures();

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    //Server Constructor
    public Gun(GunInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.networkID = netId;

        currentClip = info.clipSize;

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    @Override
    public void updateItem(float dt, boolean usedOnce, boolean using, Vector2 angle, Player player) {
        this.angle.set(angle);
        this.position.set(player.position);
        Vector2 offset = new Vector2(angle);
        offset.scl(info.gunHoldRadius);
        position.add(offset);

        if (bursting) {
            if (burstShotsFired < info.shotsPerBurst) {
                if (firingTimer <= 0 && reloadTimer <= 0) {
                    shoot(angle, player);
                }
            } else {
                if (burstDelayTimer > 0) {
                    burstDelayTimer = burstDelayTimer - dt;
                } else {
                    bursting = false;
                    burstShotsFired = 0;
                }
            }
        } else {
            if (firingTimer <= 0 && reloadTimer <= 0) {
                switch (info.firingMode) {
                    case AUTO:
                        if (using) shoot(angle, player);
                        break;
                    case SEMI_AUTO:
                        if (usedOnce) shoot(angle, player);
                        break;
                    case BURST:
                        if (usedOnce) {
                            bursting = true;
                            burstDelayTimer = info.burstDelay;
                            shoot(angle, player);
//                            --currentClip;
//                            ++burstShotsFired;
                        }
                        break;
                    case CHARGE:
                        break;
                    default:
                        break;
                }
            }
        }

        if (firingTimer > 0) {
            firingTimer = firingTimer - dt;
        }

        if (reloadTimer > 0) {
            reloadTimer = reloadTimer - dt;
        }
    }


    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        // make CreateInventoryGun packet and insert into createPacketBuffer
        createPacketBuffer.add(new CreateInventoryGun(this));
    }

    @Override
    public void run(float dt, GameServer server) {
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        if (isEquipped()) {
            gunSprite.setFlip(false,angle.x < 0);
            gunSprite.setRotation(angle.angleDeg());
            gunSprite.setOriginBasedPosition(position.x, position.y);
            gunSprite.draw(sb);
        }
    }

    private void shoot(Vector2 angle, Player player) {
        boolean shot = false;
        firingTimer = firingTimer + info.fireDelay;
        Vector2 baseVelocity = new Vector2(angle);
        baseVelocity.scl(info.bulletSpeed);

        Vector2 accumulatedKnockback = new Vector2();
        Vector2 inheritedVelocity = new Vector2(player.velocity).scl(.5f);

        for (int b = 0; b < info.bulletsPerShot; b++) {
            if (currentClip > 0) {
                Vector2 uniqueVel = new Vector2(baseVelocity);
                uniqueVel.rotateRad((float) RAND.nextGaussian()*info.spread);
                uniqueVel.scl(expGaussian(2f,info.bulletSpeedVariation));

                Vector2 uniqueKnockback = new Vector2(uniqueVel).nor().scl(-info.playerKnockback);
                accumulatedKnockback.add(uniqueKnockback);


                uniqueVel.add(inheritedVelocity); // inherit velocity from player

                float uniqueDam = info.bulletDamage + genTriangleDist()*info.bulletDamageVariance;

                bulletPackets.add(new CreateBullet(new UpdatePhysicsObject(-1, position.x, position.y, uniqueVel.x, uniqueVel.y, 0f, 0f), player.getNetworkID(),
                        info.bulletSize, uniqueDam, info.shieldDamage,info.armorDamage, info.critScalar, info.enemyKnockback));
                shot = true;
                currentClip--;
                ++burstShotsFired;
            } else {
                reloadTimer += info.reloadDelay;
                currentClip = info.clipSize;
                bursting = false;
                burstShotsFired = 0;
                break; // break out of for loop
            }
        }
        if (currentClip == 0) {
            reloadTimer += info.reloadDelay;
            currentClip = info.clipSize;
            bursting = false;
            burstShotsFired = 0;
        }
        player.velocity.add(accumulatedKnockback);
        ClientNetwork.getInstance().sendAllPackets(bulletPackets);
        bulletPackets.clear();

        if (shot) {
            // play fire sound
            float pitch = LocalRandom.RAND.nextFloat() * 0.12f + .4f + (float) (1.4 / (1 + Math.pow(Math.E, (info.bulletSize - 3.5))));
            System.out.println(pitch);
            SoundSystem.getInstance().playSoundInWorld(fireSound, position, .5f, pitch);
        }
    }

    @Override
    public void manualReload() {
        if (reloadTimer <= 0) {
            reloadTimer += info.reloadDelay;
            currentClip = info.clipSize;
            bursting = false;
            burstShotsFired = 0;
        }
    }

    @Override
    public GroundItem toGroundItem(GameServer server) {
        return new GroundItem(new Vector2(position), server.getGameWorld().getNewNetID(), info.getTextureName(),
                new Color(info.r, info.g, info.b, 1), 1f,
                new CreateInventoryGun(info, -1, server.getGameWorld().getNewNetID()), false);
    }


    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }
        if (gunSprite == null) {
            gunIcon = new Sprite(texAtl.findRegion(info.getTextureName()));
            gunIcon.setColor(info.r, info.g, info.b, 1f);
            gunSprite = new Sprite(gunIcon);
            gunSprite.setOriginCenter();
        }
        if (fireSound == null) {
            fireSound = info.getSound();
        }
    }

    public GunInfo getInfo() {
        return info;
    }

    @Override
    public void renderIcon(SpriteBatch sb, float size, float x, float y) {
        float widthToHeight = gunIcon.getRegionHeight() / (float)gunIcon.getRegionWidth();
        gunIcon.setSize(size, size * widthToHeight);
        gunIcon.setPosition(x, y);
        gunIcon.setColor(info.r, info.g, info.b, 1);
        gunIcon.draw(sb);
    }

    public int getOwnerNetId() {
        return ownerNetId;
    }

    public int getCurrentClip() {
        return currentClip;
    }
}
