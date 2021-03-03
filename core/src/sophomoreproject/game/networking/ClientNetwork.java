package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.clientlisteners.ReconnectListener;
import sophomoreproject.game.packets.RegisterPackets;

import java.io.IOException;
import java.util.ArrayList;

public final class ClientNetwork {
    private static ClientNetwork instance = null;
    private final Client client;
    private boolean firstConnect = false;
    private int accountID = -1;

    private ClientNetwork() {
        client = new Client();
        RegisterPackets.registerPackets(client.getKryo());
        client.start();

//        addListener(new ReconnectListener());
    }

    /**
     * @param ip
     * @param port
     * @return true: connection successful. false: connection failed (possibly already connected, or it just failed)
     */
    public boolean tryConnect(String ip, int port) {
        if (!firstConnect) {
            try {
                client.connect(2000, ip, port, port);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void sendPacket(Object packet) {
        client.sendTCP(packet);
    }

    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    public void removeListener(Listener listener) {
        client.removeListener(listener);
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

    public Client getClient() {
        return client;
    }

    public void sendAllPackets(ArrayList<Object> packets) {
        for (Object o : packets) client.sendTCP(o);
    }
}
