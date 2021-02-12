package sophomoreproject.game.menu;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Menu implements InputProcessor {
    private ArrayList<MenuItem> items = new ArrayList<>();
    private BitmapFont font;

    public Menu(BitmapFont font) {
        this.font = font;
    }

    public void addMenuItem(String label, MenuAction action) {
        // TODO: add menu item to items arraylist

        MenuItem newItem = new MenuItem(..., ..., items.get(items.size() - 1));

        if (items.size() == 0) {

        } else {

        }
    }

    public void run(float dt) {
        // TODO:
    }

    public void draw(SpriteBatch sb) {
        // TODO:
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
