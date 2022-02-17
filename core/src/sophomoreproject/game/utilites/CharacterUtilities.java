package sophomoreproject.game.utilites;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.PhysicsObject;

public class CharacterUtilities {
    public static void accelerateTowardsTargetVelocity(Vector2 targetVelocity, float acceleration, PhysicsObject obj, float dt) {
        obj.acceleration.set(0, 0);
        boolean moving = targetVelocity.x != 0 || targetVelocity.y != 0;

        Vector2 velocityDifference = new Vector2(targetVelocity);
        velocityDifference.sub(obj.velocity);
        velocityDifference.nor().scl(acceleration);

        if (!moving && velocityDifference.len() * dt > obj.velocity.len()) {
            obj.acceleration.set(0, 0);
            obj.velocity.set(0, 0);
        } else {
            obj.acceleration.set(velocityDifference);
        }
    }
}
