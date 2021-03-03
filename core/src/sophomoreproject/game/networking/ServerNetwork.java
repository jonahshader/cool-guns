package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.packets.RegisterPackets;
import sophomoreproject.game.systems.GameWorld;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetwork {
    private Server server; // kryonet server for sending and receiving packets
    private Accounts accounts; // this is the collection of registered accounts
    private HashMap<Integer, ConnectedAccount> usersLoggedIn; // (accountID to ConnectedAccount) this is the collection of users that are currently logged in
    private HashMap<Connection, Integer> connectionToAccountID; // this is too

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile();
        usersLoggedIn = new HashMap<>();
        connectionToAccountID = new HashMap<>();

        // if that load didn't work, just make a new accounts object
        if (accounts == null) {
            accounts = new Accounts();
        }

        server = new Server();
        server.start();
        RegisterPackets.registerPackets(server.getKryo());
        try {
            server.bind(port, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPacketToAll(Object packet) {
        server.sendToAllTCP(packet);
    }

    public void sendPacketToAllExcept(Connection c, Object packet) {
        server.sendToAllExceptTCP(c.getID(), packet);
    }

    public void sendPacketsToAll(ArrayList<Object> packets) {
        for (Object o : packets) server.sendToAllTCP(o);
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

    public HashMap<Connection, Integer> getConnectionToAccountID() {
        return connectionToAccountID;
    }
}
