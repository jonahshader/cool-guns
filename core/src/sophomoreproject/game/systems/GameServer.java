package sophomoreproject.game.systems;

import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.systems.gameplaysystems.GameSystem;
import sophomoreproject.game.systems.gameplaysystems.spawners.TestObjectSpawner;

import java.util.ArrayList;

public class GameServer {
    private GameWorld world;
    private ServerNetwork serverNetwork;

    private ArrayList<Object> createPackets = new ArrayList<>();
    private ArrayList<GameSystem> gameSystems = new ArrayList<>();

    public GameServer(ServerNetwork serverNetwork) {
        world = new GameWorld();
        this.serverNetwork = serverNetwork;

        // add game systems
        gameSystems.add(new TestObjectSpawner(this));
    }

    public void run(float dt) {
        // run game systems
        for(GameSystem g : gameSystems) g.run(dt);

        world.serverOnly(dt, serverNetwork);
        world.update(dt);
    }

    /**
     * adds a GameObject to the server's world and sends a creation packet to all the clients
     * @param gameObject
     */
    public void spawnAndSendGameObject(GameObject gameObject) {
        world.queueAddObject(gameObject);
        gameObject.addCreatePacketToBuffer(createPackets);
        serverNetwork.sendPacketsToAll(createPackets);
        createPackets.clear();
    }
}
