package sophomoreproject.game.networking.serverlisteners;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.networking.ConnectedAccount;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.packets.RequestGameData;
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
    private HashMap<Integer, ConnectedAccount> usersLoggedIn;

    public RequestListener(GameServer gameServer, GameWorld world, ServerNetwork serverNetwork) {
        this.gameServer = gameServer;
        this.world = world;
        this.connectionToAccountID = serverNetwork.getConnectionToAccountID();
        this.usersLoggedIn = serverNetwork.getUsersLoggedIn();
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
                    String username = usersLoggedIn.get(playerAccountID).getAccount().username;
                    Player newPlayer = new Player(new Vector2(), playerAccountID, world.getNewNetID(), username);
                    // register with world and distribute
                    gameServer.spawnAndSendGameObject(newPlayer);
                    System.out.println("Created new player with account id " + playerAccountID + " and net id " + newPlayer.getNetworkID() + "!");
                }
            } else {
                System.out.println("WARNING: Account not found corresponding to connection " + c.toString());
                System.out.println("This should never happen...");
            }
        } else if (o instanceof CreateBullet) {
            ((CreateBullet) o).u.netID = world.getNewNetID();
            Bullet b = new Bullet((CreateBullet) o);
            gameServer.spawnAndSendGameObject(b);
        }
    }
}
