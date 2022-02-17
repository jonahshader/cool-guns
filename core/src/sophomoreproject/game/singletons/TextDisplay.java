package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.*;

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
    private final BitmapFont fontSpaced;
    private static Sprite pixel;

    private final ArrayList<TextEntry> topLeftText;
    private final ArrayList<TextEntry> topText;
    private final ArrayList<TextEntry> topRightText;

    public enum TextPosition {
        TOP_LEFT,
        TOP,
        TOP_RIGHT
    }

    private TextDisplay() {
        font = CustomAssetManager.getInstance().manager.get(NORMAL_FONT);
        fontSpaced = CustomAssetManager.getInstance().manager.get(MENU_FONT);
        fontSpaced.getData().setLineHeight(36);
        topLeftText = new ArrayList<>();
        topText = new ArrayList<>();
        topRightText = new ArrayList<>();

        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        pixel = new Sprite(atlas.findRegion("white_pixel"));
    }

    public void addHudText(TextEntry text, TextPosition pos) {
        switch (pos) {
            case TOP_LEFT:
                topLeftText.add(text);
                break;
            case TOP:
                topText.add(text);
                break;
            case TOP_RIGHT:
                topRightText.add(text);
                break;
            default:
                break;
        }
    }

    public void removeHudTextByReference(TextEntry textRef) {
        topLeftText.remove(textRef);
        topText.remove(textRef);
        topRightText.remove(textRef);
    }

    public void draw(SpriteBatch sb, Viewport hudVp) {
        font.getData().setScale(0.25f);
        for (int i = 0; i < topLeftText.size(); ++i) {
            drawText(sb, topLeftText.get(i).toString(), TEXT_PADDING, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), .25f, Color.WHITE, Align.topLeft, font);
        }
        for (int i = 0; i < topText.size(); ++i) {
            drawText(sb, topText.get(i).toString(), hudVp.getWorldWidth() / 2f, hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), .25f, Color.WHITE, Align.center, font);
        }
        for (int i = 0; i < topRightText.size(); ++i) {
            drawText(sb, topRightText.get(i).toString(), hudVp.getWorldWidth(), hudVp.getWorldHeight() - (TEXT_PADDING + i * font.getLineHeight() * TEXT_OFFSET_PERCENT), .25f, Color.WHITE, Align.topRight, font);
        }
    }

    public void drawTextInWorld(SpriteBatch sb, String text, float x, float y, float scale, Color c) {
        drawText(sb, text, x, y + font.getLineHeight() * .5f, scale, c, Align.center, font);
    }

    public void drawTextBoxInWorld(SpriteBatch sb, String text, float x, float y, float width, float height, float textScale, Color textColor, Color boxColor) {
        pixel.setColor(boxColor);
        pixel.setPosition(x, y);
        pixel.setSize(width, -height);
        pixel.draw(sb);
        float lineHeight = font.getLineHeight();
//        font.getData().setLineHeight(lineHeight * font.getCapHeight() * 100 / textScale);

        drawText(sb, text, x, y, textScale, textColor, Align.topLeft, fontSpaced, width);
//        font.getData().setLineHeight(lineHeight);
    }

    private void drawText(SpriteBatch sb, String text, float x, float y, float scale, Color c, int align, BitmapFont f,  float targetWidth) {
        f.setUseIntegerPositions(false);
        f.getData().setScale(scale);
        f.setColor(0f, 0f, 0f, c.a);
        f.draw(sb, text, x - UNDERTEXT_OFFSET * scale, y - UNDERTEXT_OFFSET * scale, targetWidth, align, targetWidth > 0);

        f.setColor(c);
        f.draw(sb, text, x, y, targetWidth, align, targetWidth > 0);
    }

    private void drawText(SpriteBatch sb, String text, float x, float y, float scale, Color c, int align, BitmapFont f) {
        drawText(sb, text, x, y, scale, c, align, f, 0);
    }

    public static TextDisplay getInstance() {
        if (instance == null) instance = new TextDisplay();
        return instance;
    }
}
