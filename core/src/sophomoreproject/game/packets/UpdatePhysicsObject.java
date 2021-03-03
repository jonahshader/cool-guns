package sophomoreproject.game.packets;

import com.badlogic.gdx.math.Vector2;

public class UpdatePhysicsObject {
    public int netID;
    public float x, y, xVel, yVel, xAccel, yAccel;

    public UpdatePhysicsObject(int netID, Vector2 position, Vector2 velocity, Vector2 acceleration) {
        this.netID = netID;
        x = position.x;
        y = position.y;
        xVel = velocity.x;
        yVel = velocity.y;
        xAccel = acceleration.x;
        yAccel = acceleration.y;
    }

    public UpdatePhysicsObject(int netID, float x, float y, float xVel, float yVel, float xAccel, float yAccel) {
        this.netID = netID;
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
        this.xAccel = xAccel;
        this.yAccel = yAccel;
    }

    public UpdatePhysicsObject(){} // no arg constructor for KryoNet internal usage
}
