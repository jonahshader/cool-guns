package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.enemystuff.Enemy;
import sophomoreproject.game.gameobjects.enemystuff.EnemyInfo;

public class CreateEnemy {
    public UpdatePhysicsObject u;
    public EnemyInfo info;

    public CreateEnemy(Enemy toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        this.info = toSend.getInfo();
    }

    public CreateEnemy() {}

}
