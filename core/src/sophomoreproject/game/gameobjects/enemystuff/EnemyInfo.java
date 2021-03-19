package sophomoreproject.game.gameobjects.enemystuff;

import sophomoreproject.game.singletons.LocalRandom;

public class EnemyInfo {
    public float approachAngle = 0;
    public float maxIdleVelocity = 5;
    public float maxAcceleration = 80;
    public float maxActiveVelocity = 35;

    public float approachRadius = 80;
    public float attackRadius = 20;
    public float size = 1;
    public float attackDamage = 1;
    public float attackDelay = 1.5f;
    public int health = 1;

    public EnemyInfo() {}

    public EnemyInfo(float difficulty) {
        float sqrtDiff = (float) Math.sqrt(difficulty);
        float cbrtDiff = (float) Math.cbrt(difficulty);
        approachAngle = (float) (cbrtDiff * LocalRandom.RAND.nextGaussian() * 0.3);
        if (approachAngle > Math.PI / 2) approachAngle = (float) (Math.PI / 2);
        if (approachAngle < -Math.PI / 2) approachAngle = (float) (-Math.PI / 2);
        maxIdleVelocity *= 1 + sqrtDiff * .2;
        maxAcceleration *= 1 + sqrtDiff * 0.3;
        maxActiveVelocity *= 1 + sqrtDiff * .15;
        if (maxIdleVelocity > maxActiveVelocity) maxIdleVelocity = maxActiveVelocity;

        approachRadius *= 1 + Math.pow(difficulty * .5, 1/3f);
        attackRadius *= 1 + Math.pow(difficulty * .7, 1/3.5f);
        size *= .5 + (sqrtDiff * 0.11);
        attackDamage *= 1 + sqrtDiff * 0.5;
        attackDelay /= 1 + cbrtDiff * 0.5;
        health *= 1 + (Math.pow(difficulty * .33, .7));
    }
}
