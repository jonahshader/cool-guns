package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.packets.RegisterPackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.esotericsoftware.kryonet.Server.DEFAULT_OBJECT_BUUFER_SIZE;
import static com.esotericsoftware.kryonet.Server.DEFAULT_WRITE_BUFFER_SIZE;

public class ServerNetwork {
    private Server server; // kryonet server for sending and receiving packets
    private Accounts accounts; // this is the collection of registered accounts
    private HashMap<Integer, ConnectedAccount> usersLoggedIn; // (accountID to ConnectedAccount) this is the collection of users that are currently logged in
    private HashMap<Integer, Integer> connectionIdToAccountID; // this is too

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile();
        usersLoggedIn = new HashMap<>();
        connectionIdToAccountID = new HashMap<>();

        // if that load didn't work, just make a new accounts object
        if (accounts == null) {
            accounts = new Accounts();
            System.out.println("Made new accounts object");
        }

        server = new Server(DEFAULT_WRITE_BUFFER_SIZE * 32, DEFAULT_OBJECT_BUUFER_SIZE * 32);
        server.start();
        RegisterPackets.registerPackets(server.getKryo());
        try {
            server.bind(port, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPacketToAll(Object packet, boolean tcp) {
        if (tcp) {
            server.sendToAllTCP(packet);
        } else {
            server.sendToAllUDP(packet);
        }

    }

    public void sendPacketToAllExcept(Connection c, Object packet, boolean tcp) {
        if (tcp) {
            server.sendToAllExceptTCP(c.getID(), packet);
        } else {
            server.sendToAllExceptUDP(c.getID(), packet);
        }
    }

    public void sendPacketsToAll(ArrayList<Object> packets, boolean tcp) {
        if (tcp) {
            for (Object o : packets) server.sendToAllTCP(o);
        } else {
            for (Object o : packets) server.sendToAllUDP(o);
        }

    }

    public void addListener(Listener listener) {
        server.addListener(listener);
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public HashMap<Integer, ConnectedAccount> getUsersLoggedIn() {
        return usersLoggedIn;
    }

    public HashMap<Integer, Integer> getConnectionIdToAccountID() {
        return connectionIdToAccountID;
    }
}
