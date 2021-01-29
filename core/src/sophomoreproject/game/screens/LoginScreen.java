package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class  LoginScreen implements Screen, Input.TextInputListener {

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
    LoginScreen listener = new LoginScreen();
    @Override
    public void input(String text) {
        Gdx.input.getTextInput(listener, "Dialog Title", "Initial Textfield Value", "Hint Value");

    }

    @Override
    public void canceled() {

    }

}
