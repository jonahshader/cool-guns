package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.packets.RegisterPackets;

import java.io.IOException;
import java.util.ArrayList;

public class ServerNetwork {
    private Server server;
    private Accounts accounts;

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile();
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

        // add listeners to server
        server.addListener(new AccountListener(accounts));
    }

    public void sendPacketToAll(Object packet) {
        server.sendToAllTCP(packet);
    }

    public void sendPacketsToAll(ArrayList<Object> packets) {
        for (Object o : packets) server.sendToAllTCP(o);
    }
}
