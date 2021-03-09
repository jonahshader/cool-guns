package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;

import java.util.ArrayList;

public class CreatePlayer {
    public UpdatePhysicsObject u;
    public int accountId;
    public String username;
    public ArrayList<Object> inventoryPackets;

    public CreatePlayer(Player toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        accountId = toSend.getAccountId();
        username = toSend.getUsername();
        inventoryPackets = new ArrayList<>();

        // convert inventory to inventory packets
        for (Item item : toSend.getInventory()) {
            if (item == null) {
                inventoryPackets.add(null);
            } else {
                item.addCreatePacketToBuffer(inventoryPackets);
            }
        }
    }

    public CreatePlayer(){} // no arg constructor for KryoNet internal usage
}
