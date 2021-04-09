package sophomoreproject.game.gameobjects.enemystuff;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.gunstuff.AttackInfo;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.interfaces.CollisionReceiver;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.interfaces.Shadow;
import sophomoreproject.game.packets.CreateEnemy;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.UpdateEnemy;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.singletons.SoundSystem;
import sophomoreproject.game.singletons.StatsBarRenderer;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.dropper.StandardDropper;
import sophomoreproject.game.systems.marker.Marker;
import sophomoreproject.game.utilites.MathUtilities;

import java.util.ArrayList;
import java.util.Collection;

import static sophomoreproject.game.singletons.CustomAssetManager.*;
import static sophomoreproject.game.utilites.CharacterUtilities.accelerateTowardsTargetVelocity;

public class Enemy extends PhysicsObject implements Renderable, CollisionReceiver, Shadow {
    private static final float IDLE_WAIT_DELAY = 3f;
    private static final float IDLE_WAIT_VARIANCE = 2f;
    private static final float WALK_DELAY = 2f;
    private static final float WALK_VARIANCE = 2f;
    private static final float TARGET_UPDATE_DELAY = .35f;
    public static final float MAX_IDLE_TIME = 80; // 80 seconds

    public enum EnemyState {
        IDLE_WAIT,
        IDLE_WALK,
        APPROACHING_TARGET,
        ATTACKING_TARGET,
        RETURNING_TO_SPAWN
    }

    private EnemyInfo info;
    private static StandardDropper dropper = new StandardDropper();

    private static TextureAtlas texAtl = null;
    private Sprite sprite;
    private Sprite shadow;

    private static Sound itemDropSound, deathSound, bulletImpactSound;
    private boolean queueIdleSound = false;
    private boolean queueItemDropSound = false;

    private EnemyState state = EnemyState.IDLE_WALK;
    private Vector2 targetVelocity = new Vector2();
    private Vector2 playerMinusPos = new Vector2();
    private Vector2 barPos = new Vector2();
    private Marker marker;
    private Player targetPlayer;

    private float idleWaitTimer = IDLE_WAIT_DELAY;
    private float walkTimer = WALK_DELAY;
    private float targetUpdateTimer = TARGET_UPDATE_DELAY;
    private float attackTimer = 0;
    private float idleTime = 0;
    private float velIntegral = 0;
    private float heightOffset = 0;

    private int health;

    private boolean queueDead = false;


    private ArrayList<StatsBarRenderer.StatsBarInfo> bars;
    private StatsBarRenderer.StatsBarInfo healthBar;

    // server constructor
    public Enemy(EnemyInfo info, Vector2 position, int networkID) {
        super(position, new Vector2(0, 0), new Vector2(0, 0), networkID);
        this.info = info;
        health = info.health;
        updateFrequency = ServerUpdateFrequency.CONSTANT;

        // init walk velocity randomly
        targetVelocity.set(1, 0);
        targetVelocity.rotateRad((float)Math.PI * 2 * LocalRandom.RAND.nextFloat());
        targetVelocity.scl(info.maxIdleVelocity);
    }

    // client constructor
    public Enemy(CreateEnemy packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.info = packet.info;
        health = info.health;
        loadTextures();
        updateFrequency = ServerUpdateFrequency.CONSTANT;

        bars = new ArrayList<>();
        healthBar = new StatsBarRenderer.StatsBarInfo(health,info.health, StatsBarRenderer.HEALTH_BAR_COLOR);
        bars.add(healthBar);
//        bars.add(new StatsBarRenderer.StatsBarInfo(10,20, StatsBarRenderer.SHIELD_BAR_COLOR));
//        bars.add(new StatsBarRenderer.StatsBarInfo(30,50, StatsBarRenderer.STAMINA_BAR_COLOR));
//        bars.add(new StatsBarRenderer.StatsBarInfo(6,15, StatsBarRenderer.ARMOR_BAR_COLOR));


    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateEnemy(this));
    }

    @Override
    public void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer) {
        updatePacketBuffer.add(new UpdateEnemy(networkID, health, queueIdleSound, queueItemDropSound));
        queueIdleSound = false;
        queueItemDropSound = false;
        super.addUpdatePacketToBuffer(updatePacketBuffer);
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        UpdateEnemy packet = (UpdateEnemy) updatePacket;
        // prioritize drop sound
        if (packet.playDropSound) {
            // cancel some sounds to make this sound pop out
            bulletImpactSound.stop();
            // play sound effect
            SoundSystem.getInstance().playSoundInWorld(itemDropSound, position, .85f, 1f);
        } else {
            if (health != packet.health) {
                SoundSystem.getInstance().playSoundInWorld(bulletImpactSound, position, .8f, 1f);
            }

            if (packet.playIdleSound) {
                // play sound effect
                SoundSystem.getInstance().playSoundGroup(SoundSystem.SoundGroup.ENEMY_BLOB, position, .7f, 1f);
            }
        }


        health = packet.health;
        if (healthBar != null)
            healthBar.value = health;
    }

    @Override
    public void run(float dt, GameServer server) {
        float radius;
        switch (state) {
            case IDLE_WAIT:
                idleWaitTimer -= dt;
                idleTime += dt;
                if (idleWaitTimer <= 0) {
                    idleWaitTimer += IDLE_WAIT_DELAY + LocalRandom.randomPosNeg() * IDLE_WAIT_VARIANCE;
                    tryFindPlayer(server); // try to find a player at this point
                    // if we find a player,
                    if (targetPlayer != null) {
                        // approach player first
                        state = EnemyState.APPROACHING_TARGET;
                        queueIdleSound = true;
                    } else {
                        // else, go to idle walk state
                        state = EnemyState.IDLE_WALK;
                        targetVelocity.set(1, 0);
                        targetVelocity.rotateRad((float)Math.PI * 2 * LocalRandom.RAND.nextFloat());
                        targetVelocity.scl(info.maxIdleVelocity);
                        queueIdleSound = LocalRandom.RAND.nextFloat() > .5f;
                    }
                }
                break;
            case IDLE_WALK:
                walkTimer -= dt;
                idleTime += dt;
                if (walkTimer <= 0) {
                    walkTimer += WALK_DELAY + LocalRandom.randomPosNeg() * WALK_VARIANCE;
                    tryFindPlayer(server); // try to find a player at this point
                    // if we find a player,
                    if (targetPlayer != null) {
                        // approach player first
                        state = EnemyState.APPROACHING_TARGET;
                        queueIdleSound = true;
                    } else {
                        // else, go to idle wait state
                        state = EnemyState.IDLE_WAIT;
                        targetVelocity.set(0, 0);
                        queueIdleSound = LocalRandom.RAND.nextFloat() > .9f;
                    }
                }
                break;
            case APPROACHING_TARGET:
                idleTime = 0;
                playerMinusPos.set(targetPlayer.position);
                playerMinusPos.sub(position);
                radius = playerMinusPos.len();
                if (radius > info.approachRadius) {
                    // go to idle wait or idle walk
                    if (LocalRandom.RAND.nextFloat() > 0.5) {
                        state = EnemyState.IDLE_WAIT;
                        targetVelocity.set(0, 0);
                        queueIdleSound = LocalRandom.RAND.nextFloat() > 0.9;
                    } else {
                        // else, go to idle walk state
                        state = EnemyState.IDLE_WALK;
                        targetVelocity.set(1, 0);
                        targetVelocity.rotateRad((float)Math.PI * 2 * LocalRandom.RAND.nextFloat());
                        targetVelocity.scl(info.maxIdleVelocity);
                    }
                } else if (radius < info.attackRadius) {
                    // go to attacking target state
                    state = EnemyState.ATTACKING_TARGET;
                } else {
                    // stay in this state. approach target at approach angle
                    playerMinusPos.nor().rotateRad(info.approachAngle).scl(info.maxActiveVelocity);
                    targetVelocity.set(playerMinusPos);
                }
                break;
            case ATTACKING_TARGET:
                idleTime = 0;
                targetUpdateTimer -= dt;
                if (targetUpdateTimer <= 0) {
                    targetUpdateTimer += TARGET_UPDATE_DELAY;
                    tryFindPlayer(server);
                }
                if (targetPlayer != null) {
                    playerMinusPos.set(targetPlayer.position);
                    playerMinusPos.sub(position);
                    radius = playerMinusPos.len();
                    if (radius > info.attackRadius) {
                        // go back to approaching target state
                        state = EnemyState.APPROACHING_TARGET;
                    } else {
                        playerMinusPos.nor().scl(info.maxActiveVelocity);
                        targetVelocity.set(playerMinusPos);
                        playerMinusPos.nor().scl(info.knockback);
                        if (attackTimer <= 0 && MathUtilities.circleCollisionDetection(position, getRadius(), targetPlayer.position, targetPlayer.getRadius())) {
                            server.processAndSendAttackPlayer(new AttackInfo(Math.round(info.attackDamage), 0, 0, playerMinusPos.x, playerMinusPos.y), targetPlayer.getNetworkID(), networkID);
                            attackTimer += info.attackDelay;
                        }
                    }
                } else {
                    state = EnemyState.IDLE_WAIT;
                    targetVelocity.set(0, 0);
                }

                break;
            case RETURNING_TO_SPAWN:
                idleTime += dt;
                // unused for now
                break;
        }

        if (attackTimer > 0) {
            attackTimer -= dt;
        }

        if (idleTime > MAX_IDLE_TIME) {
            server.removeObject(networkID);
        } else if (queueDead) {
            // drop items
            dropItem(server);
            server.removeObject(networkID);
        }

        accelerateTowardsTargetVelocity(targetVelocity, info.maxAcceleration, this, dt);
    }

    private void tryFindPlayer(GameServer server) {
        targetPlayer = null; // reset current target to null
        Collection<Player> nearbyPlayers = server.getServerMap().getNearbyPlayers(position);
        if (nearbyPlayers != null) {
            playerMinusPos.set(0, 0);
            float nearestRadius = info.approachRadius;
            // sort through nearby players and find the closest one that is within approachRadius
            for (Player p : nearbyPlayers) {
                playerMinusPos.set(p.position);
                playerMinusPos.sub(position);
                float radius = playerMinusPos.len();
                if (radius < nearestRadius) {
                    nearestRadius = radius;
                    targetPlayer = p;
                }
            }
        }
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        velIntegral += dt * velocity.len();
        heightOffset = 5 * (float)Math.abs(Math.sin(velIntegral * 10 / info.maxActiveVelocity));
        sprite.setOriginBasedPosition(position.x, position.y + heightOffset);
        sprite.draw(sb);
        barPos.set(position);
        barPos.y += ((info.size / 2)* sprite.getHeight()) + heightOffset;
        StatsBarRenderer.getInstance().drawStatsBarsInWorld(sb,barPos,bars);
    }

    @Override
    public void drawShadow(SpriteBatch sb) {
        shadow.setOriginBasedPosition(position.x, position.y - 6 * info.size);
        shadow.draw(sb);
    }

    @Override
    public void updateFromPacket(UpdatePhysicsObject packet) {
        // suppress acceleration for latency hiding reasons
        packet.xAccel = 0;
        packet.yAccel = 0;
        super.updateFromPacket(packet);
    }

    private void loadTextures() {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);

            itemDropSound = CustomAssetManager.getInstance().manager.get(ITEM_DROP);
            bulletImpactSound = CustomAssetManager.getInstance().manager.get(BULLET_IMPACT);
        }
        if (sprite == null) {
            TextureRegion enemyTexture = texAtl.findRegion("enemy");
            sprite = new Sprite(enemyTexture);
            sprite.setOriginCenter();
            sprite.setScale(info.size);

            shadow = new Sprite(texAtl.findRegion("shadow"));
            shadow.setOriginCenter();
            shadow.setScale(info.size * 2, info.size);
            shadow.setColor(1, 1, 1, .8f);

            marker = new Marker(enemyTexture, position, 1f) {
                @Override
                public boolean isInactive() {
                    return health <= 0;
                }
            };
        }
    }

    public EnemyInfo getInfo() {
        return info;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getRadius() {
        return info.size * 8; // approximation since sprite isn't loaded on server for sprite.getWidth() to work
    }

    @Override
    public CollisionGroup getCollisionGroup() {
        return CollisionGroup.ENEMY;
    }

    @Override
    public boolean checkCollidingGroup(CollisionGroup otherCollisionGroup) {
        return otherCollisionGroup == CollisionGroup.BULLET || otherCollisionGroup == CollisionGroup.PLAYER;
    }

    @Override
    public int receiveAttack(AttackInfo attack, int attackerNetID) {
        // if not already dead,
        if (!queueDead) {
            int healthLeftBeforeDamage = health;
            health -= Math.round(attack.damage);
            velocity.add(attack.xKnockback / info.size, attack.yKnockback / info.size);
            if (health <= 0) {
                health = 0;
                queueDead = true;

                if (healthBar != null) healthBar.value = health;
                return healthLeftBeforeDamage;
            } else {
                if (healthBar != null) healthBar.value = health;
                return Math.round(attack.damage);
            }
        }
        return 0;
    }

    private void dropItem(GameServer server) {
        if (dropper.tryDropItem(server, position, info.difficulty)) {
            // queue item drop sound
            queueItemDropSound = true;
        }
    }
}
