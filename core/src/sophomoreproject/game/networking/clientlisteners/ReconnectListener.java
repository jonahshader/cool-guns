package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.singletons.HUD;

import java.io.IOException;

/**
 * TODO: this will mess up ServerNetwork's HashMap<Connection, Integer> connectionToAccountID
 * need to rework this to fully reconnect and retransmit game data
 */

public class ReconnectListener implements Listener {
//    private boolean reconnectQueued = false;

    @Override
    public void disconnected(Connection connection) {
//        reconnectQueued = true;
        System.out.println("Lost connection!");
        HUD.getInstance().setConnectionError(true);
        // TODO: implement auto reconnect
    }

//    public void update() {
//        if (reconnectQueued) {
//            System.out.println("Reconnecting...");
//            try {
//                ClientNetwork.getInstance().getClient().reconnect(10000);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            reconnectQueued = false;
//        }
//    }
}
