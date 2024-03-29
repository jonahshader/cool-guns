package sophomoreproject.game.packets;

public class UpdatePlayer {
    public int netID;
    public float xLook;
    public float yLook;

    public int health;
    public int maxHealth;
    public int shield;
    public int maxShield;
    public float stamina;

    public int totalDamage;
    public int damageSinceDeath;

    public UpdatePlayer(int netID, float xLook, float yLook, int health, int maxHealth, int shield, int maxShield, float stamina, int totalDamage, int damageSinceDeath) {
        this.netID = netID;
        this.xLook = xLook;
        this.yLook = yLook;
        this.health = health;
        this.maxHealth = maxHealth;
        this.shield = shield;
        this.maxShield = maxShield;
        this.stamina = stamina;
        this.totalDamage = totalDamage;
        this.damageSinceDeath = damageSinceDeath;
    }

    public UpdatePlayer() {}
}
