package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.enemystuff.Enemy;
import sophomoreproject.game.interfaces.CollisionReceiver;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private final Map<Integer, PhysicsObject> physicsObjects = new ConcurrentHashMap<>();
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final Map<Integer, GroundItem> groundItems = new ConcurrentHashMap<>();
    private final Map<Integer, GameObject> gameObjects = new ConcurrentHashMap<>();
    private final Map<Integer, GameObject> sleepingGameObjects = new ConcurrentHashMap<>();
    private final List<Renderable> renderables = new ArrayList<>();

    private final ArrayList<Object> serverSendUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> receiveUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectAddQueue = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectRemoveQueue = new ArrayList<>();
    private final ArrayList<GameObject> wakeToSleepingGameObjectQueue = new ArrayList<>();
    private final ArrayList<GameObject> sleepingToWakeGameObjectQueue = new ArrayList<>();
    private final List<InventoryChange> inventoryChangeQueue = Collections.synchronizedList(new ArrayList<>());

    private final ReentrantLock updatePacketsLock = new ReentrantLock();
    private final ReentrantLock gameObjectQueueLock = new ReentrantLock();
    private final ReentrantLock sleepUpdateLock = new ReentrantLock();

    private int currentMaxNetID = -1;

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt delta time
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
            } else if (o instanceof UpdateEnemy) {
                UpdateEnemy packet = (UpdateEnemy) o;
                Enemy toUpdate = (Enemy)getGameObjectFromID(packet.netID);
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
            sleepingGameObjects.remove(o.getNetworkID());
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

        // handle inventory changes
        synchronized(inventoryChangeQueue) {
            for (InventoryChange ic : inventoryChangeQueue) {
                GameObject inventory = getGameObjectFromID(ic.inventoryNetID);
                if (ic.adding) {
                    if (inventory instanceof Player) {
                        ((Player) inventory).addToInventory(ic.itemNetID, ic.itemIndex);
                    }
                } else {
                    if (inventory instanceof Player) {
                        ((Player) inventory).removeFromInventory(ic.itemNetID);
                    }
                }
            }

            inventoryChangeQueue.clear();
        }

        for (PhysicsObject p : physicsObjects.values()) { p.updatePhysics(dt); }
    }

    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        // render ground items first
        for (GroundItem g : groundItems.values()) {
            g.draw(sb);
        }
        // render everything else after
        for (Renderable r : renderables) {
            r.draw(dt, sb, sr);
        }
    }

    public void serverOnly(float dt, ServerNetwork serverNetwork, GameServer server) {
        for (GameObject g : gameObjects.values()) g.run(dt, server);
        for (GameObject g : gameObjects.values()) {
            if (g.getUpdateFrequency() == GameObject.ServerUpdateFrequency.CONSTANT) {
                g.addUpdatePacketToBuffer(serverSendUpdatePacketBuffer);
            }
        }

        serverNetwork.sendPacketsToAll(serverSendUpdatePacketBuffer, true);
        serverSendUpdatePacketBuffer.clear();
    }

    public void handleSetSleepStatePacket(UpdateSleepState packet) {
        sleepUpdateLock.lock();
        if (packet.sleeping) {
            GameObject obj = getGameObjectFromID(packet.networkID);
            if (obj != null) {
                wakeToSleepingGameObjectQueue.add(obj);
            } else {
                System.out.println("Tried updating sleep state of null object! " + packet.toString());
            }
        } else {
            GameObject obj = getSleepingGameObjectFromID(packet.networkID);
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
        if (o instanceof GroundItem) groundItems.put(o.getNetworkID(), (GroundItem)o);
        gameObjects.put(o.getNetworkID(), o);
    }

    private void removeObject(GameObject o) {
        if (o instanceof PhysicsObject) physicsObjects.remove(o.getNetworkID());
        if (o instanceof Renderable) renderables.remove(o);
        if (o instanceof Player) players.remove(o.getNetworkID());
        if (o instanceof GroundItem) groundItems.remove(o.getNetworkID());
        gameObjects.remove(o.getNetworkID());
    }

    public GameObject getGameObjectFromID(int networkID) {
        return gameObjects.get(networkID);
    }

    public GameObject getSleepingGameObjectFromID(int networkID) {
        return sleepingGameObjects.get(networkID);
    }

    public PhysicsObject getPhysicsObjectFromID(int networkID) {
        return physicsObjects.get(networkID);
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
        System.out.println("Looking for sleeping player with account id " + accountID);
        for (GameObject o : sleepingGameObjects.values()) {
            if (o instanceof Player) {
                System.out.println("Found a player");
                Player p = (Player) o;
                if (p.getAccountId() == accountID) {
                    System.out.println("Player matches account id " + accountID);
                    return p.getNetworkID();
                } else {
                    System.out.println("Player does not match account id (looking for " + accountID + " but found " + p.getAccountId() + ")");
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

    public Collection<GroundItem> getGroundItems() {
        return groundItems.values();
    }

    public void handleInventoryChangePacket(InventoryChange ic) {
        synchronized (inventoryChangeQueue) {
            inventoryChangeQueue.add(ic);
        }
    }
}
