package sophomoreproject.game.gameobjects.gunstuff;

import static sophomoreproject.game.singletons.LocalRandom.expGaussian;


public class GunInfo {
    public float fireDelay = .3f;
    public float burstDelay = .5f;
    public float reloadDelay = 1f;
    public float spread = .1f;
    public Gun.FiringMode firingMode = Gun.FiringMode.SEMI_AUTO;
    public int clipSize = 10;
    public float bulletSpeed = 75f;
    public float bulletSpeedVariation = .2f;
    public int bulletsPerShot = 1;
    public int shotsPerBurst = 3;
    public float bulletSize = 4f;
    public Bullet.BulletType bulletType = Bullet.BulletType.STANDARD;
    public float bulletDamage = 2f;
    public float bulletDamageVariance = 1.5f;
    public float critScalar = 1.5f;
    public float playerKnockback = 45f;
    public float enemyKnockback = 15f;
    public Gun.GunType gunType = Gun.GunType.PISTOL;
    public float gunHoldRadius = 30f;
    public int shieldDamage = 0;
    public int armorDamage = 0;

    public GunInfo() {}

    public void randomize(float randomness) {
        fireDelay *= expGaussian(2f, randomness);
        burstDelay *= expGaussian(2f, randomness);
        reloadDelay *= expGaussian(2f, randomness);
        spread *= expGaussian(2f, randomness);
        clipSize *= expGaussian(2f, randomness);
        bulletSpeed *= expGaussian(2f, randomness);
        bulletSpeedVariation *= expGaussian(2f, randomness);
        bulletsPerShot *= expGaussian(2f, randomness);
        shotsPerBurst *= expGaussian(2f, randomness);
        bulletSize *= expGaussian(2f, randomness);
        bulletDamage *= expGaussian(2f, randomness);
        bulletDamageVariance *= expGaussian(2f, randomness);;
        critScalar *= expGaussian(2f, randomness);
        playerKnockback *= expGaussian(2f, randomness);
        enemyKnockback *= expGaussian(2f, randomness);
        gunHoldRadius *= expGaussian(2f, randomness);
        shieldDamage *= expGaussian(2f, randomness);
        armorDamage *= expGaussian(2f, randomness);

        // TODO: select random bullet type, gun type, firing mode
        // they can be determined by the variables
        bulletType = Bullet.BulletType.STANDARD;
        gunType = Gun.GunType.PISTOL;
        firingMode = Gun.FiringMode.SEMI_AUTO;
    }

    // Could compare the sum of squares for combined parameters to the default sum of squares for some gun presets (rifle, pistol, etc.)
    // The gun type will be whatever preset value the new sum of squares is closest to

    public float distanceToGun(GunInfo otherGun) {
        float sumOfSquares = 0;
        sumOfSquares += Math.pow(this.fireDelay - otherGun.fireDelay, 2);
        sumOfSquares += Math.pow(this.burstDelay - otherGun.burstDelay, 2);
        sumOfSquares += Math.pow(this.reloadDelay - otherGun.reloadDelay, 2);
        sumOfSquares += Math.pow(this.spread - otherGun.spread, 2);
        sumOfSquares += Math.pow(this.clipSize - otherGun.clipSize, 2);
        sumOfSquares += Math.pow(this.bulletSpeed - otherGun.bulletSpeed, 2);
        sumOfSquares += Math.pow(this.bulletSpeedVariation - otherGun.bulletSpeedVariation, 2);
        sumOfSquares += Math.pow(this.bulletsPerShot - otherGun.bulletsPerShot, 2);
        sumOfSquares += Math.pow(this.shotsPerBurst - otherGun.shotsPerBurst, 2);
        sumOfSquares += Math.pow(this.bulletSize - otherGun.bulletSize, 2);
        sumOfSquares += Math.pow(this.bulletDamage - otherGun.bulletDamage, 2);
        sumOfSquares += Math.pow(this.bulletDamageVariance - otherGun.bulletDamageVariance, 2);
        sumOfSquares += Math.pow(this.critScalar - otherGun.critScalar, 2);
        sumOfSquares += Math.pow(this.playerKnockback - otherGun.playerKnockback, 2);
        sumOfSquares += Math.pow(this.enemyKnockback - otherGun.enemyKnockback, 2);
        sumOfSquares += Math.pow(this.gunHoldRadius - otherGun.gunHoldRadius, 2);
        sumOfSquares += Math.pow(this.shieldDamage - otherGun.shieldDamage, 2);
        sumOfSquares += Math.pow(this.armorDamage - otherGun.armorDamage, 2);

        return (float) Math.sqrt(sumOfSquares);
    }
}
