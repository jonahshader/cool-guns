package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.UpdatePhysicsObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private final ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<Renderable> renderables = new ArrayList<>();

    private final ArrayList<Object> serverUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> clientUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectAddQueue = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectRemoveQueue = new ArrayList<>();

    private ReentrantLock clientUpdatePacketsLock = new ReentrantLock();
    private ReentrantLock gameObjectQueueLock = new ReentrantLock();

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

        Collections.sort(physicsObjects);
        Collections.sort(gameObjects);

        for (PhysicsObject p : physicsObjects) {
            p.updatePhysics(dt);
        }
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        for (Renderable r : renderables) r.draw(sb, sr);
    }

    public void serverOnly(float dt, ServerNetwork serverNetwork) {
        for (GameObject g : gameObjects) g.run(dt);
        for (GameObject g : gameObjects) {
            if (g.getUpdateFrequency() == GameObject.ServerUpdateFrequency.CONSTANT) {
                g.addUpdatePacketToBuffer(serverUpdatePacketBuffer);
            }
        }

        serverNetwork.sendPacketsToAll(serverUpdatePacketBuffer);
        serverUpdatePacketBuffer.clear();
    }

    public void clientOnly(float dt, ClientNetwork clientNetwork) {
        //TODO: learn how to use locks and synchronize instead of this volatile stuff. its not working
        clientUpdatePacketsLock.lock();
        for (Object o : clientUpdatePacketBuffer) {
            if (o instanceof UpdatePhysicsObject) {
                UpdatePhysicsObject packet = (UpdatePhysicsObject) o;
                PhysicsObject toUpdate = getPhysicsObjectFromID(packet.netID);
                if (toUpdate != null) {
                    toUpdate.updateFromPacket(packet);
                }
            } // TODO: add other Update packets
        }
        clientUpdatePacketBuffer.clear();
        clientUpdatePacketsLock.unlock();
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
        clientUpdatePacketBuffer.add(packet);
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
        int index = getGameObjectIndexFromID(networkID, gameObjects);
        if (index >= 0) {
            return gameObjects.get(index);
        } else {
            return null;
        }
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
    private static int getGameObjectIndexFromID(int networkID, ArrayList<GameObject> array) {
        int l = 0, r = array.size() - 1;

        if (r <= 0) return -1;
        while (l <= r) {
            int m = l + (r - 1) / 2;

            // check if x is present at mid
            if (array.get(m).getNetworkID() == networkID)
                return m;

            // if x is greater, ignore left half
            if (array.get(m).getNetworkID() < networkID)
                l = m + 1;
            // if x is smaller, ignore right half
            else
                r = m - 1;
        }

        // if element wasn't found, return -1
        return -1;
    }

    private static int getPhysicsObjectIndexFromID(int networkID, ArrayList<PhysicsObject> array) {
        int l = 0, r = array.size() - 1;

        if (r <= 0) return -1;
        while (l <= r) {
            int m = l + (r - 1) / 2;

            // check if x is present at mid
            if (array.get(m).getNetworkID() == networkID)
                return m;

            // if x is greater, ignore left half
            if (array.get(m).getNetworkID() < networkID)
                l = m + 1;
                // if x is smaller, ignore right half
            else
                r = m - 1;
        }

        // if element wasn't found, return -1
        return -1;
    }

    public ArrayList<Object> createWorldCopy() {
        ArrayList<Object> worldCopy = new ArrayList<>();
        for (GameObject g : gameObjects) {
            g.addCreatePacketToBuffer(worldCopy);
        }
        return worldCopy;
    }

    public int getNewNetID() {
        if (gameObjectAddQueue.size() == 0) {
            if (gameObjects.size() == 0) return 0;
            else return gameObjects.get(gameObjects.size() - 1).getNetworkID() + 1;
        } else {
            return gameObjectAddQueue.get(gameObjectAddQueue.size() - 1).getNetworkID() + 1;
        }

    }
}
