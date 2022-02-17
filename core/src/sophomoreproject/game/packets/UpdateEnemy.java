package sophomoreproject.game.packets;

public class UpdateEnemy {
    public int netID;
    public int health;
    public boolean playIdleSound = false;
    public boolean playDropSound = false;

    public UpdateEnemy(int netID, int health, boolean playIdleSound, boolean playDropSound) {
        this.netID = netID;
        this.health = health;
        this.playIdleSound = playIdleSound;
        this.playDropSound = playDropSound;
    }

    public UpdateEnemy() {
    }
}
