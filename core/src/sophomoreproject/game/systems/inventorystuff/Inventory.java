package sophomoreproject.game.systems.inventorystuff;

import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.InventoryItem;

import java.util.ArrayList;

public class Inventory {
    public class InventoryPacket {
        public ArrayList<Object> createPackets;
    }

    public Inventory(InventoryPacket packet) {

    }

    private String name;
    private ArrayList<InventoryItem> items;
    private int maxSize;

    public void addItem(InventoryItem item) {

    }

    public void removeItem(int index) {

    }

    public InventoryItem getItem(int index) {
        return items.get(index);
    }

    public void receiveUpdate(Object inventoryUpdatePacket) {
    }
}
