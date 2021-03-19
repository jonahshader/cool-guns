package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.gameobjects.Player;

import java.util.ArrayList;

public final class HUD {

    private HUD(){
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(640 * .5f, 360 * .5f, cam);


        bars = new ArrayList<>();
        healthBar = new StatsBarRenderer.StatsBarInfo(15,20,StatsBarRenderer.HEALTH_BAR_COLOR);
        shieldBar = new StatsBarRenderer.StatsBarInfo(10,20, StatsBarRenderer.SHIELD_BAR_COLOR);
        staminaBar = new StatsBarRenderer.StatsBarInfo(30,50, StatsBarRenderer.STAMINA_BAR_COLOR);
        armorBar = new StatsBarRenderer.StatsBarInfo(6,15, StatsBarRenderer.ARMOR_BAR_COLOR);
        bars.add(healthBar);
        bars.add(shieldBar);
        bars.add(staminaBar);
        bars.add(armorBar);
    }
    private static HUD instance;
    private Player player;
    private Vector2 hudPos = new Vector2();
    private static final float HUD_PADDING = 4;
    private Viewport viewport;
    private Camera cam;

    private final ArrayList<StatsBarRenderer.StatsBarInfo> bars;
    private final StatsBarRenderer.StatsBarInfo healthBar;
    private final StatsBarRenderer.StatsBarInfo shieldBar;
    private final StatsBarRenderer.StatsBarInfo staminaBar;
    private final StatsBarRenderer.StatsBarInfo armorBar;

    public static HUD getInstance() {
        if (instance == null){
            instance = new HUD();
        }
        return instance;
    }


    public void setPlayer(Player player) {
        this.player = player;


    }

    public void draw(SpriteBatch sb){
        if (player != null){
            viewport.apply(true);
            sb.setProjectionMatrix(cam.combined);
            sb.begin();
            hudPos.set(HUD_PADDING + StatsBarRenderer.WIDTH/2, HUD_PADDING);
            StatsBarRenderer.getInstance().drawStatsBarsInWorld(sb,hudPos,bars);
            sb.end();
        }
    }
    public void resize(int width, int height){
        viewport.update(width, height);
    }
}
