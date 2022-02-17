package sophomoreproject.game.gameobjects.bootstuff;

import static java.lang.Math.sqrt;
import static sophomoreproject.game.singletons.LocalRandom.expGaussian;
import static sophomoreproject.game.systems.PlayerController.BASE_PLAYER_ACCELERATION;
import static sophomoreproject.game.systems.PlayerController.BASE_PLAYER_WALK_SPEED;

public class BootsInfo {
    public float accelerationToWalkRatio = BASE_PLAYER_ACCELERATION / BASE_PLAYER_WALK_SPEED;
    public float speed = 13f;
    public float acceleration = speed * (accelerationToWalkRatio);

    public float r = 1;
    public float g = 1;
    public float b = 1;

    public BootsInfo() {}

    public void randomize(float randomness) {
        speed *= expGaussian(2f, randomness);
        acceleration *= expGaussian(2f, randomness);

    }

    public void scaleScore(float scalar) {
        scalar = (float)Math.pow(scalar * .2, .8);
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
