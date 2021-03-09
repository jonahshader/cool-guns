package sophomoreproject.game.singletons;

import java.util.Random;

public class LocalRandom {
    public final static Random RAND = new Random();

    public static float randomPosNeg() {
        return RAND.nextFloat() * (RAND.nextFloat() > 0.5 ? 1 : -1);
    }

    /**
     * @return random number from -1 to 1 in a triangle distribution
     */
    public static float genTriangleDist() {
        float val = RAND.nextFloat();
        val *= val;
        return (RAND.nextFloat() > 0.5 ? val : -val);
    }

    public static float absGaussian() {
        return (float) Math.abs(RAND.nextGaussian());
    }

    public static float expGaussian(float base, float scalar) {
        return (float) Math.pow(base, RAND.nextGaussian()*scalar);
    }
}
