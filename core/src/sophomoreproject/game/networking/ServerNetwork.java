package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sophomoreproject.game.systems.GameServer;

import java.io.IOException;

public class ServerNetwork {
    private Server server;
    private GameServer gameServer;

    public ServerNetwork(int port, GameServer gameServer) {
        this.gameServer = gameServer;
        server = new Server();
        try {
            server.bind(port, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                
            }
        });
    }

}
