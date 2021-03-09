package sophomoreproject.game.packets;

public class InventoryChange {
    public int inventoryNetID;
    public int itemIndex;
    public int itemNetID;
    public boolean adding;

    public InventoryChange(int inventoryNetID, int itemIndex, int itemNetID, boolean adding) {
        this.inventoryNetID = inventoryNetID;
        this.itemIndex = itemIndex;
        this.itemNetID = itemNetID;
        this.adding = adding;
    }

    public InventoryChange() {
    }
}
