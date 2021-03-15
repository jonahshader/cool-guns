package sophomoreproject.game.gameobjects.enemystuff;

import sophomoreproject.game.singletons.LocalRandom;

public class EnemyInfo {
    public float approachAngle = 0;
    public float maxIdleVelocity = 5;
    public float maxAcceleration = 200;
    public float maxActiveVelocity = 50;

    public float approachRadius = 150;
    public float attackRadius = 75;
    public float size = 1;
    public float attackDamage = 1;
    public float attackDelay = 1.5f;
    public float health = 5;

    public EnemyInfo() {}

    public EnemyInfo(float difficulty) {
        float sqrtDiff = (float) Math.sqrt(difficulty);
        float cbrtDiff = (float) Math.cbrt(difficulty);
        approachAngle = (float) (cbrtDiff * LocalRandom.RAND.nextGaussian() * 0.3);
        maxIdleVelocity *= cbrtDiff;
        maxAcceleration *= sqrtDiff;
        maxActiveVelocity *= cbrtDiff;

        approachRadius *= cbrtDiff;
        size *= cbrtDiff;
        attackDamage *= cbrtDiff;
        attackDelay /= cbrtDiff;
        health *= cbrtDiff;
    }
}
