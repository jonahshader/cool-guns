package sophomoreproject.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sophomoreproject.game.screens.ConnectServerScreen;
import sophomoreproject.game.screens.LoginScreen;
import sophomoreproject.game.screens.MainMenuScreen;
import sophomoreproject.game.screens.TempBypassScreen;
import sophomoreproject.game.singletons.CustomAssetManager;

public class CoolGuns extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	private boolean useLocalHost;

    public CoolGuns(boolean useLocalHost) {
        this.useLocalHost = useLocalHost;
    }

    @Override
	public void create () {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		CustomAssetManager.getInstance().loadImages();
		CustomAssetManager.getInstance().loadFonts();
		CustomAssetManager.getInstance().loadSounds();
		CustomAssetManager.getInstance().manager.finishLoading();
        // change screen to a new instance of LoginScreen
        setScreen(new ConnectServerScreen(this));
//		setScreen(new MainMenuScreen(this, 0));
	}

	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}
}
