package sophomoreproject.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sophomoreproject.game.screens.LoginScreen;

public class CoolGuns extends Game {
	private SpriteBatch batch;
	
	@Override
	public void create () {
	    batch = new SpriteBatch();

        // change screen to a new instance of LoginScreen
        setScreen(new LoginScreen(batch));
	}

	@Override
	public void render () {
	    super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}
}
