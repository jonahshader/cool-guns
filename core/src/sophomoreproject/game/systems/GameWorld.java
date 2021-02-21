package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.packets.UpdatePhysicsObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private final ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<GameObject> sleepingGameObjects = new ArrayList<>();
    private final ArrayList<Renderable> renderables = new ArrayList<>();

    private final ArrayList<Object> serverSendUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> clientReceiveUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> clientSendUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectAddQueue = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectRemoveQueue = new ArrayList<>();
    private final ArrayList<GameObject> wakeToSleepingGameObjectQueue = new ArrayList<>();
    private final ArrayList<GameObject> sleepingToWakeGameObjectQueue = new ArrayList<>();

    private final ReentrantLock clientUpdatePacketsLock = new ReentrantLock();
    private final ReentrantLock gameObjectQueueLock = new ReentrantLock();
    private final ReentrantLock sleepUpdateLock = new ReentrantLock();

    private int currentMaxNetID = -1;

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt
     */
    public void update(float dt) {
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
            sleepingGameObjects.add(o);
            removeObject(o);
            System.out.println("Object with id " + o.getNetworkID() + " is sleeping now.");
        }
        sleepingToWakeGameObjectQueue.clear();
        wakeToSleepingGameObjectQueue.clear();
        sleepUpdateLock.unlock();

        Collections.sort(physicsObjects);
        Collections.sort(gameObjects);

//        System.out.println("Start");
//        for (GameObject o : gameObjects) System.out.println(o.getNetworkID());

        for (PhysicsObject p : physicsObjects) { p.updatePhysics(dt); }
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        for (Renderable r : renderables) r.draw(sb, sr);
    }

    public void serverOnly(float dt, ServerNetwork serverNetwork) {
        for (GameObject g : gameObjects) g.run(dt);
        for (GameObject g : gameObjects) {
            if (g.getUpdateFrequency() == GameObject.ServerUpdateFrequency.CONSTANT) {
                g.addUpdatePacketToBuffer(serverSendUpdatePacketBuffer);
            }
        }

        serverNetwork.sendPacketsToAll(serverSendUpdatePacketBuffer);
        serverSendUpdatePacketBuffer.clear();
    }

    public void clientOnly(float dt) {
        clientUpdatePacketsLock.lock();
        // Receive update packets
        for (Object o : clientReceiveUpdatePacketBuffer) {
            if (o instanceof UpdatePhysicsObject) {
                UpdatePhysicsObject packet = (UpdatePhysicsObject) o;
                PhysicsObject toUpdate = getPhysicsObjectFromID(packet.netID);
                if (toUpdate != null) {
                    toUpdate.updateFromPacket(packet);
                }
            }
        }
        clientReceiveUpdatePacketBuffer.clear();
        clientUpdatePacketsLock.unlock();

        // Send update packets
        for (GameObject o : gameObjects) {
            if (o.getUpdateFrequency() == GameObject.ServerUpdateFrequency.SEND_ONLY) {
                o.addUpdatePacketToBuffer(clientSendUpdatePacketBuffer);
            }
        }
        ClientNetwork.getInstance().sendAllPackets(clientSendUpdatePacketBuffer);
        clientSendUpdatePacketBuffer.clear();
    }

    public void handleSetSleepStatePacket(UpdateSleepState packet) {
        sleepUpdateLock.lock();
        if (packet.sleeping) {
            wakeToSleepingGameObjectQueue.add(getGameObjectFromID(packet.networkID));
        } else {
            sleepingToWakeGameObjectQueue.add(getSleepingGameObjectFromID(packet.networkID));
        }
        sleepUpdateLock.unlock();
    }

    public void queueAddObject(GameObject o) {
        gameObjectQueueLock.lock();
        gameObjectAddQueue.add(o);
        gameObjectQueueLock.unlock();
    }

    public void queueRemoveObject(GameObject o) {
        gameObjectQueueLock.lock();
        gameObjectRemoveQueue.add(o);
        gameObjectQueueLock.unlock();
    }

    public void queueAddUpdatePacket(Object packet) {
        clientUpdatePacketsLock.lock();
        clientReceiveUpdatePacketBuffer.add(packet);
        clientUpdatePacketsLock.unlock();
    }

    private void addObject(GameObject o) {
        if (o instanceof PhysicsObject) physicsObjects.add((PhysicsObject) o);
        if (o instanceof Renderable) renderables.add((Renderable) o);
        gameObjects.add(o);
    }

    private void removeObject(GameObject o) {
        if (o instanceof PhysicsObject) physicsObjects.remove(o);
        if (o instanceof Renderable) renderables.remove(o);
        gameObjects.remove(o);
    }

    public GameObject getGameObjectFromID(int networkID) {
//        int index = getGameObjectIndexFromID(networkID, gameObjects);
//        if (index >= 0) {
//            return gameObjects.get(index);
//        } else {
//            return null;
//        }
        for (GameObject g : gameObjects) if (g.getNetworkID() == networkID) return g;
        return null;
    }

    public GameObject getSleepingGameObjectFromID(int networkID) {
        for (GameObject g : sleepingGameObjects) if (g.getNetworkID() == networkID) return g;
        return null;
    }

    public PhysicsObject getPhysicsObjectFromID(int networkID) {
//        int index = getPhysicsObjectIndexFromID(networkID, physicsObjects);
//        if (index >= 0) {
//            return physicsObjects.get(index);
//        } else {
//            return null;
//        }

        for (PhysicsObject p : physicsObjects) if (p.getNetworkID() == networkID) return p;
        return null;
    }

    // modified from: https://www.geeksforgeeks.org/binary-search/
//    private static int getGameObjectIndexFromID(int networkID, ArrayList<GameObject> array) {
//        int l = 0, r = array.size() - 1;
//
////        if (r <= 0) return -1;
//        while (l <= r) {
//            int m = l + (r - 1) / 2;
//
//            // check if x is present at mid
//            if (array.get(m).getNetworkID() == networkID)
//                return m;
//
//            // if x is greater, ignore left half
//            if (array.get(m).getNetworkID() < networkID)
//                l = m + 1;
//            // if x is smaller, ignore right half
//            else
//                r = m - 1;
//        }
//
//        // if element wasn't found, return -1
//        return -1;
//    }
//
//    private static int getPhysicsObjectIndexFromID(int networkID, ArrayList<PhysicsObject> array) {
//        int l = 0, r = array.size() - 1;
//
////        if (r <= 0) return -1;
//        while (l <= r) {
//            int m = l + (r - 1) / 2;
//
//            if (m > r || m < 0) return -1;
//            // check if x is present at mid
//            if (array.get(m).getNetworkID() == networkID)
//                return m;
//
//            // if x is greater, ignore left half
//            if (array.get(m).getNetworkID() < networkID)
//                l = m + 1;
//                // if x is smaller, ignore right half
//            else
//                r = m - 1;
//        }
//
//        // if element wasn't found, return -1
//        return -1;
//    }

    public ArrayList<Object> createWorldCopy() {
        ArrayList<Object> worldCopy = new ArrayList<>();
        // add wakeful objects
        for (GameObject g : gameObjects) {
            g.addCreatePacketToBuffer(worldCopy);
        }
        // add sleeping objects
        for (GameObject g : sleepingGameObjects) {
            g.addCreatePacketToBuffer(worldCopy);
        }
        return worldCopy;
    }

    public int getNewNetID() {
//        if (gameObjectAddQueue.size() == 0) {
//            if (gameObjects.size() == 0) return 0;
//            else return gameObjects.get(gameObjects.size() - 1).getNetworkID() + 1;
//        } else {
//            return gameObjectAddQueue.get(gameObjectAddQueue.size() - 1).getNetworkID() + 1;
//        }

        return ++currentMaxNetID;

    }

    public int getPlayerNetIDFromAccountID(int accountID) {
        for (PhysicsObject o : physicsObjects) {
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
        for (GameObject o : sleepingGameObjects) {
            if (o instanceof Player) {
                Player p = (Player) o;
                if (p.getAccountId() == accountID) {
                    return p.getNetworkID();
                }
            }
        }
        return -1;
    }
}
