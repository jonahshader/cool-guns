package sophomoreproject.game.systems.mapstuff;

import java.util.ArrayList;
import java.util.Random;

public class OctaveSet {
    private ArrayList<Octave> octaves;
    private double totalMagnitude = 0.0;
    private Random ran;

    public OctaveSet(Random ran) {
        octaves = new ArrayList<>();
        this.ran = ran;
    }

    public void addOctave(double spacialScalar, double magnitudeScalar) {
        octaves.add(new Octave(ran.nextLong(), spacialScalar, magnitudeScalar));
        totalMagnitude += magnitudeScalar;
    }

    public void addOctaveFractal(double spacialScalar, double magnitudeScalar,
                                 double spacialCompound, double magnitudeCompound,
                                 int octaves) {
        for (int i = 0; i < octaves; ++i) {
            addOctave(spacialScalar * Math.pow(spacialCompound, i),
                    magnitudeScalar * Math.pow(magnitudeCompound, i));
        }
    }

    public double getValue(int x, int y) {
        double val = 0;
        for (Octave octave : octaves) {
            val += octave.getValue(x, y);
        }

        return val / totalMagnitude;
    }
}
