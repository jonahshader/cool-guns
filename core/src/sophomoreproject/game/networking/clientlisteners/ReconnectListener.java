package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.ClientNetwork;

import java.io.IOException;

public class ReconnectListener implements Listener {
    private boolean reconnectQueued = false;

    @Override
    public void disconnected(Connection connection) {
        reconnectQueued = true;
        System.out.println("Lost connection!");
    }

    public void update() {
        if (reconnectQueued) {
            System.out.println("Reconnecting...");
            try {
                ClientNetwork.getInstance().getClient().reconnect(10000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            reconnectQueued = false;
        }
    }
}
