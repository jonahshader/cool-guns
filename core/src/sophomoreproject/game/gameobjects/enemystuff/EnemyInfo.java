package sophomoreproject.game.gameobjects.enemystuff;

import sophomoreproject.game.singletons.LocalRandom;

public class EnemyInfo {
    public float difficulty;
    public float approachAngle = 0;
    public float maxIdleVelocity = 5;
    public float maxAcceleration = 100;
    public float maxActiveVelocity = 45;

    public float approachRadius = 70;
    public float attackRadius = 30;
    public float size = .75f;
    public float attackDamage = 1;
    public float attackDelay = 1.5f;
    public int health = 1;
    public float knockback = 30;

    public EnemyInfo() {}

    public EnemyInfo(float difficulty) {
        this.difficulty = difficulty;
        difficulty *= LocalRandom.expGaussian(2, 0.13f);
        float sqrtDiff = (float) Math.sqrt(difficulty);
        float cbrtDiff = (float) Math.cbrt(difficulty);
        approachAngle = (float) ((1 + cbrtDiff) * LocalRandom.RAND.nextGaussian() * 0.2);
        if (approachAngle > Math.PI / 2) approachAngle = (float) (Math.PI / 2);
        if (approachAngle < -Math.PI / 2) approachAngle = (float) (-Math.PI / 2);
        maxIdleVelocity *= 1 + sqrtDiff * .2;
        maxAcceleration *= 1 + sqrtDiff * 0.3;
        maxActiveVelocity *= 1 + sqrtDiff * .15;
        if (maxIdleVelocity > maxActiveVelocity) maxIdleVelocity = maxActiveVelocity;

        approachRadius *= 1 + cbrtDiff * .3;
        attackRadius *= 1 + cbrtDiff * .3;
        attackDamage *= 1 + sqrtDiff * 0.1;
        attackDelay /= 1 + cbrtDiff * 0.5;
        health *= 1 + (Math.pow(difficulty * .15, 1.0));
        knockback *= sqrtDiff;
//        size *= Math.sqrt(health * 5);

        randomize(0.035f);
    }

    private void randomize(float ran) {
        maxIdleVelocity *= LocalRandom.expGaussian(2, ran);
        maxAcceleration *= LocalRandom.expGaussian(2, ran);
        maxActiveVelocity *= LocalRandom.expGaussian(2, ran);
        approachRadius *= LocalRandom.expGaussian(2, ran);
        attackRadius *= LocalRandom.expGaussian(2, ran);
        attackDamage *= LocalRandom.expGaussian(2, ran);
        attackDelay *= LocalRandom.expGaussian(2, ran);
        health *= LocalRandom.expGaussian(2, ran);
        knockback *= LocalRandom.expGaussian(2, ran);


        // recalculate size based on health
        size = (float)Math.pow(health, 1/2.5) * .75f; // somewhere between sqrt and cbrt because in the real world its volume would correspond to health, but cbrt seems too strong
    }
}
