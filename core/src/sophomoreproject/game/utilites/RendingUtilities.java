package sophomoreproject.game.utilites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class RendingUtilities {
    public static void renderCharacter(Vector2 pos, Vector2 vel, Vector2 size, SpriteBatch sb, TextureRegion[] textures) {
        int index = 6;
        if (vel.x != 0 || vel.y != 0) {
            float angle = vel.angleRad();

            if (angle < 0)
                angle += Math.PI*2;

            angle += Math.PI/8;

            if (angle >= Math.PI*2) {
                angle = angle - (float) Math.PI*2;
            }
            index = (int) (angle/((float)Math.PI/4));
        }
        TextureRegion t = textures[index];
        float width = size.x * t.getRegionWidth();
        float height = size.y * t.getRegionHeight();
        sb.draw(t, pos.x - width/2,pos.y - height/2, width, height);
    }


}
