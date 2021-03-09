package sophomoreproject.game.packets;


import sophomoreproject.game.gameobjects.gunstuff.Bullet;

public class CreateBullet {
    public UpdatePhysicsObject u;
    public int creatorNetId;
    public float bulletSize;
    public float damage;
    public int shieldDamage;
    public int armorDamage;
    public float critScalar;
    public float enemyKnockback;

    public CreateBullet(Bullet toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        creatorNetId = toSend.getCreatorNetId();
        bulletSize = toSend.getBulletSize();
    }

    //Client request constructor
    public CreateBullet(UpdatePhysicsObject u, int creatorNetId, float bulletSize, float
            damage, int shieldDamage, int armorDamage, float critScalar, float enemyKnockback) {
        this.u = u;
        this.creatorNetId = creatorNetId;
        this.bulletSize = bulletSize;
        this.damage = damage;
        this.shieldDamage = shieldDamage;
        this.armorDamage = armorDamage;
        this.critScalar = critScalar;
        this.enemyKnockback = enemyKnockback;
    }

    public CreateBullet() {

    }
}
