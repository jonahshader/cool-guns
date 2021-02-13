package sophomoreproject.game.systems.gameplaysystems.spawners;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.GameWorld;
import sophomoreproject.game.systems.gameplaysystems.GameSystem;

public class TestObjectSpawner implements GameSystem {
    private final GameServer gameServer;
    private final GameWorld world;
    private static final float SPAWN_TIME = 0.5f;
    private float spawnTime = SPAWN_TIME;

    public TestObjectSpawner(GameServer gameServer, GameWorld world) {
        this.gameServer = gameServer;
        this.world = world;
    }

    public void run(float dt) {
        if (spawnTime < 0) {
            spawnTime += SPAWN_TIME;
            // spawn test object
            for (int i = 0; i < 32; i++) {
                gameServer.spawnAndSendGameObject(new TestObject(new Vector2(), world.getNewNetID(), false));
            }
        }

        spawnTime -= dt;
    }
}
