package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class TextDisplay {
    private static TextDisplay instance;

    private static final float UNDERTEXT_OFFSET = 2f;
    private static final float TEXT_OFFSET_PERCENT = 1.5f; // percentage of height
    private static final float TEXT_PADDING = 3f;
    private final BitmapFont font;

    private final ArrayList<String> topLeftText;
    private final ArrayList<String> topText;
//    private final ArrayList<String> topRightText;

    public enum TextPosition {
        TOP_LEFT,
        TOP,
//        TOP_RIGHT
    }


    private TextDisplay() {
        font = CustomAssetManager.getInstance().manager.get(MENU_FONT);

        topLeftText = new ArrayList<>();
        topText = new ArrayList<>();
//        topRightText = new ArrayList<>();
    }

    public void addHudText(String text, TextPosition pos) {
        switch (pos) {
            case TOP_LEFT:
                topLeftText.add(text);
                break;
            case TOP:
                topText.add(text);
                break;
//            case TOP_RIGHT:
//                topRightText.add(text);
//                break;
            default:
                break;
        }
    }

    public void removeHudTextByReference(String textRef) {
        topLeftText.remove(textRef);
        topText.remove(textRef);
//        topRightText.remove(textRef);
    }

    public void draw(SpriteBatch sb, Viewport hudVp) {
        for (int i = 0; i < topLeftText.size(); ++i) {
            drawText(sb, topLeftText.get(i), TEXT_PADDING, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), 1f, Color.WHITE);
            System.out.println("Rendering text");
        }
        for (int i = 0; i < topText.size(); ++i) {
            drawText(sb, topText.get(i), TEXT_PADDING, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), 1f, Color.WHITE);
        }

    }

//    public void drawTextInWorld(SpriteBatch sb, float x, float y, Color c) {
//
//    }

    public void drawTextInWorld(SpriteBatch sb, String text, float x, float y, float scale) {
//        font.getData().setScale(scale);
        font.setColor(1f, 1f, 1f, 1f);
        font.draw(sb, text, x, y, 9999f, Align.center, false);
    }

    private void drawText(SpriteBatch sb, String text, float x, float y, float scale, Color c) {
//        font.getData().setScale(scale);
        font.setColor(0f, 0f, 0f, c.a);
        font.draw(sb, text, x + UNDERTEXT_OFFSET, y + UNDERTEXT_OFFSET);

        font.setColor(c);
        font.draw(sb, text, x, y);
    }

    public static TextDisplay getInstance() {
        if (instance == null) instance = new TextDisplay();
        return instance;
    }
}
