package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public final class StatsBarRenderer {

    public static class StatsBarInfo{

        public int value, maxValue;
        public Color color;
        public String barName;

        public StatsBarInfo(int value, int maxValue, Color color, String barName) {
            this.value = value;
            this.maxValue = maxValue;
            this.color = color;
            this.barName = barName;
        }
    }

    private static StatsBarRenderer instance;

    public static final float WIDTH = 30f;
    public static final float HEIGHT= 2f;
    public static final float BAR_INTERVAL = 3f;

    public static final Color HEALTH_BAR_COLOR = new Color(1,0,0,1);
    public static final Color SHIELD_BAR_COLOR = new Color(0,0,1,1);
    public static final Color STAMINA_BAR_COLOR = new Color(1,1,.2f,1);

    private final Sprite pixel;

    private StatsBarRenderer() {
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        pixel = new Sprite(atlas.findRegion("white_pixel"));

        pixel.setOrigin(.5f, 0);

    }

    private void drawBar(SpriteBatch sb, Vector2 bottomCenter, StatsBarInfo bar, boolean drawLabel){
        pixel.setColor(0, 0, 0, .5f);
        pixel.setScale(WIDTH, HEIGHT);
        pixel.setOriginBasedPosition(bottomCenter.x, bottomCenter.y);
        pixel.draw(sb);
        pixel.setColor(bar.color);
        pixel.setScale(WIDTH * (bar.value / (float) bar.maxValue), HEIGHT);
        pixel.setOriginBasedPosition(bottomCenter.x, bottomCenter.y);
        pixel.draw(sb);

        if (drawLabel) {
            TextDisplay.getInstance().drawTextInWorld(sb, bar.barName, bottomCenter.x - WIDTH, bottomCenter.y, .1f, Color.WHITE );
        }
    }
    public void drawStatsBarsInWorld(SpriteBatch sb, Vector2 pos, ArrayList<StatsBarInfo> bars, boolean drawLabel, float spacing){
        Vector2 posCopy = new Vector2(pos);
        for (StatsBarInfo bar: bars){
            if (bar.maxValue != 0) {
                drawBar(sb, posCopy, bar, drawLabel);
                posCopy.y += spacing;
            }
             
            //bar_interval * scale
            // create hud scale

        }

    }
    public void drawStatsBarsInWorld(SpriteBatch sb, Vector2 pos, ArrayList<StatsBarInfo> bars, boolean drawLabel){
        drawStatsBarsInWorld(sb,pos,bars,drawLabel, BAR_INTERVAL);

    }

    public static StatsBarRenderer getInstance() {
        if (instance == null) instance = new StatsBarRenderer();
        return instance;
    }
}
