package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;

import java.util.ArrayList;

public class CreatePlayer {
    public UpdatePhysicsObject u;
    public int accountId;
    public String username;
    public ArrayList<Integer> inventoryItems;

    public CreatePlayer(Player toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        accountId = toSend.getAccountId();
        username = toSend.getUsername();
        inventoryItems = new ArrayList<>();

        // convert inventory to inventory packets
        for (Integer item : toSend.getInventory()) {
            if (item == null) {
                inventoryItems.add(null);
            } else {
                inventoryItems.add(item);
            }
        }
    }

    public CreatePlayer(){} // no arg constructor for KryoNet internal usage
}
