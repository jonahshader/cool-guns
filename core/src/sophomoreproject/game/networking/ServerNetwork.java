package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.packets.RegisterPackets;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;
import sophomoreproject.game.systems.GameServer;

import java.io.IOException;

public class ServerNetwork {
    private Server server;
    private Accounts accounts;

    private long lastNanos;

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile();
        // if that load didn't work, just make a new object
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

        lastNanos = System.nanoTime();
    }

    public void update() {
        long nanos = System.nanoTime();
        double dt = nanos - lastNanos;
    }

}
