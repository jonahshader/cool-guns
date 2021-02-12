package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sophomoreproject.game.javafx.LoginWindow;

/**
 * this is the screen that first pops up when you launch the game.
 * it will ask the user to register or login to an account.
 * if the account already exists and user clicks "register", display "Account already exists!"
 * if the account doesn't exist and user clicks "login", display "Account doesn't exist!"
 * if the user attempts to register an account without a username or a password, display "Invalid username/password!"
 */

public class LoginScreen implements Screen {
    private SpriteBatch batch;

    public LoginScreen(SpriteBatch batch) {
        this.batch = batch;
        LoginWindow.launch();
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
