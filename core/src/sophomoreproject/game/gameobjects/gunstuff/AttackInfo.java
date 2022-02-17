package sophomoreproject.game.gameobjects.gunstuff;

public class AttackInfo {
    public int damage;
    public float xKnockback;
    public float yKnockback;

    public AttackInfo(int damage, float xKnockback, float yKnockback) {
        this.damage = damage;
        this.xKnockback = xKnockback;
        this.yKnockback = yKnockback;
    }

    public AttackInfo() { }
}
