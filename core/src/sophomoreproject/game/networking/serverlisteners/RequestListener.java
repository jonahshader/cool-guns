package sophomoreproject.game.networking.serverlisteners;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
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
    private HashMap<Integer, Integer> connectionToAccountID;
    private HashMap<Integer, ConnectedAccount> usersLoggedIn;

    public RequestListener(GameServer gameServer, GameWorld world, ServerNetwork serverNetwork) {
        this.gameServer = gameServer;
        this.world = world;
        this.connectionToAccountID = serverNetwork.getConnectionIdToAccountID();
        this.usersLoggedIn = serverNetwork.getUsersLoggedIn();
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof RequestGameData) {
            gameServer.sendAllWorldDataToClient(c);

            if (connectionToAccountID.containsKey(c.getID())) {
                int playerAccountID = connectionToAccountID.get(c.getID());
                int playerNetID = world.getSleepingPlayerNetIDFromAccountID(playerAccountID);
                if (playerNetID >= 0) {
                    gameServer.setAndSendSleepState(playerNetID, false);
//                    world.handleSetSleepStatePacket(new UpdateSleepState(playerNetID, false));
                    System.out.println("Waking up player with accountID " + playerAccountID + " and netID " + playerNetID + "!");
                } else {
                    // create player
                    String username = usersLoggedIn.get(playerAccountID).getAccount().username;
                    Player newPlayer = new Player(new Vector2(), playerAccountID, world.getNewNetID(), username);

                    // make a default gun
                    GunInfo gunInfo = new GunInfo();
                    Gun gun = new Gun(gunInfo, newPlayer.getNetworkID(), world.getNewNetID());
                    gameServer.spawnAndSendGameObject(gun);

                    // make more guns
                    GunInfo autoGunInfo = new GunInfo();
                    autoGunInfo.gunType = Gun.GunType.SMG;
                    autoGunInfo.firingMode = Gun.FiringMode.AUTO;
                    autoGunInfo.fireDelay = 7/60f;
                    autoGunInfo.bulletsPerShot = 7;
                    autoGunInfo.clipSize = 63;
//                    autoGunInfo.playerKnockback = 1f;
                    Gun autoGun = new Gun(autoGunInfo, newPlayer.getNetworkID(), world.getNewNetID());
                    gameServer.spawnAndSendGameObject(autoGun);

                    GunInfo burstGunInfo = new GunInfo();
                    burstGunInfo.firingMode = Gun.FiringMode.BURST;
                    burstGunInfo.fireDelay = 0.05f;
                    burstGunInfo.clipSize = 13;
                    Gun burstGun = new Gun(burstGunInfo, newPlayer.getNetworkID(), world.getNewNetID());
                    gameServer.spawnAndSendGameObject(burstGun);

                    GunInfo shotgunInfo = new GunInfo();
                    shotgunInfo.gunType = Gun.GunType.SHOTGUN;
                    shotgunInfo.bulletsPerShot = 12;
                    shotgunInfo.clipSize = 24;
                    shotgunInfo.bulletSpeed = 225;
                    shotgunInfo.bulletSize = .8f;
                    shotgunInfo.fireDelay = 0.1f;
                    Gun shotgun = new Gun(shotgunInfo, newPlayer.getNetworkID(), world.getNewNetID());
                    gameServer.spawnAndSendGameObject(shotgun);

                    GunInfo sniperInfo = new GunInfo();
                    sniperInfo.gunType = Gun.GunType.RIFLE;
                    sniperInfo.clipSize = 1;
                    sniperInfo.reloadDelay = 0.001f;
                    sniperInfo.fireDelay = 0.001f;
                    sniperInfo.bulletSpeed = 900;
                    sniperInfo.spread = 0.001f;
                    sniperInfo.bulletDamage = 40000;
                    sniperInfo.bulletDamageVariance = 0;
                    Gun sniper = new Gun(sniperInfo, newPlayer.getNetworkID(), world.getNewNetID());
                    gameServer.spawnAndSendGameObject(sniper);

                    // put gun in player inventory
                    newPlayer.getInventory().set(0, gun.getNetworkID()); // first slot
                    newPlayer.getInventory().set(1, autoGun.getNetworkID()); // second slot
                    newPlayer.getInventory().set(2, burstGun.getNetworkID()); // third slot
                    newPlayer.getInventory().set(3, shotgun.getNetworkID());
                    newPlayer.getInventory().set(4, sniper.getNetworkID());

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
            Bullet b = new Bullet((CreateBullet) o, false);
            gameServer.spawnAndSendGameObject(b);
        }
    }
}
