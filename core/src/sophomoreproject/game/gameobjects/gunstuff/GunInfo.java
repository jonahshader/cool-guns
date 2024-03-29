package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.audio.Sound;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.LocalRandom;

import static sophomoreproject.game.singletons.LocalRandom.expGaussian;


public class GunInfo {
    public float fireDelay = .3f;
    public float burstDelay = .5f;
    public float reloadDelay = 1f;
    public float spread = .1f;
    public Gun.FiringMode firingMode = Gun.FiringMode.SEMI_AUTO;
    public int clipSize = 10;
    public float bulletSpeed = 100f;
    public float bulletSpeedVariation = .175f;
    public int bulletsPerShot = 1;
    public int shotsPerBurst = 3;
    public float bulletSize = 6f;
    public Bullet.BulletType bulletType = Bullet.BulletType.STANDARD;
    public float bulletDamage = 5.25f;
    public float bulletDamageVariance = 1.5f;
    public float critScalar = 1.5f;
    public float playerKnockback = 25f;
    public float enemyKnockback = 50f;
    public Gun.GunType gunType = Gun.GunType.PISTOL;
    public float gunHoldRadius = 18f;
    public int shieldDamage = 0;
    public int armorDamage = 0;
    public float r = 1;
    public float g = 1;
    public float b = 1;

    public GunInfo() {}

    public void loadGunTypeDefaults(Gun.GunType type, boolean randomizeFiringMode) {
        gunType = type;
        switch (type) {
            case PISTOL:
                firingMode = (randomizeFiringMode && LocalRandom.RAND.nextFloat() < (1/2f)) ? Gun.FiringMode.BURST : Gun.FiringMode.SEMI_AUTO;
                if (firingMode == Gun.FiringMode.SEMI_AUTO) {
                    fireDelay = .1f;
                }
                break;
            case RIFLE:
                firingMode = Gun.FiringMode.SEMI_AUTO;
                bulletSize = 5;
                clipSize = 5;
                fireDelay = 1.0f;
                reloadDelay = 3f;
                bulletSpeed = 400;
                spread = 0.02f;
                bulletDamage = 20;
                critScalar = 3;
                break;
            case SMG:
//                firingMode = (randomizeFiringMode && LocalRandom.RAND.nextFloat() < (1/3f)) ? Gun.FiringMode.BURST : Gun.FiringMode.AUTO;
                firingMode = Gun.FiringMode.AUTO;
                bulletDamage = 1.25f;
                bulletSpeed = 85;
                fireDelay = 6f/60;
                clipSize = 20;
                bulletSize = 5f;
                reloadDelay = 1.3f;
                enemyKnockback = 30;
                break;
            case LMG:
                firingMode = Gun.FiringMode.AUTO;
                bulletDamage = .8f;
                bulletDamageVariance = .5f;
                bulletsPerShot = 3;
                enemyKnockback = 30;
                playerKnockback = 20;
                bulletSpeed = 90;
                spread = .15f;
                fireDelay = 6f/60;
                clipSize = 100;
                bulletSize = 4;
                reloadDelay = 3;
                break;
            case SHOTGUN:
                firingMode = Gun.FiringMode.SEMI_AUTO;
                bulletDamage = 1.2f;
                bulletsPerShot = 12;
                clipSize = 36;
                bulletSize = 2.5f;
                fireDelay = .2f;
                playerKnockback = 20;
                break;
            default:
                break;
        }
    }

    public void loadStarterGun() {
        loadGunTypeDefaults(Gun.GunType.PISTOL, false);
        bulletDamage = 1;
        bulletDamageVariance = 0;
    }

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
        bulletDamageVariance *= expGaussian(2f, randomness);
        critScalar *= expGaussian(2f, randomness);
        playerKnockback *= expGaussian(2f, randomness);
        enemyKnockback *= expGaussian(2f, randomness);
//        gunHoldRadius *= expGaussian(2f, randomness);
        shieldDamage *= expGaussian(2f, randomness);
        armorDamage *= expGaussian(2f, randomness);

        clipSize = Math.max(1, clipSize);
        bulletsPerShot = Math.max(1, bulletsPerShot);
        shotsPerBurst = Math.max(1, shotsPerBurst);
    }

    public void scaleScore(float scalar) {
        scalar = (float)Math.pow(scalar * .15, .6);
        fireDelay /= Math.pow(scalar, 1/3.1);
        burstDelay /= Math.sqrt(scalar);
        reloadDelay /= Math.sqrt(scalar);
        spread /= Math.cbrt(scalar);
        clipSize *= Math.sqrt(scalar);
        bulletSpeed *= Math.sqrt(scalar);
        bulletSpeedVariation /= Math.sqrt(scalar);
        bulletsPerShot *= Math.sqrt(scalar);
        shotsPerBurst *= Math.sqrt(scalar);
        bulletSize *= Math.cbrt(scalar);
        bulletDamage *= Math.pow(scalar, 1/2.5);
        bulletDamageVariance /= Math.cbrt(scalar);
        critScalar *= Math.cbrt(scalar);
        playerKnockback /= Math.cbrt(scalar);
        enemyKnockback *= Math.sqrt(scalar);
        shieldDamage *= Math.sqrt(scalar);
        armorDamage *= Math.sqrt(scalar);

        clipSize = Math.max(1, clipSize);
        bulletsPerShot = Math.max(1, bulletsPerShot);
        shotsPerBurst = Math.max(1, shotsPerBurst);
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

    public float getMagnitude() {
        float sumOfSquares = 0;
        sumOfSquares += Math.pow(this.fireDelay, 2);
        sumOfSquares += Math.pow(this.burstDelay, 2);
        sumOfSquares += Math.pow(this.reloadDelay, 2);
        sumOfSquares += Math.pow(this.spread, 2);
        sumOfSquares += Math.pow(this.clipSize, 2);
        sumOfSquares += Math.pow(this.bulletSpeed, 2);
        sumOfSquares += Math.pow(this.bulletSpeedVariation, 2);
        sumOfSquares += Math.pow(this.bulletsPerShot, 2);
        sumOfSquares += Math.pow(this.shotsPerBurst, 2);
        sumOfSquares += Math.pow(this.bulletSize, 2);
        sumOfSquares += Math.pow(this.bulletDamage, 2);
        sumOfSquares += Math.pow(this.bulletDamageVariance, 2);
        sumOfSquares += Math.pow(this.critScalar, 2);
        sumOfSquares += Math.pow(this.playerKnockback, 2);
        sumOfSquares += Math.pow(this.enemyKnockback, 2);
        sumOfSquares += Math.pow(this.gunHoldRadius, 2);
        sumOfSquares += Math.pow(this.shieldDamage, 2);
        sumOfSquares += Math.pow(this.armorDamage, 2);

        return (float) Math.sqrt(sumOfSquares);
    }

    private int getFiresPerClip() {
        return (int)Math.ceil(clipSize / (double)bulletsPerShot);
    }

    private float getEmptyClipTime() {
        if (firingMode == Gun.FiringMode.BURST) {
            int bursts = (int)Math.ceil(clipSize / ((double)shotsPerBurst * bulletsPerShot));
            return (getFiresPerClip() * fireDelay) + reloadDelay + bursts * burstDelay;
        } else {
            return (getFiresPerClip() * fireDelay) + reloadDelay;
        }
    }

    private float getPlayerKnockbackAcceleration() {
        return clipSize * playerKnockback / getEmptyClipTime();
    }

    private float getPlayerKnockbackScoreMultiplier() {
        return 1 / (0.8f + getPlayerKnockbackAcceleration() * 0.1f);
    }

    private float getEnemyKnockbackAcceleration() {
        return clipSize * enemyKnockback / getEmptyClipTime();
    }

    private float getEnemyKnockbackScoreMultiplier() {
        return (float)Math.sqrt(1 + getEnemyKnockbackAcceleration() * 0.1f);
    }

    public float getDps() {
        return clipSize * bulletDamage / getEmptyClipTime();
    }

    public float getSpreadScoreMultiplier() {
        return (float) Math.pow(2, -spread);
    }

    public float getBulletSizeScoreMultiplier() {
        return (float) (1 + Math.sqrt(bulletSize));
    }

    public float getBulletSpeedScoreMultiplier() {
        return (float) Math.sqrt(bulletSpeed);
    }

    public float getGeneralScore() {
        float score = getDps();
        score *= getSpreadScoreMultiplier();
        score *= getBulletSizeScoreMultiplier();
        score *= getBulletSpeedScoreMultiplier();
        score *= getPlayerKnockbackScoreMultiplier();
        score *= getEnemyKnockbackScoreMultiplier();
        return score * 0.01f;
    }

    public void normalize() {
        float inverseMag = 1/getMagnitude();
        fireDelay *= inverseMag;
        burstDelay *= inverseMag;
        reloadDelay *= inverseMag;
        spread *= inverseMag;
        clipSize *= inverseMag;
        bulletSpeed *= inverseMag;
        bulletSpeedVariation *= inverseMag;
        bulletsPerShot *= inverseMag;
        shotsPerBurst *= inverseMag;
        bulletSize *= inverseMag;
        bulletDamage *= inverseMag;
        bulletDamageVariance *= inverseMag;
        critScalar *= inverseMag;
        playerKnockback *= inverseMag;
        enemyKnockback *= inverseMag;
        gunHoldRadius *= inverseMag;
        shieldDamage *= inverseMag;
        armorDamage *= inverseMag;
    }

    public String getTextureName() {
        switch (gunType) {
            case SHOTGUN:
                return "shotgun";
            case RIFLE:
                return "91";
            case LMG:
                return "lmg";
            case SMG:
                return "smg";
            default:
                return "default_gun";
        }
    }

    public Sound getSound() {
        switch (gunType) {
            case SHOTGUN:
                return CustomAssetManager.getInstance().manager.get(CustomAssetManager.GUN_FIRE_SHOTGUN, Sound.class);
            default:
                return CustomAssetManager.getInstance().manager.get(CustomAssetManager.GUN_FIRE_NORMAL, Sound.class);
        }
    }
}
