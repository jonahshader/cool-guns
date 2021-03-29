package sophomoreproject.game.packets;

public class UpdateEnemy {
    public int netID;
    public int health;
    public boolean playIdleSound = false;

    public UpdateEnemy(int netID, int health, boolean playIdleSound) {
        this.netID = netID;
        this.health = health;
        this.playIdleSound = playIdleSound;
    }

    public UpdateEnemy() {
    }
}
