package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable {
    void draw(float dt, SpriteBatch sb, ShapeRenderer sr);
}
