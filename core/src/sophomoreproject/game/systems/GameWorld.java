package sophomoreproject.game.systems;

import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.GameObject;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameWorld {
    // note: the contents of these arrays are mutually exclusive.
    // one object should only exist in one array at a time, even though objects can be both PhysicsObject and GameObject
    private ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    /**
     * this method should be called by both client and server. just does physics for now.
     * @param dt
     */
    public void update(float dt) {
        Collections.sort(physicsObjects);
//        physicsObjects.parallelStream().forEach(o -> o.updatePhysics(dt));
//        physicsObjects.forEach(o -> o.updatePhysics(dt));
    }

    public void serverOnly(float dt) {
//        physicsObjects.parallelStream().forEach(o -> o.run(dt));
//        physicsObjects.forEach(o -> o.run(dt));
    }

    public void addPhysicsObject(PhysicsObject o) {
        physicsObjects.add(o);
    }

    public void removePhysicsObject(PhysicsObject o) {
        physicsObjects.remove(o);
    }

    public void removePhysicsObject(int networkID) {

    }


}
