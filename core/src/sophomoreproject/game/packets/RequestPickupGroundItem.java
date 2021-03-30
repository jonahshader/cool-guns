package sophomoreproject.game.packets;

public class RequestPickupGroundItem {
    public int playerId;
    public int groundItemId;

    public RequestPickupGroundItem(int playerId, int groundItemId) {
        this.playerId = playerId;
        this.groundItemId = groundItemId;
    }

    public RequestPickupGroundItem() {}
}
