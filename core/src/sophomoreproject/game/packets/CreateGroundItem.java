package sophomoreproject.game.packets;

public class CreateGroundItem {
    public float x;
    public float y;
    public int netId;
    public float r;
    public float g;
    public float b;
    public float scale;
    public String textureName;
    public Object realizedObject;
    public boolean fresh;

    public CreateGroundItem(float x, float y, int netId, float r, float g, float b, float scale, String textureName, Object realizedObject, boolean fresh) {
        this.x = x;
        this.y = y;
        this.netId = netId;
        this.r = r;
        this.g = g;
        this.b = b;
        this.scale = scale;
        this.textureName = textureName;
        this.realizedObject = realizedObject;
        this.fresh = fresh;
    }

    public CreateGroundItem() {}
}
