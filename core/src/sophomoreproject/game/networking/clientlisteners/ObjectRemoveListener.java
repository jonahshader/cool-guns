package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.packets.RemoveObject;
import sophomoreproject.game.systems.GameWorld;

public class ObjectRemoveListener implements Listener {
    private GameWorld world;

    public ObjectRemoveListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof RemoveObject) {
            GameObject toRemove = world.getGameObjectFromID(((RemoveObject) o).networkID);
            if (toRemove != null) {
                world.queueRemoveObject(toRemove);
            }
        }
    }
}
