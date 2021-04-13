package sophomoreproject.game.gameobjects.shieldstuff;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.systems.PlayerController;

import static java.lang.Math.sqrt;
import static sophomoreproject.game.singletons.LocalRandom.expGaussian;

public class ShieldInfo {
    public float capacity = 5f;
    public float regenRate = capacity * .15f;
    public float regenDelay = 7f;
    public float health = capacity * .1f;

    public float r = 1;
    public float g = 1;
    public float b = 1;

    public ShieldInfo() {}

    public void randomize(float randomness) {
        capacity *= expGaussian(2f, randomness);
        regenRate *= expGaussian(2f, randomness);
        regenDelay *= expGaussian(2f, randomness);
        health *= expGaussian(2f, randomness);
    }

    public void scaleScore(float scalar) {
        capacity *= Math.sqrt(scalar);
        regenRate *= Math.sqrt(scalar);
        regenDelay /= Math.sqrt(scalar);
        health *= Math.sqrt(scalar);
    }

    //This might be a bit messed up
    public float getGeneralScore() {
        return (float) sqrt(Math.pow(capacity,2) + Math.pow(regenRate,2)+ Math.pow(regenDelay,2)+ Math.pow(health,2));
    }

    public String getTextureName() {
        return "shield";
    }

}
