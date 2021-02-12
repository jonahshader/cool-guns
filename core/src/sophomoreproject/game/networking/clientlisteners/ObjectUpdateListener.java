package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.systems.GameWorld;

public class ObjectUpdateListener extends Listener {
    private GameWorld world;

    public ObjectUpdateListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof UpdatePhysicsObject) {
            world.queueAddUpdatePacket(o);
        }
    }
}
