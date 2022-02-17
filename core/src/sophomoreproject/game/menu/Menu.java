package sophomoreproject.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuItem> items = new ArrayList<>();
    private BitmapFont font;
    private Camera camera;
    private float yOffset;

    public Menu(BitmapFont font, Camera camera, float yOffset) {
        this.font = font;
        this.camera = camera;
        this.yOffset = yOffset;
    }

    public void addMenuItem(String label, MenuAction action) {
        // TODO: add menu item to items arraylist
        MenuItem newItem;
        if (items.size() == 0) {
          //Call first menu item constructor
            newItem = new MenuItem(action, MenuItem.MENU_PADDING, MenuItem.MENU_PADDING + yOffset, 455f, 90f, label, font, camera);
        } else {
          //Call other items constructor
            newItem = new MenuItem(action, label, items.get(items.size() - 1), font, camera);
        }
        items.add(newItem);
    }

    public void drawShape(ShapeRenderer sr) {
        for (MenuItem item : items)
            item.drawShape(sr);
    }

    public void drawText(SpriteBatch sb) {
        for (MenuItem item : items) item.drawText(sb);
    }

    public void run(float dt) {
        for (MenuItem item : items) item.run(dt);
    }
}
