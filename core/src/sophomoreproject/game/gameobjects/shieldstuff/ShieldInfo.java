package sophomoreproject.game.gameobjects.shieldstuff;

import sophomoreproject.game.systems.PlayerController;

import static java.lang.Math.sqrt;
import static sophomoreproject.game.singletons.LocalRandom.expGaussian;

public class ShieldInfo {
    public float capacity;
    public float regenRate;
    public float regenDelay;
    public float health;

    public float r = 1;
    public float g = 1;
    public float b = 1;

    public ShieldInfo() {}

    public void randomize(float randomness) {
        capacity *= expGaussian(2f, randomness);
        acceleration *= expGaussian(2f, randomness);

    }

    public void scaleScore(float scalar) {
        speed *= Math.sqrt(scalar);
        acceleration *= Math.sqrt(scalar);
    }

    public float getGeneralScore() {
        return (float) sqrt(Math.pow(speed,2) + Math.pow(((1/accelerationToWalkRatio)*acceleration),2));
    }

    public String getTextureName() {
        return "speed_shoes";
    }

}
