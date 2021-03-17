package sophomoreproject.game.networking.serverlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.packets.UpdateItem;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.packets.UpdatePlayer;
import sophomoreproject.game.packets.UpdateSleepState;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.GameWorld;

/**
 * some packets are received from the client for updating stuff on the server.
 * for example, player update requires the client to tell the server the state
 * of the client's player (which is via a UpdatePhysicsObject packet)
 *
 * i will include the UpdateSleepState packet here too
 */
public class RelaySendOnlyPacketsListener implements Listener {
    private ServerNetwork serverNetwork; // needed for sending out relay packets
    private GameWorld world; // needed for queuing update packet
    private GameServer gameServer;

    public RelaySendOnlyPacketsListener(ServerNetwork serverNetwork, GameServer gameServer) {
        this.serverNetwork = serverNetwork;
        this.gameServer = gameServer;
        this.world = gameServer.getGameWorld();
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof UpdatePhysicsObject) {
            world.queueAddUpdatePacket(o);
            serverNetwork.sendPacketToAllExcept(c, o, false);
        } else if (o instanceof UpdateSleepState) {
            gameServer.setAndSendSleepState((UpdateSleepState) o);
        } else if (o instanceof UpdateItem) {
            world.queueAddUpdatePacket(o);
            serverNetwork.sendPacketToAllExcept(c, o, false);
        } else if (o instanceof UpdatePlayer) {
            world.queueAddUpdatePacket(o);
            serverNetwork.sendPacketToAllExcept(c, o, false);
        }
    }
}
