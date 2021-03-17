package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private final Map<Integer, PhysicsObject> physicsObjects = new ConcurrentHashMap<>();
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final Map<Integer, GameObject> gameObjects = new ConcurrentHashMap<>();
    private final Map<Integer, GameObject> sleepingGameObjects = new ConcurrentHashMap<>();
    private final List<Renderable> renderables = new ArrayList<>();

    private final ArrayList<Object> serverSendUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> receiveUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectAddQueue = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectRemoveQueue = new ArrayList<>();
    private final ArrayList<GameObject> wakeToSleepingGameObjectQueue = new ArrayList<>();
    private final ArrayList<GameObject> sleepingToWakeGameObjectQueue = new ArrayList<>();

    private final ReentrantLock updatePacketsLock = new ReentrantLock();
    private final ReentrantLock gameObjectQueueLock = new ReentrantLock();
    private final ReentrantLock sleepUpdateLock = new ReentrantLock();

    private int currentMaxNetID = -1;

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt
     */
    public void update(float dt) {
        // handle update packets
        updatePacketsLock.lock();
        for (Object o : receiveUpdatePacketBuffer) {
            if (o instanceof UpdatePhysicsObject) {
                UpdatePhysicsObject packet = (UpdatePhysicsObject) o;
                PhysicsObject toUpdate = getPhysicsObjectFromID(packet.netID);
                if (toUpdate != null)
                    toUpdate.updateFromPacket(packet);
            } else if (o instanceof UpdateItem) {
                UpdateItem packet = (UpdateItem) o;
                Item toUpdate = (Item)getGameObjectFromID(packet.netID);
                if (toUpdate != null)
                    toUpdate.receiveUpdate(packet);
            } else if (o instanceof UpdatePlayer) {
                UpdatePlayer packet = (UpdatePlayer) o;
                Player toUpdate = (Player)getGameObjectFromID(packet.netID);
                if (toUpdate != null)
                    toUpdate.receiveUpdate(packet);
            }
        }
        receiveUpdatePacketBuffer.clear();
        updatePacketsLock.unlock();


        // process gameObject add and remove queues
        gameObjectQueueLock.lock();
        for (GameObject o : gameObjectAddQueue) addObject(o);
        for (GameObject o : gameObjectRemoveQueue) removeObject(o);
        gameObjectAddQueue.clear();
        gameObjectRemoveQueue.clear();
        gameObjectQueueLock.unlock();

        sleepUpdateLock.lock();
        // move from sleeping object array to normal working arrays
        for (GameObject o : sleepingToWakeGameObjectQueue) {
            sleepingGameObjects.remove(o);
            addObject(o);
            System.out.println("Object with id " + o.getNetworkID() + " is awake now.");
        }
        // move from
        for (GameObject o : wakeToSleepingGameObjectQueue) {
            sleepingGameObjects.put(o.getNetworkID(), o);
            removeObject(o);
            System.out.println("Object with id " + o.getNetworkID() + " is sleeping now.");
        }
        sleepingToWakeGameObjectQueue.clear();
        wakeToSleepingGameObjectQueue.clear();
        sleepUpdateLock.unlock();

        for (PhysicsObject p : physicsObjects.values()) { p.updatePhysics(dt); }
    }

    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        for (Renderable r : renderables) r.draw(dt, sb, sr);
    }

    public void serverOnly(float dt, ServerNetwork serverNetwork, GameServer server) {
        for (GameObject g : gameObjects.values()) g.run(dt, server);
        for (GameObject g : gameObjects.values()) {
            if (g.getUpdateFrequency() == GameObject.ServerUpdateFrequency.CONSTANT) {
                g.addUpdatePacketToBuffer(serverSendUpdatePacketBuffer);
            }
        }

        serverNetwork.sendPacketsToAll(serverSendUpdatePacketBuffer, false);
        serverSendUpdatePacketBuffer.clear();


    }

    public void handleSetSleepStatePacket(UpdateSleepState packet) {
        sleepUpdateLock.lock();
        if (packet.sleeping) {
            GameObject obj = getGameObjectFromID(packet.networkID);
            if (obj == null) {
                obj = getSleepingGameObjectFromID(packet.networkID);
            }
            if (obj != null) {
                wakeToSleepingGameObjectQueue.add(obj);
            } else {
                System.out.println("Tried updating sleep state of null object! " + packet.toString());
            }
        } else {
            GameObject obj = getSleepingGameObjectFromID(packet.networkID);
            if (obj == null) {
                obj = getGameObjectFromID(packet.networkID);
            }
            if (obj != null) {
                sleepingToWakeGameObjectQueue.add(obj);
            } else {
                System.out.println("Tried updating sleep state of null object! " + packet.toString());
            }
        }
        sleepUpdateLock.unlock();
    }

    public void queueAddObject(GameObject o) {
        gameObjectQueueLock.lock();
        gameObjectAddQueue.add(o);
        gameObjectQueueLock.unlock();
    }

    public void addSleepingObject(GameObject o) {
        sleepUpdateLock.lock();
        sleepingGameObjects.put(o.getNetworkID(), o);
        sleepUpdateLock.unlock();
    }

    public void queueRemoveObject(GameObject o) {
        gameObjectQueueLock.lock();
        gameObjectRemoveQueue.add(o);
        gameObjectQueueLock.unlock();
    }

    public void queueAddUpdatePacket(Object packet) {
        updatePacketsLock.lock();
        receiveUpdatePacketBuffer.add(packet);
        updatePacketsLock.unlock();
    }

    private void addObject(GameObject o) {
        if (o instanceof PhysicsObject) physicsObjects.put(o.getNetworkID(), (PhysicsObject) o);
        if (o instanceof Renderable) renderables.add((Renderable) o);
        if (o instanceof Player) players.put(o.getNetworkID(), (Player)o);
        gameObjects.put(o.getNetworkID(), o);
    }

    private void removeObject(GameObject o) {
        if (o instanceof PhysicsObject) physicsObjects.remove(o.getNetworkID());
        if (o instanceof Renderable) renderables.remove(o);
        gameObjects.remove(o.getNetworkID());
        sleepingGameObjects.remove(o.getNetworkID());
        players.remove(o.getNetworkID());
    }

    public GameObject getGameObjectFromID(int networkID) {
        return gameObjects.get(networkID);
//        for (GameObject g : gameObjects.values()) if (g.getNetworkID() == networkID) {
//            return g;
//        }
//        return null;
    }

    public GameObject getSleepingGameObjectFromID(int networkID) {
        return sleepingGameObjects.get(networkID);
//        for (GameObject g : sleepingGameObjects) if (g.getNetworkID() == networkID) return g;
//        return null;
    }

    public PhysicsObject getPhysicsObjectFromID(int networkID) {
        return physicsObjects.get(networkID);
//        for (PhysicsObject p : physicsObjects) if (p.getNetworkID() == networkID) return p;
//        return null;
    }

    public ArrayList<Object> createWorldCopy() {
        ArrayList<Object> worldCopy = new ArrayList<>();
        ArrayList<Object> sleepingCreatePackets = new ArrayList<>();
        // add wakeful objects
        for (GameObject g : gameObjects.values()) {
            g.addCreatePacketToBuffer(worldCopy);
        }
        // add sleeping objects to temp array
        for (GameObject g : sleepingGameObjects.values()) {
            g.addCreatePacketToBuffer(sleepingCreatePackets);
        }
        // wrap sleeping object create packets with CreateSleeping packet
        for (Object p : sleepingCreatePackets) {
            worldCopy.add(new CreateSleeping(p));
        }
        return worldCopy;
    }

    public synchronized int getNewNetID() {
//        if (gameObjectAddQueue.size() == 0) {
//            if (gameObjects.size() == 0) return 0;
//            else return gameObjects.get(gameObjects.size() - 1).getNetworkID() + 1;
//        } else {
//            return gameObjectAddQueue.get(gameObjectAddQueue.size() - 1).getNetworkID() + 1;
//        }

        return ++currentMaxNetID;

    }

    public int getPlayerNetIDFromAccountID(int accountID) {
        for (PhysicsObject o : physicsObjects.values()) {
            if (o instanceof Player) {
                Player p = (Player) o;
                if (p.getAccountId() == accountID) {
                    return p.getNetworkID();
                }
            }
        }
        return -1;
    }

    public int getSleepingPlayerNetIDFromAccountID(int accountID) {
        for (GameObject o : sleepingGameObjects.values()) {
            if (o instanceof Player) {
                Player p = (Player) o;
                if (p.getAccountId() == accountID) {
                    return p.getNetworkID();
                }
            }
        }
        return -1;
    }

    public Collection<PhysicsObject> getPhysicsObjects() {
        return physicsObjects.values();
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }
}
