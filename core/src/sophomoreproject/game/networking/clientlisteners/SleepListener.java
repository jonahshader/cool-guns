package sophomoreproject.game.networking.clientlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.systems.GameWorld;

public class SleepListener implements Listener {
    private GameWorld world;

    public SleepListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof UpdateSleepState) {
            world.handleSetSleepStatePacket((UpdateSleepState) o);
        }
    }
}
