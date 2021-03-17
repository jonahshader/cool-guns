package sophomoreproject.game.gameobjects.gunstuff;

public class AttackInfo {
    public int damage;
    public int shieldDamage;
    public int armorDamage;
    public float xKnockback;
    public float yKnockback;

    public AttackInfo(int damage, int shieldDamage, int armorDamage, float xKnockback, float yKnockback) {
        this.damage = damage;
        this.shieldDamage = shieldDamage;
        this.armorDamage = armorDamage;
        this.xKnockback = xKnockback;
        this.yKnockback = yKnockback;
    }

    public AttackInfo() { }
}
