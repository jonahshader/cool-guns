package sophomoreproject.game.packets;


import sophomoreproject.game.gameobjects.Bullet;
import sophomoreproject.game.gameobjects.Player;

public class CreateBullet {
    public UpdatePhysicsObject bullet;
    public int accountId;

    public CreateBullet(Bullet toSend) {
        bullet = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
//        accountId = toSend.getAccountId();
    }
}
