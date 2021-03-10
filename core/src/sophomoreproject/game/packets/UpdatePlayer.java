package sophomoreproject.game.packets;

public class UpdatePlayer {
    public int netID;
    public float xLook;
    public float yLook;

    public UpdatePlayer(int netID, float xLook, float yLook) {
        this.netID = netID;
        this.xLook = xLook;
        this.yLook = yLook;
    }

    public UpdatePlayer() {
    }
}
