package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.enemystuff.Enemy;
import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.packets.*;
import sophomoreproject.game.systems.GameClient;
import sophomoreproject.game.systems.GameWorld;

public class ObjectCreationListener implements Listener {
    private GameWorld world;
    private GameClient gameClient;

    public ObjectCreationListener(GameWorld world, GameClient gameClient) {
        this.world = world;
        this.gameClient = gameClient;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof CreateSleeping) {
            handlePacket(c, ((CreateSleeping)o).packet, true);
        } else {
            handlePacket(c, o, false);
        }
    }

    private void handlePacket(Connection c, Object o, boolean sleepingObj) {
        GameObject toQueue = null;
        if (o instanceof CreatePlayer) {
            CreatePlayer packet = (CreatePlayer) o;
            Player newPlayer = new Player(packet);
            toQueue = newPlayer;
            // if this player is owned by this client,
            if (newPlayer.getAccountId() == gameClient.getAccountID())
                // then tell the game client that this player is to be controlled
                gameClient.setClientControlledPlayer(newPlayer);
        } else if (o instanceof CreateTestObject) {
            CreateTestObject packet = (CreateTestObject) o;
            toQueue = new TestObject(packet);
        } else if (o instanceof CreateBullet) {
            toQueue = new Bullet((CreateBullet) o, true);
        } else if (o instanceof CreateInventoryGun) {
            toQueue = new Gun((CreateInventoryGun) o);
        } else if (o instanceof CreateEnemy) {
            toQueue = new Enemy((CreateEnemy) o);
        }


        if (toQueue != null) {
            if (sleepingObj) {
                world.addSleepingObject(toQueue);
            } else {
                world.queueAddObject(toQueue);
            }
        }

    }
}
