package sophomoreproject.game.packets;

public class UpdateItem {
    public int netID;
    public boolean equipped;

    public UpdateItem(int netID, boolean equipped) {
        this.netID = netID;
        this.equipped = equipped;
    }

    public UpdateItem(){}
}
