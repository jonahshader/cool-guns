package sophomoreproject.game.utilites;

import com.badlogic.gdx.math.Vector2;

public class MathUtilities {
    public static int wrap(int val, int min, int max) {
        int out = (val - min) % (max - min);
        if (out < 0) out += (max - min);
        out += min;
        return out;
    }

    public static boolean circleCollisionDetection(Vector2 pos1, float r1, Vector2 pos2, float r2) {
        return circleCollisionDetection(pos1.x, pos1.y, r1, pos2.x, pos2.y, r2);
    }

    public static boolean circleCollisionDetection(float x1, float y1, float r1, float x2, float y2, float r2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= (r1 + r2) * (r1 + r2);
    }

    // TODO: test lol
    public static void circleCollisionMoveDist(float x1, float y1, float r1, float x2, float y2, float r2, Vector2 output) {
        float scaler = (float) ((r1 + r2) / Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
        output.set((x2 - x1) * scaler, (y2 - y1) * scaler);
    }
}
