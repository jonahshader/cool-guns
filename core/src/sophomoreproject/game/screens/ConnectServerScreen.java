package sophomoreproject.game.screens;

import com.badlogic.gdx.Screen;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.javafx.LoginWindow;
import sophomoreproject.game.networking.ClientNetwork;


public class ConnectServerScreen implements Screen {
    ClientNetwork network;
    CoolGuns game;

    public ConnectServerScreen(CoolGuns game) {
        this.game = game;
        network = ClientNetwork.getInstance();
        LoginWindow.launch(LoginWindow.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
