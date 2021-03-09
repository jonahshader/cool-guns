package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;

public class CreateInventoryGun {
    public GunInfo info;
    public int ownerNetId;
    public int netId;


    public CreateInventoryGun(Gun toSend) {
        this.info = toSend.getInfo();
        this.ownerNetId = toSend.getOwnerNetId();
        this.netId = toSend.getNetworkID();
    }

    public CreateInventoryGun(GunInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.netId = netId;
    }

    public CreateInventoryGun() { } //For KryoNet
}

