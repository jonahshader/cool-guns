package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.packets.UpdatePhysicsObject;

import java.util.ArrayList;

public abstract class PhysicsObject extends GameObject {
    public final Vector2 position;
    public final Vector2 velocity;
    public final Vector2 acceleration;

    public PhysicsObject(Vector2 position, Vector2 velocity, Vector2 acceleration, int networkID) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.networkID = networkID;
    }

    public PhysicsObject(float x, float y, float xVel, float yVel, float xAccel, float yAccel, int networkID) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(xVel, yVel);
        this.acceleration = new Vector2(xAccel, yAccel);
        this.networkID = networkID;
    }

    public void updatePhysics(float dt) {
        velocity.mulAdd(acceleration, dt); // integrate acceleration
        position.mulAdd(velocity, dt);     // integrate velocity
//        System.out.println("Pos: " + position.toString());
    }

    @Override
    public void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer) {
        updatePacketBuffer.add(new UpdatePhysicsObject(getNetworkID(), position, velocity, acceleration));
    }

    public void updateFromPacket(UpdatePhysicsObject packet) {
        position.x = packet.x;
        position.y = packet.y;
        velocity.x = packet.x;
        velocity.y = packet.y;
        acceleration.x = packet.x;
        acceleration.y = packet.y;
    }
}
