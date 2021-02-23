package sophomoreproject.game.systems;

import com.esotericsoftware.kryonet.Connection;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.networking.serverlisteners.RequestListener;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.systems.gameplaysystems.GameSystem;
import sophomoreproject.game.systems.gameplaysystems.spawners.TestObjectSpawner;

import java.util.ArrayList;

public class GameServer {
    private GameWorld world;
    private ServerNetwork serverNetwork;
    private double time = 0.0;

    private ArrayList<Object> createPackets = new ArrayList<>();
    private ArrayList<GameSystem> gameSystems = new ArrayList<>();

    public GameServer(ServerNetwork serverNetwork) {
        world = new GameWorld();
        this.serverNetwork = serverNetwork;

        // add game systems
        gameSystems.add(new TestObjectSpawner(this, world));

        // add listeners
        serverNetwork.addListener(new RequestListener(this, world, serverNetwork.getConnectionToAccountID()));
    }

    public void run(float dt) {
        // run game systems
        for(GameSystem g : gameSystems) g.run(dt);

        world.serverOnly(dt, serverNetwork);
        world.update(dt);

        time += dt;
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

    public void sendAllWorldDataToClient(Connection c) {
        ArrayList<Object> worldPackets = world.createWorldCopy();
        for (Object packet : worldPackets) c.sendTCP(packet);
    }

    public GameWorld getGameWorld() {
        return world;
    }

    public void setAndSendSleepState(int networkID, boolean sleeping) {
        UpdateSleepState packet = new UpdateSleepState(networkID, sleeping);
        world.handleSetSleepStatePacket(packet);
        serverNetwork.sendPacketToAll(packet);
    }

    public void setAndSendSleepState(UpdateSleepState packet) {
        world.handleSetSleepStatePacket(packet);
        serverNetwork.sendPacketToAll(packet);
    }
}
