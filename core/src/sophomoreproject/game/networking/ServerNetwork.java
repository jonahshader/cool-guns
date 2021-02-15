package sophomoreproject.game.networking;

import com.esotericsoftware.kryonetty.ServerEndpoint;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.packets.RegisterPackets;

import java.io.IOException;
import java.util.ArrayList;

public class ServerNetwork {
    private ServerEndpoint server;
    private Accounts accounts;

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile();
        // if that load didn't work, just make a new accounts object
        if (accounts == null) {
            accounts = new Accounts();
        }

        server = new ServerEndpoint(RegisterPackets.makeKryoNetty());

        server.start(port);

        // add listeners to server
        server.getEventHandler().register(new AccountListener(accounts));
    }

    public void sendPacketToAll(Object packet) {
        server.se
        server.sendToAllTCP(packet);
    }

    public void sendPacketsToAll(ArrayList<Object> packets) {
        for (Object o : packets) server.sendToAllTCP(o);
    }

    public void addListener(Listener listener) {
        server.addListener(listener);
    }
}
