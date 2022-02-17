package sophomoreproject.game.interfaces;

import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

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
    private volatile boolean awake = true;

    public int getNetworkID() { return networkID; }
    public ServerUpdateFrequency getUpdateFrequency() { return updateFrequency; }

    public abstract void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer);
    public abstract void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer);
    public abstract void receiveUpdate(Object updatePacket);
    public abstract void run(float dt, GameServer server);

    public void setAwake(boolean awake) { this.awake = awake; }
    public boolean isAwake() { return awake; }

    @Override
    public int compareTo(GameObject o) {
        return networkID - o.networkID;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GameObject && ((GameObject)obj).networkID == networkID);
    }
}
