package sophomoreproject.game.systems.mapstuff;

import sophomoreproject.game.systems.mapstuff.Octave;
import sophomoreproject.game.systems.mapstuff.OctaveSet;

import java.util.ArrayList;
import java.util.Random;

public class TwistedOctaveSet extends OctaveSet {
    private ArrayList<Octave> octaveXShift = new ArrayList<>();
    private ArrayList<Octave> octaveYShift = new ArrayList<>();
    private float twistMagnitude = 0f;
    private final float twistAmount;

    public TwistedOctaveSet(Random ran, float twistAmount) {
        super(ran);
        this.twistAmount = twistAmount;
    }

    public void addTwisterOctave(double spacialScalar, double magnitudeScalar) {
        octaveXShift.add(new Octave(ran.nextLong(), spacialScalar, magnitudeScalar));
        octaveYShift.add(new Octave(ran.nextLong(), spacialScalar, magnitudeScalar));
        twistMagnitude += magnitudeScalar;
    }

    public void addTwisterOctaveFractal(double spacialScalar, double magnitudeScalar,
                                        double spacialCompound, double magnitudeCompound,
                                        int octaves) {
        for (int i = 0; i < octaves; ++i) {
            addTwisterOctave(spacialScalar * Math.pow(spacialCompound, i),
                    magnitudeScalar * Math.pow(magnitudeCompound, i));
        }
    }

    @Override
    public double getValue(float x, float y) {
        float xTwisted = 0;
        for (Octave o : octaveXShift) {
            xTwisted += o.getValue(x, y);
        }
        xTwisted /= twistMagnitude;
        xTwisted *= twistAmount;

        float yTwisted = 0;
        for (Octave o : octaveYShift) {
            yTwisted += o.getValue(x, y);
        }
        yTwisted /= twistMagnitude;
        yTwisted *= twistAmount;
        return super.getValue(x + xTwisted, y + yTwisted);
    }
}
