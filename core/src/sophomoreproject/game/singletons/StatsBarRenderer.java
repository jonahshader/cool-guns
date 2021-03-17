package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public class StatsBarRenderer {

    public static class StatsBarInfo{

        public float value, maxValue;
        public Color color;

        public StatsBarInfo(float value, float maxValue, Color color) {
            this.value = value;
            this.maxValue = maxValue;
            this.color = color;
        }
    }

    private static StatsBarRenderer instance;

    private static final float WIDTH = 30f;
    private static final float HEIGHT= 2f;
    private static final float BAR_INTERVAL = 3f;

    public static final Color HEALTH_BAR_COLOR = new Color(1,0,0,1);
    public static final Color SHIELD_BAR_COLOR = new Color(0,0,1,1);
    public static final Color STAMINA_BAR_COLOR = new Color(1,1,.2f,1);
    public static final Color ARMOR_BAR_COLOR = new Color(1,.5f,0,1);

    private Sprite pixel;

    private StatsBarRenderer() {
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        pixel = new Sprite(atlas.findRegion("white_pixel"));

        pixel.setOrigin(.5f, 0);

    }

    private void drawBar(SpriteBatch sb, Vector2 bottomCenter, StatsBarInfo bar, float scale){
        pixel.setColor(0,0,0,.5f);
        pixel.setScale(WIDTH * scale, HEIGHT * scale);
        pixel.setOriginBasedPosition(bottomCenter.x, bottomCenter.y);
        pixel.draw(sb);
        pixel.setColor(bar.color);
        pixel.setScale(WIDTH*(bar.value/bar.maxValue), HEIGHT);
        pixel.setOriginBasedPosition(bottomCenter.x, bottomCenter.y);
        pixel.draw(sb);
//        TextDisplay.getInstance().drawTextInWorld(sb, ((int)bar.value) + "/" + ((int)bar.maxValue), bottomCenter.x, bottomCenter.y, .15f, Color.WHITE );
//        sb.draw(pixel, bottomCenter.x, bottomCenter.y, WIDTH/2, 0, WIDTH, HEIGHT, 1,1,0 );

    }
    public void drawStatsBarsInWorld(SpriteBatch sb, Vector2 pos, ArrayList<StatsBarInfo> bars){
        Vector2 posCopy = new Vector2(pos);
        for (StatsBarInfo bar: bars){
            drawBar(sb,posCopy,bar, 1);
            posCopy.y += BAR_INTERVAL;

            //bar_interval * scale
            // create hud scale

        }
//       for (int i = 0; i < bars.size(); i++) {
//           posCopy.y = pos.y + i * BAR_INTERVAL;
//           drawBar(sb,posCopy, bars.get(i));
//
//       }
    }

    public static StatsBarRenderer getInstance() {
        if (instance == null) instance = new StatsBarRenderer();
        return instance;
    }
}
