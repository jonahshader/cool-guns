package sophomoreproject.game.gameobjects.enemystuff;

import sophomoreproject.game.singletons.LocalRandom;

public class EnemyInfo {
    public float approachAngle = 0;
    public float maxIdleVelocity = 5;
    public float maxAcceleration = 200;
    public float maxActiveVelocity = 50;

    public float approachRadius = 180;
    public float attackRadius = 50;
    public float size = 1;
    public float attackDamage = 1;
    public float attackDelay = 1.5f;
    public int health = 5;

    public EnemyInfo() {}

    public EnemyInfo(float difficulty) {
        float sqrtDiff = (float) Math.sqrt(difficulty);
        float cbrtDiff = (float) Math.cbrt(difficulty);
        approachAngle = (float) (cbrtDiff * LocalRandom.RAND.nextGaussian() * 0.3);
        maxIdleVelocity *= sqrtDiff;
        maxAcceleration *= sqrtDiff * 0.75;
        maxActiveVelocity *= cbrtDiff;

        approachRadius *= Math.pow(difficulty, 1/3f);
        attackRadius *= Math.pow(difficulty, 1/3.5f);
        size *= .5 + (cbrtDiff * 0.5);
        attackDamage *= 1 + cbrtDiff * 0.5;
        attackDelay /= 1 + cbrtDiff * 0.5;
        health *= cbrtDiff;
    }
}
