package sophomoreproject.game.packets;

public class UpdateItem {
    public int netID;
    public boolean equipped;
    public float xPos, yPos;
    public float xAngle, yAngle;

    public UpdateItem(int netID, boolean equipped, float xPos, float yPos, float xAngle, float yAngle) {
        this.netID = netID;
        this.equipped = equipped;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
    }

    public UpdateItem(){}
}
