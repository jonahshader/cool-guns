package sophomoreproject.game.packets;

public class UpdatePhysicsObject {
    public long netID;
    public float x, y, xVel, yVel, xAccel, yAccel;

    public UpdatePhysicsObject(long netID, float x, float y, float xVel, float yVel, float xAccel, float yAccel) {
        this.netID = netID;
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
        this.xAccel = xAccel;
        this.yAccel = yAccel;
    }
}
