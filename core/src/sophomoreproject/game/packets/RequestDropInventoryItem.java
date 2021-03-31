package sophomoreproject.game.packets;

public class RequestDropInventoryItem {
    public int playerId;
    public int inventoryItemId;

    public RequestDropInventoryItem(int playerId, int inventoryItemId) {
        this.playerId = playerId;
        this.inventoryItemId = inventoryItemId;
    }

    public RequestDropInventoryItem() {}
}
