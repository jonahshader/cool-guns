package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.packets.RegisterPackets;
import sophomoreproject.game.packets.RequestNewAccount;
import sophomoreproject.game.systems.GameServer;

import java.io.IOException;

public class ServerNetwork {
    private static final String PATH_TO_ACCOUNTS = "accounts.data";
    private Server server;
    private Accounts accounts;

    public ServerNetwork(int port) {
        // try to load accounts file
        accounts = Accounts.loadFromFile(PATH_TO_ACCOUNTS);
        // if that load didn't work, just make a new object
        if (accounts == null) {
            accounts = new Accounts();
        }

        server = new Server();
        RegisterPackets.registerPackets(server.getKryo());
        try {
            server.bind(port, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof RequestNewAccount) {
                    if (accounts.tryAddAccount(
                            ((RequestNewAccount) object).username,
                            ((RequestNewAccount) object).password)) {
                        long accountID = accounts.tryGetAccountID(
                                ((RequestNewAccount) object).username,
                                ((RequestNewAccount) object).password);

                    }
                }
            }
        });
    }

}
