package sophomoreproject.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.SoundSystem;

import java.util.ArrayList;

public class MenuItem {
    private MenuAction action;
    private float x, y, width, height;
    private final int MOUSE_OVER_INDENT = 15;
    public static final int MENU_PADDING = 25;
    private String label;
    private BitmapFont font;
    private Camera camera;
    private static Sound mouseOverSound;
    private static Sound clickSound;

    private boolean mouseOver = false;

    // constructor for first menu item
    public MenuItem(MenuAction action, float x, float y, float width, float height, String label, BitmapFont font, Camera camera) {
        this.action = action;
        this.x = x - (width / 2);
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.font = font;
        this.camera = camera;

        loadSounds();
    }

    // constructor for other items.
    public MenuItem(MenuAction action, String label, MenuItem previousMenuItem, BitmapFont font, Camera camera) {
        this.action = action;
        this.label = label;
        this.font = font;
        this.camera = camera;

        this.x = previousMenuItem.x;
        this.y = previousMenuItem.y - (previousMenuItem.height + MENU_PADDING);
        this.width = previousMenuItem.width;
        this.height = previousMenuItem.height;

        loadSounds();
    }

    public void run(float dt) {
        Vector2 m = getMouseWorld();
        if (m.x >= x && m.y >= y && m.x <= x + width
                && m.y <= y + height) {
            if (Gdx.input.justTouched()) {
                SoundSystem.getInstance().playSoundStandalone(clickSound, .8f, 0f);
                action.execute();
            }

            if (!mouseOver) {
                mouseOver = true;
                // play mouseOver sound
                SoundSystem.getInstance().playSoundStandalone(mouseOverSound, .8f, 0f);
            }
        } else {
            mouseOver = false;
        }
    }

    public void drawShape(ShapeRenderer sr) {
        Vector2 m = getMouseWorld();
        if (m.x >= x && m.y >= y && m.x <= x + width
                && m.y <= y + height) {
            sr.setColor(0.8f, 0.8f, 0.8f, 1);
            sr.rect(x + MOUSE_OVER_INDENT, y, width, height);
        } else {
            sr.setColor(0.5f, 0.5f, 0.5f, 1);
            sr.rect(x, y, width, height);
        }
    }

    public void drawText(SpriteBatch sb) {
        Vector2 m = getMouseWorld();
        if (m.x >= x && m.y >= y && m.x <= x + width
                && m.y <= y + height) {
            font.draw(sb, label, (x + height / 4 ) + MOUSE_OVER_INDENT, y + height / 2);
        } else {
            font.draw(sb, label, x + height / 4, y + height / 2);
        }
    }

    private Vector2 getMouseWorld() {
        Vector3 mouseWorld = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Vector2(mouseWorld.x, mouseWorld.y);
    }

    private void loadSounds() {
        if (mouseOverSound == null) {
            mouseOverSound = CustomAssetManager.getInstance().manager.get(CustomAssetManager.MENU_SOUND, Sound.class);
            clickSound = CustomAssetManager.getInstance().manager.get(CustomAssetManager.OPEN_SOUND, Sound.class);
        }
    }
}
