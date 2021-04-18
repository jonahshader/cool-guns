package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.packets.*;
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
        } else if (o instanceof InventoryChange) {
            world.handleInventoryChangePacket((InventoryChange) o);
        } else if (o instanceof AttackPlayer) {
            world.handleAttackPlayerPacket((AttackPlayer)o);
        } else if (o instanceof AttackFeedback) {
            world.handleAttackFeedbackPacket((AttackFeedback) o);
        }
    }
}
