package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoginScreen implements Screen {
    private SpriteBatch batch;

    public LoginScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // set clear color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // apply clear color to screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
