package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.packets.CreateTestObject;
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
        if (o instanceof CreatePlayer) {
            CreatePlayer packet = (CreatePlayer) o;
            Player newPlayer = new Player(packet, true);
            world.queueAddObject(newPlayer);
            // if this player is owned by this client,
            if (newPlayer.getAccountId() == gameClient.getAccountID())
                // then tell the game client that this player is to be controlled
                gameClient.setClientControlledPlayer(newPlayer);
        } else if (o instanceof CreateTestObject) {
            CreateTestObject packet = (CreateTestObject) o;
            world.queueAddObject(new TestObject(packet, true));
        }
    }
}
