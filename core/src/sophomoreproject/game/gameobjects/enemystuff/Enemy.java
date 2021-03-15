package sophomoreproject.game.gameobjects.enemystuff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateEnemy;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;
import java.util.Collection;

import static sophomoreproject.game.utilites.CharacterUtilities.accelerateTowardsTargetVelocity;

public class Enemy extends PhysicsObject implements Renderable {
    private static float IDLE_WAIT_DELAY = 3f;
    private static float WALK_DELAY = 2f;
    private static float IDLE_WAIT_VARIANCE = 1;
    private static float WALK_VARIANCE = 1;
    private static float MAX_AGE = 4 * 60; // 4 minutes

    private static Vector2 IDLE_VEL = new Vector2(); // just zero vector


    public enum EnemyState {
        IDLE_WAIT,
        IDLE_WALK,
        APPROACHING_TARGET,
        ATTACKING_TARGET,
        RETURNING_TO_SPAWN
    }
    private EnemyInfo info;

    private static TextureAtlas texAtl = null;
    private Sprite sprite;

    private EnemyState state = EnemyState.IDLE_WAIT;
    private Vector2 targetVelocity = new Vector2();
    private Vector2 playerMinusPos = new Vector2();
    private Player targetPlayer;

    private float idleWaitTimer = IDLE_WAIT_DELAY;
    private float walkTimer = WALK_DELAY;
    private float age = 0;


    // server constructor
    public Enemy(EnemyInfo info, Vector2 position, int networkID) {
        super(position, new Vector2(0, 0), new Vector2(0, 0), networkID);
        this.info = info;
        updateFrequency = ServerUpdateFrequency.CONSTANT;
    }

    // client constructor
    public Enemy(CreateEnemy packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.info = packet.info;

        loadTextures();
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateEnemy(this));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {

    }

    @Override
    public void run(float dt, GameServer server) {
        float radius;
        switch (state) {
            case IDLE_WAIT:
                idleWaitTimer -= dt;
                if (idleWaitTimer <= 0) {
                    idleWaitTimer += IDLE_WAIT_DELAY + LocalRandom.randomPosNeg() * IDLE_WAIT_VARIANCE;
                    tryFindPlayer(server); // try to find a player at this point
                    // if we find a player,
                    if (targetPlayer != null) {
                        // approach player first
                        state = EnemyState.APPROACHING_TARGET;
                    } else {
                        // else, go to idle walk state
                        state = EnemyState.IDLE_WALK;
                        targetVelocity.set(1, 0);
                        targetVelocity.rotateRad((float)Math.PI * 2 * LocalRandom.RAND.nextFloat());
                        targetVelocity.scl(info.maxIdleVelocity);
                    }
                }
                break;
            case IDLE_WALK:
                walkTimer -= dt;
                if (walkTimer <= 0) {
                    walkTimer += WALK_DELAY + LocalRandom.randomPosNeg() * WALK_VARIANCE;
                    tryFindPlayer(server); // try to find a player at this point
                    // if we find a player,
                    if (targetPlayer != null) {
                        // approach player first
                        state = EnemyState.APPROACHING_TARGET;
                    } else {
                        // else, go to idle wait state
                        state = EnemyState.IDLE_WAIT;
                        targetVelocity.set(0, 0);
                    }

                }
                break;
            case APPROACHING_TARGET:
                playerMinusPos.set(targetPlayer.position);
                playerMinusPos.sub(position);
                radius = playerMinusPos.len();
                if (radius > info.approachRadius) {
                    // go to idle wait or idle walk
                    if (LocalRandom.RAND.nextFloat() > 0.5) {
                        state = EnemyState.IDLE_WAIT;
                    } else {
                        state = EnemyState.IDLE_WALK;
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
                playerMinusPos.set(targetPlayer.position);
                playerMinusPos.sub(position);
                radius = playerMinusPos.len();
                if (radius > info.attackRadius) {
                    // go back to approaching target state
                    state = EnemyState.APPROACHING_TARGET;
                } else {
                    playerMinusPos.nor().scl(info.maxActiveVelocity);
                    targetVelocity.set(playerMinusPos);
                    // TODO: determine if enemy is touching player. if true, damage player and set cooldown timer
                }
                break;
            case RETURNING_TO_SPAWN:
                // unused for now
                break;
        }

        if (age > MAX_AGE) {
            server.removeObject(networkID);
        }

        accelerateTowardsTargetVelocity(targetVelocity, info.maxAcceleration, this, dt);
        age += dt;
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
        sprite.setOriginBasedPosition(position.x, position.y);
        sprite.draw(sb);
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
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }
        if (sprite == null) {
            sprite = new Sprite(texAtl.findRegion("enemy"));
            sprite.setOriginCenter();
            sprite.setScale(info.size);
        }
    }

    public EnemyInfo getInfo() {
        return info;
    }
}
