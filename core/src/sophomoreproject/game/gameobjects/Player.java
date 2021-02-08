package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.singletons.CustomAssetManager;

public class Player extends PhysicsObject implements Renderable{

    private static final TextureAtlas textAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/stuff.pack");
    private static final TextureRegion playerTex = textAtl.findRegion("fronttroll");

    public Player(Vector2 position) {
        super(position, new Vector2(0,0), new Vector2(0,0));
    }

    public void run(float dt) {
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(playerTex, position.x, position.y);
    }
}
