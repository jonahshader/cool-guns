package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.bootstuff.Boots;
import sophomoreproject.game.gameobjects.bootstuff.BootsInfo;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;

public class CreateBoots {
    public BootsInfo info;
    public int ownerNetId;
    public int netId;

    public CreateBoots(Boots toSend) {
        this.info = toSend.getInfo();
        this.ownerNetId = toSend.getOwnerNetId();
        this.netId = toSend.getNetworkID();
    }

    public CreateBoots(BootsInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.netId = netId;
    }

    public CreateBoots() {}
}
