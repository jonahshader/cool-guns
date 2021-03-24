package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public final class TextDisplay {
    public static class TextEntry {
        public String entry;

        public TextEntry(String entry) {
            this.entry = entry;
        }

        @Override
        public String toString() {
            return entry;
        }
    }
    private static TextDisplay instance;

    private static final float UNDERTEXT_OFFSET = 3f;
    private static final float TEXT_OFFSET_PERCENT = 1.5f; // percentage of height
    private static final float TEXT_PADDING = 3f;
    public static final Color WHITE = new Color(1,1,1,1);
    private final BitmapFont font;

    private final ArrayList<TextEntry> topLeftText;
    private final ArrayList<TextEntry> topText;
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

    public void addHudText(TextEntry text, TextPosition pos) {
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
        font.getData().setScale(0.25f);
        for (int i = 0; i < topLeftText.size(); ++i) {
            drawText(sb, topLeftText.get(i).toString(), TEXT_PADDING, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), .25f, Color.WHITE, Align.topLeft);
        }
        for (int i = 0; i < topText.size(); ++i) {
            drawText(sb, topText.get(i).toString(), hudVp.getWorldWidth() / 2f, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), .25f, Color.WHITE, Align.center);
        }

    }

    public void drawTextInWorld(SpriteBatch sb, String text, float x, float y, float scale, Color c) {
        drawText(sb, text, x, y + font.getLineHeight() * .5f, scale, c, Align.center);
    }

    private void drawText(SpriteBatch sb, String text, float x, float y, float scale, Color c, int align) {
        font.setUseIntegerPositions(false);
        font.getData().setScale(scale);
        font.setColor(0f, 0f, 0f, c.a);
        font.draw(sb, text, x - UNDERTEXT_OFFSET * scale, y - UNDERTEXT_OFFSET * scale, 0f, align, false);

        font.setColor(c);
        font.draw(sb, text, x, y, 0f, align, false);
    }

    public static TextDisplay getInstance() {
        if (instance == null) instance = new TextDisplay();
        return instance;
    }
}
