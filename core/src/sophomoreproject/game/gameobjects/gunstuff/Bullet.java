package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.CollisionReceiver;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.utilites.MathUtilities;

import java.util.ArrayList;
import java.util.Collection;

import static sophomoreproject.game.screens.GameScreen.GAME_HEIGHT;
import static sophomoreproject.game.screens.GameScreen.GAME_WIDTH;

public class Bullet extends PhysicsObject implements Renderable {
    public enum BulletType {
        STANDARD,
        FIRE,
        VOLTAGE,
        ACID
    }

    private int creatorNetId;
    private static TextureAtlas texAtl = null;
    private Sprite sprite = null;
    private float bulletSize;
    private int damage;
    private int shieldDamage;
    private int armorDamage;
    private float critScalar;
    private float enemyKnockback;
    private Vector2 bulletSpawn;
    private static final float MAX_RANGE = (float)Math.sqrt(GAME_WIDTH * GAME_WIDTH + GAME_HEIGHT * GAME_HEIGHT) * 1.25f; // world units
    private static final float MAX_AGE = 10; // seconds

    private float spin;
    private float age = 0;

    //Server side constructor
//    public Bullet(Vector2 position, Vector2 velocity, Vector2 acceleration,
//                  int creatorNetId, int networkID, float bulletSize,
//                  float damage, int shieldDamage, int armorDamage,
//                  float critScalar, float enemyKnockback) {
//        super(position, velocity, acceleration, networkID);
//        this.creatorNetId = creatorNetId;
//        this.bulletSize = bulletSize;
//        this.damage = damage;
//        this.shieldDamage = shieldDamage;
//        this.armorDamage = armorDamage;
//        this.critScalar = critScalar;
//        this.enemyKnockback = enemyKnockback;
//        this.bulletSpawn = new Vector2(position);
//
//        updateFrequency = ServerUpdateFrequency.ONCE;
//    }

    //Client/Server side constructor
    public Bullet(CreateBullet packet, boolean client) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.creatorNetId = packet.creatorNetId;
        this.bulletSize = packet.bulletSize;
        this.damage = Math.round(packet.damage);
        this.shieldDamage = packet.shieldDamage;
        this.armorDamage = packet.armorDamage;
        this.critScalar = packet.critScalar;
        this.enemyKnockback = packet.enemyKnockback;
        this.bulletSpawn = new Vector2(packet.u.x, packet.u.y);

        updateFrequency = ServerUpdateFrequency.ONCE;

        spin = (float) (LocalRandom.RAND.nextGaussian() * 180 / Math.PI);

        if (client)
            loadTextures();
    }


    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateBullet(this));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
    }

    @Override
    public void run(float dt, GameServer server) {
        Collection<CollisionReceiver> receivers = server.getServerMap().getNearbyCollisionReceivers(position);
        if (receivers != null) {
            for (CollisionReceiver collisionReceiver : receivers) {
//            if (collisionReceiver == null) {
//                System.out.println("yikes this thing is null");
//            }
//            assert collisionReceiver != null;
                if (collisionReceiver.checkCollidingGroup(CollisionReceiver.CollisionGroup.BULLET)) {
                    if (collisionReceiver.getNetworkID() != creatorNetId) {
                        float radius = Math.max(dt * velocity.len() * .5f, bulletSize);
                        if (MathUtilities.circleCollisionDetection(position.x, position.y, radius, collisionReceiver.getPosition().x, collisionReceiver.getPosition().y, collisionReceiver.getRadius())) {
                            Vector2 knockbackVec = new Vector2(velocity).nor().scl(enemyKnockback);
                            // correct collision.
                            // if collision kills target,
                            // TODO: should this be parameterized? penetration ability?
                            int damageDealt = collisionReceiver.receiveAttack(new AttackInfo(damage, shieldDamage, armorDamage, knockbackVec.x, knockbackVec.y), creatorNetId);
                            System.out.println("Bullet inflicted " + damageDealt + " damage.");
                            if (damageDealt == damage) {
                                // remove bullet
                                server.removeObject(networkID);
                                break;
                            } else {
                                damage -= damageDealt;
                                if (damage <= 0) {
                                    server.removeObject(networkID);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        float dist = position.dst(bulletSpawn);
        if (dist >= MAX_RANGE || age >= MAX_AGE) {
            server.removeObject(networkID);
        }
        age += dt;
    }

    public int getCreatorNetId() {
        return creatorNetId;
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }

        if (sprite == null) {
            sprite = new Sprite(texAtl.findRegion("bullet"));
            sprite.setOriginCenter();
            sprite.setScale(bulletSize);
        }
    }

    public float getBulletSize() {
        return bulletSize;
    }

    public float getEnemyKnockback() {
        return enemyKnockback;
    }

    public float getDamage() {
        return damage;
    }

    public int getShieldDamage() {
        return shieldDamage;
    }

    public int getArmorDamage() {
        return armorDamage;
    }

    public float getCritScalar() {
        return critScalar;
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        sprite.setPosition(position.x, position.y);
        sprite.rotate(spin * dt);
        sprite.draw(sb);
    }
}
