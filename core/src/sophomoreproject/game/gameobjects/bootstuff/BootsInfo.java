package sophomoreproject.game.gameobjects.bootstuff;


import sophomoreproject.game.systems.PlayerController;

import static java.lang.Math.sqrt;
import static sophomoreproject.game.singletons.LocalRandom.expGaussian;

public class BootsInfo {
    public float accelerationToWalkRatio = PlayerController.getInstance().PLAYER_ACCELERATION / PlayerController.getInstance().PLAYER_WALK_SPEED;
    public float speed = 10f;
    public float acceleration = speed * (accelerationToWalkRatio);

    //randomize
    //scale

    public float r = 1;
    public float g = 1;
    public float b = 1;

    public BootsInfo() {}

    public void randomize(float randomness) {
        speed *= expGaussian(2f, randomness);
        acceleration *= expGaussian(2f, randomness);

    }

    public void scaleScore(float scalar) {
        sqrt(Math.pow(speed,2) + Math.pow(((1/accelerationToWalkRatio)*acceleration),2));
    }

    public String getTextureName() {
                return "speed_shoes";
        }

}
