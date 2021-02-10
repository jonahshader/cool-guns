package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.TestObject;

public class CreatePlayer {
    public UpdatePhysicsObject u;
    public int accountId;

    public CreatePlayer(Player toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        accountId = toSend.getAccountId();
    }
}
