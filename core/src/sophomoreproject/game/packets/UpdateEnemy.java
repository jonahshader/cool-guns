package sophomoreproject.game.packets;

public class UpdateEnemy {
    public int netID;
    public int health;

    public UpdateEnemy(int netID, int health) {
        this.netID = netID;
        this.health = health;
    }

    public UpdateEnemy() {
    }
}
