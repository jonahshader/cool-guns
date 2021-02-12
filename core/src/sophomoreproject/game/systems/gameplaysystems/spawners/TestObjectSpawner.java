package sophomoreproject.game.systems.gameplaysystems.spawners;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.gameplaysystems.GameSystem;

public class TestObjectSpawner implements GameSystem {
    private final GameServer gameServer;
    private static final float SPAWN_TIME = 4.0f;
    private float spawnTime = SPAWN_TIME;

    public TestObjectSpawner(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void run(float dt) {
        if (spawnTime < 0) {
            spawnTime += SPAWN_TIME;
            // spawn test object
            gameServer.spawnAndSendGameObject(new TestObject(new Vector2()));
            System.out.println("Test object spawned on server!");
        }

        spawnTime -= dt;
    }
}
