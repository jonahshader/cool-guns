package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.networking.ClientNetwork;

public class ConnectServerScreen implements Screen {
    ClientNetwork network;
    CoolGuns game;

    public ConnectServerScreen(CoolGuns game) {
        this.game = game;
        network = ClientNetwork.getInstance();
        network.tryConnect();

        game.setScreen(new LoginScreen());
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
