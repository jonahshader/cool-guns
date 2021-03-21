package sophomoreproject.game.systems;

import com.esotericsoftware.kryonet.Connection;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.networking.serverlisteners.RequestListener;
import sophomoreproject.game.packets.RemoveObject;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.systems.gameplaysystems.GameSystem;
import sophomoreproject.game.systems.gameplaysystems.spawners.TestObjectSpawner;
import sophomoreproject.game.systems.mapstuff.MapGenerator;
import sophomoreproject.game.systems.mapstuff.serverside.ServerMap;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class GameServer {
    public static final long GAME_SEED = 81528512;

    private GameWorld world;
    private ServerMap serverMap;
    private ServerNetwork serverNetwork;

    private ArrayList<Object> createPackets = new ArrayList<>();
    private ArrayList<GameSystem> gameSystems = new ArrayList<>();
    private ArrayList<GameObject> forceUpdateQueue = new ArrayList<>();
    private ArrayList<Object> forceUpdatePackets = new ArrayList<>();

    private ReentrantLock forceUpdateQueueLock = new ReentrantLock();



    public GameServer(ServerNetwork serverNetwork) {
        world = new GameWorld();
        this.serverNetwork = serverNetwork;
        serverMap = new ServerMap(new MapGenerator(null, GAME_SEED), this);

        // add game systems
//        gameSystems.add(new TestObjectSpawner(this, world));

        // add listeners
        serverNetwork.addListener(new RequestListener(this, world, serverNetwork));
    }

    public void run(float dt) {
        // run game systems
        for(GameSystem g : gameSystems) g.run(dt);

        world.serverOnly(dt, serverNetwork, this);
        world.update(dt);

        // send manual updates
        forceUpdateQueueLock.lock();
        for (GameObject toUpdate : forceUpdateQueue) {
            toUpdate.addUpdatePacketToBuffer(forceUpdatePackets);
        }
        serverNetwork.sendPacketsToAll(forceUpdatePackets, true);
        forceUpdatePackets.clear();
        forceUpdateQueue.clear();
        forceUpdateQueueLock.unlock();

        serverMap.update();
        serverMap.run(dt);
    }

    /**
     * adds a GameObject to the server's world and sends a creation packet to all clients
     * @param gameObject
     */
    public synchronized void spawnAndSendGameObject(GameObject gameObject) {
        world.queueAddObject(gameObject);
        gameObject.addCreatePacketToBuffer(createPackets);
        serverNetwork.sendPacketsToAll(createPackets, true);
        createPackets.clear();
    }

    public void sendAllWorldDataToClient(Connection c) {
        ArrayList<Object> worldPackets = world.createWorldCopy();
        for (Object packet : worldPackets) c.sendTCP(packet);
    }

    public GameWorld getGameWorld() {
        return world;
    }

    /**
     * sets sleep state of object with networkID and transmits a corresponding UpdateSleepPacket to all clients
     * @param networkID
     * @param sleeping
     */
    public void setAndSendSleepState(int networkID, boolean sleeping) {
        UpdateSleepState packet = new UpdateSleepState(networkID, sleeping);
        world.handleSetSleepStatePacket(packet);
        serverNetwork.sendPacketToAll(packet, true);
    }

    /**
     * sets sleep state of object with networkID and transmits a corresponding UpdateSleepPacket to all clients
     * @param packet
     */
    public void setAndSendSleepState(UpdateSleepState packet) {
        world.handleSetSleepStatePacket(packet);
        serverNetwork.sendPacketToAll(packet, true);
    }

    /**
     * removes the object with the specified id and transmits a RemoveObject packet to all clients
     * @param networkID id of object to remove
     */
    public void removeObject(int networkID) {
        RemoveObject packet = new RemoveObject(networkID);
        GameObject obj = world.getGameObjectFromID(packet.networkID);
        world.queueRemoveObject(obj);
        serverNetwork.sendPacketToAll(packet, true);
    }

    /**
     * queues gameobject with id to update manually
     * @param itemID
     */
    public void queueForceUpdate(int itemID) {
        GameObject toUpdate = world.getGameObjectFromID(itemID);
        if (toUpdate!= null) {
            forceUpdateQueueLock.lock();
            forceUpdateQueue.add(toUpdate);
            forceUpdateQueueLock.unlock();
        }
    }

    public ServerMap getServerMap() {
        return serverMap;
    }
}
