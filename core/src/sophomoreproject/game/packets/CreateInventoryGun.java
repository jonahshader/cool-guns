package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;

public class CreateInventoryGun {
    public GunInfo info;
    public int ownerNetId;


    public CreateInventoryGun(Gun toSend) {
        this.info = toSend.getInfo();
        this.ownerNetId = toSend.getOwnerNetId();
    }

    public CreateInventoryGun(GunInfo info, int ownerNetId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
    }

    public CreateInventoryGun() { } //For KryoNet
}
