package sophomoreproject.game.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class MenuItem {
    private MenuAction action;
    private float x, y, width, height;
    private String label;

    // constructor for first menu item
    public MenuItem(MenuAction action, float x, float y, float width, float height, String label) {
        this.action = action;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    // constructor for
    public MenuItem(MenuAction action, String label, MenuItem previousMenuItem) {
        this.action = action;
        this.label = label;

        this.x = previousMenuItem.x;
        this.y = previousMenuItem.y;
        this.width = previousMenuItem.width;
        this.height = previousMenuItem.height;
    }

    public void run(float dt) {

        if (this menuitem was just clicked) {
            action.execute();
        }
    }

    public void draw(SpriteBatch sb) {

    }

}
