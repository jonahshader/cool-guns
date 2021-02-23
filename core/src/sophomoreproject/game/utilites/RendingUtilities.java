package sophomoreproject.game.utilites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class RendingUtilities {
    public static void renderCharacter(Vector2 pos, Vector2 vel, Vector2 size, SpriteBatch sb, TextureRegion[] textures) {
        if (vel.x == 0 && vel.y == 0) {
            sb.draw(textures[6], pos.x - size.x/2,pos.y - size.y/2, size.x, size.y);
        } else {
            float angle = vel.angleRad() + (float) Math.PI/8;
            if (angle >= Math.PI*2) {
                angle = angle - (float) Math.PI*2;
            }
            int index = (int) (angle/((float)Math.PI/4));
            sb.draw(textures[index], pos.x - size.x/2,pos.y - size.y/2, size.x, size.y);

        }







        /*
        if (Math.abs(correctionScalar*vel.x) > Math.abs(vel.y)) {
            if (vel.x > 0) {
                sb.draw(right, pos.x - size.x/2,pos.y - size.y/2, size.x, size.y);
            } else {
                sb.draw(right, pos.x + size.x/2,pos.y - size.y/2, - size.x, size.y);
            }
        } else {
            if (vel.y > 0) {
                sb.draw(back, pos.x - size.x/2,pos.y - size.y/2, size.x, size.y);
            } else {
                sb.draw(front, pos.x - size.x/2,pos.y - size.y/2, size.x, size.y);
            }
        }
*/
    }
}
