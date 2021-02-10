package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.packets.CreateTestObject;
import sophomoreproject.game.systems.GameWorld;

public class ObjectCreationListener extends Listener {
    private GameWorld world;

    public ObjectCreationListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof CreatePlayer) {
            CreatePlayer packet = (CreatePlayer) o;
            world.addObject(new Player(packet));
        } else if (o instanceof CreateTestObject) {
            CreateTestObject packet = (CreateTestObject) o;
            world.addObject(new TestObject(packet));
        }
    }
}
