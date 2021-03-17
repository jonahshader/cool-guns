package sophomoreproject.game.packets;

public class UpdateEnemy {
    public int netId;
    public int health;

    public UpdateEnemy(int netId, int health) {
        this.netId = netId;
        this.health = health;
    }

    public UpdateEnemy() {
    }
}
