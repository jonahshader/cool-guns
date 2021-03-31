package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.gunstuff.AttackInfo;

public class AttackPlayer {
    public AttackInfo info;
    public int playerId;
    public int attackerId;

    public AttackPlayer(AttackInfo info, int playerId, int attackerId) {
        this.info = info;
        this.playerId = playerId;
        this.attackerId = attackerId;
    }

    public AttackPlayer() { }
}
