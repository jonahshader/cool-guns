package sophomoreproject.game.packets;


import sophomoreproject.game.gameobjects.Bullet;
import sophomoreproject.game.gameobjects.Player;

public class CreateBullet {
    public UpdatePhysicsObject u;
    public int creatorNetId;
    public float bulletSize;

    public CreateBullet(Bullet toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        creatorNetId = toSend.getCreatorNetId();
        bulletSize = toSend.getBulletSize();
    }

    //Client request constructor
    public CreateBullet(UpdatePhysicsObject u, int creatorNetId, float bulletSize) {
        this.u = u;
        this.creatorNetId = creatorNetId;
        this.bulletSize = bulletSize;
    }

    public CreateBullet() {

    }
}
