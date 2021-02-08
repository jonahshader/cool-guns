package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
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

    public int getNetworkID() { return networkID; }
    public ServerUpdateFrequency getUpdateFrequency() { return updateFrequency; }

    public abstract void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer);
    public abstract void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer);
    public abstract void run(float dt);

    @Override
    public int compareTo(GameObject o) {
        return networkID - o.networkID;
    }
}
