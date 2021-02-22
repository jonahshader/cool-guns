package sophomoreproject.game.networking.serverlisteners;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.packets.RequestGameData;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.GameWorld;

import java.util.HashMap;

/**
 * RequestListener listens to the clients for any requests.
 * If the player requests the game data, this class will
 * create a player for that client if nessesary.
 * If the player already exists, it will wake it up.
 * Client will need to detect when a CreatePlayer packet
 * is sent where the packet's accountID matches the client's
 * account id. This will be done in the ObjectCreationListener
 * client listener class.
 */
public class RequestListener implements Listener {
    private GameServer gameServer;
    private GameWorld world;
    private HashMap<Connection, Integer> connectionToAccountID;

    public RequestListener(GameServer gameServer, GameWorld world, HashMap<Connection, Integer> connectionToAccountID) {
        this.gameServer = gameServer;
        this.world = world;
        this.connectionToAccountID = connectionToAccountID;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof RequestGameData) {
            gameServer.sendAllWorldDataToClient(c);

            if (connectionToAccountID.containsKey(c)) {
                int playerAccountID = connectionToAccountID.get(c);
                int playerNetID = world.getSleepingPlayerNetIDFromAccountID(playerAccountID);
                if (playerNetID >= 0) {
                    gameServer.setAndSendSleepState(playerNetID, false);
//                    world.handleSetSleepStatePacket(new UpdateSleepState(playerNetID, false));
                    System.out.println("Waking up player with accountID " + playerAccountID + " and netID " + playerNetID + "!");
                } else {
                    // create player
                    Player newPlayer = new Player(new Vector2(), playerAccountID, world.getNewNetID(), false);
                    // register with world and distribute
                    gameServer.spawnAndSendGameObject(newPlayer);
                    System.out.println("Created new player with account id " + playerAccountID + " and net id " + newPlayer.getNetworkID() + "!");
                }
            } else {
                System.out.println("WARNING: Account not found corresponding to connection " + c.toString());
                System.out.println("This should never happen...");
            }
        }
    }
}
