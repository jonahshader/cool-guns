package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Objects;

/**
 * network id is a unique id that is assigned by the server.
 */

public abstract class GameObject implements Comparable<GameObject>{
    public enum ServerUpdateFrequency {
        CONSTANT,
        ONCE,
        SEND_ONLY
    }
    protected int networkID;
    protected ServerUpdateFrequency updateFrequency;

    int getNetworkID() { return networkID; }
    ServerUpdateFrequency getUpdateFrequency() { return updateFrequency; }

    public abstract void run(float dt);

    @Override
    public int compareTo(GameObject o) {
        return networkID - o.networkID;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GameObject that = (GameObject) o;
//        return networkID == that.networkID;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(networkID);
//    }
}
