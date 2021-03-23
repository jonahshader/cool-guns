package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.systems.GameServer;

import java.util.Collection;

import static sophomoreproject.game.screens.GameScreen.GAME_HEIGHT;

public class Spawner {
    private static final float SPAWN_RADIUS = GAME_HEIGHT * .45f;
    private static final float IS_SPAWNING_UPDATE_DELAY = 1;
    private Vector2 position;
    private int spawnsBuffered;
    private int maxSpawnBuffer;
    private float spawnDelay;
    private float spawnRegenDelay;
    private float spawnTimer;
    private float spawnRegenTimer;
    private float isSpawningUpdateTimer = IS_SPAWNING_UPDATE_DELAY;
    private SpawnAction spawnAction;
    private Player nearestPlayer;
    private Vector2 playerMinusPos = new Vector2();


    public Spawner(Vector2 position, int spawnsBuffered, int maxSpawnBuffer, float spawnDelay, float spawnRegenDelay, SpawnAction spawnAction) {
        this.position = position;
        this.spawnsBuffered = spawnsBuffered;
        this.maxSpawnBuffer = maxSpawnBuffer;
        this.spawnDelay = spawnDelay;
        this.spawnRegenDelay = spawnRegenDelay;
        this.spawnAction = spawnAction;
    }

    public void run(float dt, GameServer gameServer) {
        if (isSpawningUpdateTimer > 0) {
            isSpawningUpdateTimer -= dt;
        } else {
            isSpawningUpdateTimer += IS_SPAWNING_UPDATE_DELAY;
            tryFindPlayer(gameServer);
        }

        if (spawnTimer <= 0 && spawnsBuffered > 0 && isSpawning()) {
            spawn(gameServer);
        }

        if (spawnTimer > 0) {
            spawnTimer -= dt;
        }
        if (spawnRegenTimer > 0) {
            spawnRegenTimer -= dt;
        } else if (spawnsBuffered < maxSpawnBuffer) {
            ++spawnsBuffered;
            spawnRegenTimer += spawnRegenDelay;
        }
    }

    private void spawn(GameServer server) {
        spawnAction.spawn(server, position.x, position.y);
        spawnsBuffered--;
        spawnTimer += spawnDelay;
    }

    private void tryFindPlayer(GameServer server) {
        nearestPlayer = null; // reset current target to null
        Collection<Player> nearbyPlayers = server.getServerMap().getNearbyPlayers(position);
        if (nearbyPlayers != null) {
            playerMinusPos.set(0, 0);
            float nearestRadius = SPAWN_RADIUS;
            // sort through nearby players and find the closest one that is within approachRadius
            for (Player p : nearbyPlayers) {
                playerMinusPos.set(p.position);
                playerMinusPos.sub(position);
                float radius = playerMinusPos.len();
                if (radius < nearestRadius) {
                    nearestRadius = radius;
                    nearestPlayer = p;
                }
            }
        }
    }

    private boolean isSpawning() {
        return nearestPlayer != null;
    }
}
