package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.javafx.LoginWindow;
import sophomoreproject.game.javafx.ServerConnectWindow;
import sophomoreproject.game.networking.ClientNetwork;


public class ConnectServerScreen implements Screen {
    CoolGuns game;

    public ConnectServerScreen(CoolGuns game) {
        this.game = game;

        game.setScreen(new LoginScreen (game));
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
