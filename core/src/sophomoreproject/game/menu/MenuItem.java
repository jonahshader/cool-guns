package sophomoreproject.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class MenuItem {
    private MenuAction action;
    private float x, y, width, height;
    private final int MOUSE_OVER_INDENT = 8;
    public static final int MENU_PADDING = 50;
    private String label;
    private BitmapFont font;

    // constructor for first menu item
    public MenuItem(MenuAction action, float x, float y, float width, float height, String label, BitmapFont font) {
        this.action = action;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.font = font;
    }

    // constructor for other items.
    public MenuItem(MenuAction action, String label, MenuItem previousMenuItem, BitmapFont font) {
        this.action = action;
        this.label = label;
        this.font = font;

        this.x = previousMenuItem.x;
        this.y = previousMenuItem.height + previousMenuItem.y + MENU_PADDING;
        this.width = previousMenuItem.width;
        this.height = previousMenuItem.height;
    }

    public void run(float dt) {
        if (Gdx.input.getX() >= x && Gdx.input.getY() >= y && Gdx.input.getX() <= x + width
        && Gdx.input.getY() <= y + height) {
            if (Gdx.input.justTouched()) {
                action.execute();
            }
        }
    }


    public void drawShape(ShapeRenderer sr) {
        if (Gdx.input.getX() >= x && Gdx.input.getY() >= y && Gdx.input.getX() <= x + width
                && Gdx.input.getY() <= y + height) {
            sr.setColor(0.8f,0.8f,0.8f, 1);
            sr.rect(x + MOUSE_OVER_INDENT, y, width, height);
        } else {
        sr.setColor(0.5f,0.5f,0.5f, 1);
        sr.rect(x, y, width, height); }
    }

    public void drawText(SpriteBatch sb) {
        if (Gdx.input.getX() >= x && Gdx.input.getY() >= y && Gdx.input.getX() <= x + width
                && Gdx.input.getY() <= y + height) {
            font.draw(sb, label, x + MOUSE_OVER_INDENT, y);
        } else {
        font.draw(sb, label, x, y); }
    }
}
