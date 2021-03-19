package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.packets.UpdateEnemy;
import sophomoreproject.game.packets.UpdateItem;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.packets.UpdatePlayer;
import sophomoreproject.game.systems.GameWorld;

public class ObjectUpdateListener implements Listener {
    private GameWorld world;

    public ObjectUpdateListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof UpdatePhysicsObject) {
            world.queueAddUpdatePacket(o);
        } else if (o instanceof UpdateItem) {
            world.queueAddUpdatePacket(o);
        } else if (o instanceof UpdatePlayer) {
            world.queueAddUpdatePacket(o);
        } else if (o instanceof UpdateEnemy) {
            world.queueAddUpdatePacket(o);
        }
    }
}
