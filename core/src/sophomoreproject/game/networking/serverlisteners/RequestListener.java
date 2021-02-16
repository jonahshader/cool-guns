package sophomoreproject.game.networking.serverlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.packets.RequestGameData;
import sophomoreproject.game.systems.GameServer;

public class RequestListener implements Listener {
    private GameServer gameServer;

    public RequestListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof RequestGameData) {
            gameServer.sendAllWorldDataToClient(c);
        }
    }
}
