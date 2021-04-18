package sophomoreproject.game.packets;
// lets the attacker know how much damage it dealt
public class AttackFeedback {
    public int damage;
    public int attackerId;
    public int attackedId;

    public AttackFeedback(int damage, int attackerId, int attackedId) {
        this.damage = damage;
        this.attackerId = attackerId;
        this.attackedId = attackedId;
    }

    public AttackFeedback() { }
}
