package sophomoreproject.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sophomoreproject.game.screens.LoginScreen;

public class CoolGuns extends Game {
    private InputMultiplexer inputMultiplexer;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		inputMultiplexer = new InputMultiplexer();
	    batch = new SpriteBatch();


		// set input processor to the input multiplexer.
        // this allows multiple objects to handle input at the same time
        Gdx.input.setInputProcessor(inputMultiplexer);

        // change screen to a new instance of LoginScreen
        setScreen(new LoginScreen(batch, inputMultiplexer));
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
