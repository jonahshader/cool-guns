package sophomoreproject.game.systems.mapstuff;

import sophomoreproject.game.external.OpenSimplex2F;

public class Octave {
    private OpenSimplex2F noise;
    private double spacialScalar, magnitudeScalar;

    public Octave(long seed, double spacialScalar, double magnitudeScalar) {
        noise = new OpenSimplex2F(seed);

        this.spacialScalar = spacialScalar;
        this.magnitudeScalar = magnitudeScalar;
    }

    public double getValue(int x, int y) {
        return noise.noise2(x * spacialScalar, y * spacialScalar) * magnitudeScalar;
    }
}
