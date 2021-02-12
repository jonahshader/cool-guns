package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.ServerNetwork;

import java.util.ArrayList;
import java.util.Collections;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private final ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<Renderable> renderables = new ArrayList<>();

    private final ArrayList<Object> serverUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<Object> clientUpdatePacketBuffer = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectAddQueue = new ArrayList<>(), gameObjectRemoveQueue = new ArrayList<>();

    private volatile boolean readingClientUpdatePackets = false;

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt
     */
    public void update(float dt) {
        // process gameObject add and remove queues
        for (GameObject o : gameObjectAddQueue) addObject(o);
        for (GameObject o : gameObjectRemoveQueue) removeObject(o);
        gameObjectAddQueue.clear();
        gameObjectRemoveQueue.clear();

        Collections.sort(physicsObjects);
        Collections.sort(gameObjects);

        for (PhysicsObject p : physicsObjects) {
            p.updatePhysics(dt);
        }
    }

    public void draw(SpriteBatch sb) {
        for (Renderable r : renderables) r.draw(sb);
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
        readingClientUpdatePackets = true;
        for (Object o : clientUpdatePacketBuffer) {

        }
        readingClientUpdatePackets = false;
    }

    public void queueAddObject(GameObject o) {
        gameObjectAddQueue.add(o);
    }

    public void queueRemoveObject(GameObject o) {
        gameObjectRemoveQueue.add(o);
    }

    public void queueAddUpdatePacket(Object packet) {
//        while (readingClientUpdatePackets) {}
        clientUpdatePacketBuffer.add(packet);
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

    public GameObject getObjectFromID(int networkID) {
        int index = getObjectIndexFromID(networkID, gameObjects);
        if (index >= 0) {
            return gameObjects.get(getObjectIndexFromID(networkID, gameObjects));
        } else {
            return null;
        }
    }

    // modified from: https://www.geeksforgeeks.org/binary-search/
    private static int getObjectIndexFromID(int networkID, ArrayList<GameObject> array) {
        int l = 0, r = array.size();
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

        // if element wasn't found, return null
        return -1;
    }
}
