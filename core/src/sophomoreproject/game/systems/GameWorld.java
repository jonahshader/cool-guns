package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;

import java.util.ArrayList;
import java.util.Collections;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Renderable> renderables = new ArrayList<>();

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt
     */
    public void update(float dt) {
        Collections.sort(physicsObjects);
        for (PhysicsObject p : physicsObjects) {
            p.updatePhysics(dt);
        }
    }

    public void draw(SpriteBatch sb) {
        for (Renderable r : renderables) r.draw(sb);
    }

    public void serverOnly(float dt) {
        for (PhysicsObject p : physicsObjects) p.run(dt);
    }

    public void addObject(Object o) {
        if (o instanceof PhysicsObject) physicsObjects.add((PhysicsObject) o);
        if (o instanceof Renderable) renderables.add((Renderable) o);
        if (o instanceof GameObject) gameObjects.add((GameObject) o);
    }

    public void removeObject(Object o) {
        if (o instanceof PhysicsObject) physicsObjects.remove(o);
        if (o instanceof Renderable) renderables.remove(o);
        if (o instanceof GameObject) gameObjects.remove(o);
    }

    public Object getObjectFromID(int networkID) {
        return getObjectFromID(networkID, gameObjects);
    }

    // modified from: https://www.geeksforgeeks.org/binary-search/
    private static GameObject getObjectFromID(int networkID, ArrayList<GameObject> array) {
        int l = 0, r = array.size();
        while (l <= r) {
            int m = l + (r - 1) / 2;

            // check if x is present at mid
            if (array.get(m).getNetworkID() == networkID)
                return array.get(m);

            // if x is greater, ignore left half
            if (array.get(m).getNetworkID() < networkID)
                l = m + 1;
            // if x is smaller, ignore right half
            else
                r = m - 1;
        }

        // if element wasn't found, return null
        return null;
    }


}
