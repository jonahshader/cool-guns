package sophomoreproject.game.systems.inventorystuff;

import sophomoreproject.game.interfaces.Item;

import java.util.ArrayList;

public class Inventory {
    public class InventoryPacket {
        public ArrayList<Object> createPackets;
    }

    public Inventory(InventoryPacket packet) {

    }

    private String name;
    private ArrayList<Item> items;
    private int maxSize;

    public void addItem(Item item) {

    }

    public void removeItem(int index) {

    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public void receiveUpdate(Object inventoryUpdatePacket) {
    }
}
