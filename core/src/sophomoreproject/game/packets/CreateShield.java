package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.bootstuff.Boots;
import sophomoreproject.game.gameobjects.bootstuff.BootsInfo;
import sophomoreproject.game.gameobjects.shieldstuff.Shield;
import sophomoreproject.game.gameobjects.shieldstuff.ShieldInfo;

public class CreateShield {
    public ShieldInfo info;
    public int ownerNetId;
    public int netId;

    public CreateShield(Shield toSend) {
        this.info = toSend.getInfo();
        this.ownerNetId = toSend.getOwnerNetId();
        this.netId = toSend.getNetworkID();
    }

    public CreateShield(ShieldInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.netId = netId;
    }

    public CreateShield() {}
}
