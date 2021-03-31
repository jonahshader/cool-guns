package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;

import java.util.ArrayList;

public class CreatePlayer {
    public UpdatePhysicsObject u;
    public int accountId;
    public String username;
    public ArrayList<Integer> inventoryItems;

    public int health;
    public int maxHealth;
    public int shield;
    public int maxShield;
    public float stamina;

    public CreatePlayer(Player toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        accountId = toSend.getAccountId();
        username = toSend.getUsername();
        inventoryItems = new ArrayList<>();

        health = toSend.getHealth();
        maxHealth = toSend.getMaxHealth();
        shield = toSend.getShield();
        maxShield = toSend.getMaxShield();
        stamina = toSend.getStamina();

        // copy inventory
        inventoryItems.addAll(toSend.getInventory());
    }

    public CreatePlayer(){} // no arg constructor for KryoNet internal usage
}
