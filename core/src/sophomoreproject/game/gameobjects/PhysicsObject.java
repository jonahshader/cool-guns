package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.GameObject;

abstract public class PhysicsObject extends GameObject {
    public final Vector2 position;
    public final Vector2 velocity;
    public final Vector2 acceleration;

    public PhysicsObject(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public void updatePhysics(float dt) {
        velocity.mulAdd(acceleration, dt); // integrate acceleration
        position.mulAdd(velocity, dt);     // integrate velocity
    }
}
