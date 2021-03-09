package sophomoreproject.game.utilites;

public class MathUtilities {
    public static int wrap(int val, int min, int max) {
        int out = (val - min) % (max - min);
        if (out < 0) out += (max - min);
        out += min;
        return out;
    }
}
