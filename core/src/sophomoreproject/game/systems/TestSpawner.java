package sophomoreproject.game.systems;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.packets.CreateTestObject;

/**
 * this is ran by the server
 */
public class TestSpawner {
    private GameWorld world;
    private Server server;
    private static final float SPAWN_TIME = 3.0f;
    private float currentSpawnTimeout = 0.0f;

    public TestSpawner(GameWorld world) {
        this.world = world;
    }

    public void run(float dt) {
        if (currentSpawnTimeout > SPAWN_TIME) {
            currentSpawnTimeout -= SPAWN_TIME;

            // spawn
            TestObject testObject = new TestObject(new Vector2());
            // add to world
            world.addObject(testObject);
            // transmit creation packet
            server.sendToAllTCP(new CreateTestObject(testObject));
        }

        currentSpawnTimeout += dt;
    }
}
