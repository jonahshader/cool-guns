package sophomoreproject.game.networking;

import com.esotericsoftware.kryonetty.ClientEndpoint;
import com.esotericsoftware.kryonetty.network.handler.NetworkListener;
import sophomoreproject.game.packets.RegisterPackets;

import java.io.IOException;

public final class ClientNetwork {
    private static ClientNetwork instance = null;
    private final ClientEndpoint client;
    private boolean connected = false;
    private int accountID = -1;

    private ClientNetwork() {
        client = new ClientEndpoint(RegisterPackets.makeKryoNetty());
    }

    /**
     * @param ip
     * @param port
     * @return true: connection successful. false: connection failed (possibly already connected, or it just failed)
     */
    public boolean tryConnect(String ip, int port) {
        if (!connected) {
            client.connect(ip, port);
            connected = true;
            return true;
        } else {
            return false;
        }
    }

    public void sendPacket(Object packet) {
        client.send(packet);
    }

    public void addListener(NetworkListener listener) {
        client.getEventHandler().register(listener);
    }

    public int getAccountID() {
        return accountID;
    }

    public static ClientNetwork getInstance() {
        if (instance == null) {
            instance = new ClientNetwork();
        }

        return instance;
    }
}
