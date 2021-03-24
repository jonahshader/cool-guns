package sophomoreproject.game.interfaces;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.gunstuff.AttackInfo;

// assuming class implementing this is also a GameObject
public interface CollisionReceiver {
    enum CollisionGroup {
        BULLET,
        ENEMY,
        PLAYER
    }
    Vector2 getPosition();
    float getRadius();
    CollisionGroup getCollisionGroup();
    boolean checkCollidingGroup(CollisionGroup otherCollisionGroup);
    int receiveAttack(AttackInfo attack, int attackerNetID);
    int getNetworkID();
}
