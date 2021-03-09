package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.LocalRandom.*;

//This is owned by a player or it is in an inventory (like a shop).

public class Gun extends Item implements Renderable {
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

    private GunInfo info;

    private static TextureAtlas texAtl = null;
    private Sprite gunSprite = null;
    private float firingTimer;
    private final ArrayList<Object> bulletPackets = new ArrayList<>();
    private Vector2 angle = new Vector2();
    private Vector2 position = new Vector2();
    private int ownerNetId;
    private boolean bursting = false;
    private float burstDelayTimer = 0;
    private int burstShotsFired = 0;

    //Client Constructor
    public Gun(CreateInventoryGun packet) {
        this.info = packet.info;
        this.ownerNetId = packet.ownerNetId;
        this.networkID = packet.netId;
        loadTextures();
    }

    //Server Constructor
    public Gun(GunInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.networkID = netId;
    }

    @Override
    public void updateItem(float dt, boolean usedOnce, boolean using, Vector2 angle, Player player) {
        this.angle = angle;
        this.position = new Vector2(player.position);
        Vector2 offset = new Vector2(angle);
        offset.scl(info.gunHoldRadius);
        position.add(offset);

        if (bursting) {
            if (burstShotsFired < info.shotsPerBurst) {
                if (firingTimer <= 0) {
                    shoot(angle, player);
                    ++burstShotsFired;
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
            if (firingTimer <= 0) {
                switch (info.firingMode) {
                    case AUTO:
                        if (using) shoot(angle, player);
                        break;
                    case SEMI_AUTO:
                        if (usedOnce) shoot(angle, player);
                        break;
                    case BURST:
                        bursting = true;
                        burstDelayTimer = info.burstDelay;
                        shoot(angle, player);
                        ++burstShotsFired;
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
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        if (isEquipped()) {
            gunSprite.setFlip(false,angle.x < 0);
            gunSprite.setRotation(angle.angleDeg());
            gunSprite.setPosition(position.x, position.y);
            gunSprite.draw(sb);
        }
    }

    private void shoot(Vector2 angle, Player player) {
        firingTimer = firingTimer + info.fireDelay;
        Vector2 baseVelocity = new Vector2(angle);
        Vector2 knockbackVelocity = new Vector2(angle);
        knockbackVelocity.scl(-info.playerKnockback);
        baseVelocity.scl(info.bulletSpeed);

        for (int b = 0; b < info.bulletsPerShot; b++) {
            player.velocity.add(knockbackVelocity);
            Vector2 uniqueVel = new Vector2(baseVelocity);
            uniqueVel.rotateRad((float) RAND.nextGaussian()*info.spread);
            uniqueVel.scl(expGaussian(2f,info.bulletSpeedVariation));
            float uniqueDam = info.bulletDamage + genTriangleDist()*info.bulletDamageVariance;

            bulletPackets.add(new CreateBullet(new UpdatePhysicsObject(-1, player.position.x, player.position.y, uniqueVel.x, uniqueVel.y, 0f, 0f), player.getNetworkID(),
                    info.bulletSize, uniqueDam, info.shieldDamage,info.armorDamage, info.critScalar, info.enemyKnockback));
        }
        ClientNetwork.getInstance().sendAllPackets(bulletPackets);
        bulletPackets.clear();
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
            gunSprite = new Sprite(texAtl.findRegion("default_gun"));
        }
    }

    public GunInfo getInfo() {
        return info;
    }

    @Override
    public void renderIcon(SpriteBatch sb, float size, float x, float y) {

    }

    public int getOwnerNetId() {
        return ownerNetId;
    }
}
