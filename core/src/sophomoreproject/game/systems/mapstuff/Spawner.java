package sophomoreproject.game.systems.mapstuff;

import sophomoreproject.game.systems.GameServer;

public class Spawner {
    private int x, y;
    private int spawnsBuffered;
    private int maxSpawnBuffer;
    private float spawnDelay;
    private float spawnRegenDelay;
    private float spawnTimer;
    private float spawnRegenTimer;
    private SpawnAction spawnAction;

    public Spawner(int x, int y, int spawnsBuffered, int maxSpawnBuffer, float spawnDelay, float spawnRegenDelay, SpawnAction spawnAction) {
        this.x = x;
        this.y = y;
        this.spawnsBuffered = spawnsBuffered;
        this.maxSpawnBuffer = maxSpawnBuffer;
        this.spawnDelay = spawnDelay;
        this.spawnRegenDelay = spawnRegenDelay;
        this.spawnAction = spawnAction;
    }

    public void run(float dt, GameServer gameServer) {
        if (spawnTimer <= 0 && spawnsBuffered > 0) {
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
        spawnAction.spawn(server, x, y);
        spawnsBuffered--;
        spawnTimer += spawnDelay;
    }

    private boolean isRunning() {
        return false;
    }
}
