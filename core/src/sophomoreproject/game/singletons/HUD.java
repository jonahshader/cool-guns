package sophomoreproject.game.singletons;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.systems.GameWorld;
import sophomoreproject.game.systems.PlayerController;

import java.util.ArrayList;
import java.util.List;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public final class HUD {
    private static HUD instance;
    private Player player;
    private GameWorld world;
    private Vector2 hudPos = new Vector2();
    private static final float HUD_PADDING = 4;
    private Viewport viewport;
    private Camera cam;
    private Sprite pixel;
    private boolean connectionError = false;

    private static final float HOTBAR_SPACE_SIZE = 16;
    private static final float HOTBAR_ICON_PAD = 2;
    private static final Color HOTBAR_COLOR = new Color(0.75f, 0.75f, 0.75f, 0.5f);
    private static final Color HOTBAR_COLOR_EQUIPPED = new Color(0.8f, 0.8f, 0.8f, 1);

    private final ArrayList<StatsBarRenderer.StatsBarInfo> bars;
    private final StatsBarRenderer.StatsBarInfo healthBar;
    private final StatsBarRenderer.StatsBarInfo shieldBar;
    private final StatsBarRenderer.StatsBarInfo staminaBar;
    private final StatsBarRenderer.StatsBarInfo armorBar;

    private HUD(){
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(640 * .5f, 360 * .5f, cam);
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        pixel = new Sprite(atlas.findRegion("white_pixel"));

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
        viewport.apply(true);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (player != null){
            hudPos.set(HUD_PADDING + StatsBarRenderer.WIDTH/2, HUD_PADDING);
            StatsBarRenderer.getInstance().drawStatsBarsInWorld(sb,hudPos,bars);
            drawHotbar(sb);
        }

        if (connectionError) {
            TextDisplay.getInstance().drawTextInWorld(sb, "Connection Error!\n\n Please Restart", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, .2f, new Color(1, .8f, .8f, 1));
        }

        sb.end();
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    private void drawHotbar(SpriteBatch sb) {
        if (!PlayerController.getInstance().playerIsNull()) {
            int inventorySize = PlayerController.getInstance().getInventorySize();
            int equippedItemIndex = PlayerController.getInstance().getEquippedItemIndex();
            Player player = PlayerController.getInstance().getPlayer();
            for (int i = 0; i < inventorySize; ++i) {
                List<Integer> inventory = player.getInventory();
                Object gameObj = null;
                if (inventory.get(i) != null) {
                    gameObj = world.getGameObjectFromID(inventory.get(i));
                }

                // calculate render pos
                float x = ((viewport.getWorldWidth() / 2f) - (HOTBAR_SPACE_SIZE * inventorySize / 2f)) + HOTBAR_SPACE_SIZE * i;
                float y = HOTBAR_ICON_PAD;

                if (gameObj != null) {
                    Item gameItem = (Item) gameObj;
                    // render
                    drawSingleHotbarItem(sb, x, y, i == equippedItemIndex, gameItem);
                } else {
                    // render
                    drawSingleHotbarItem(sb, x, y, i == equippedItemIndex, null);
                }
            }
        }
    }

    private void drawSingleHotbarItem(SpriteBatch sb, float x, float y, boolean equipped, Item item) {
        pixel.setColor(equipped ? HOTBAR_COLOR_EQUIPPED : HOTBAR_COLOR);
        pixel.setSize(HOTBAR_SPACE_SIZE, HOTBAR_SPACE_SIZE);
        pixel.setPosition(x, y);
        pixel.draw(sb);

        if (item != null)
            item.renderIcon(sb, HOTBAR_SPACE_SIZE - HOTBAR_ICON_PAD * 2, x + HOTBAR_ICON_PAD, y + HOTBAR_ICON_PAD);
    }

    public void setGameWorld(GameWorld world) {
        this.world = world;
    }
}
