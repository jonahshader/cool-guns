package sophomoreproject.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.systems.GameWorld;
import sophomoreproject.game.systems.PlayerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    private static final float LEADERBOARD_ROW_SPACING = 16;
    private static final float LEADERBOARD_COLUMN_SPACING = 48;
    private static final float LEADERBOARD_TEXT_SIZE = .15f;
    private static final Color ONLINE_COLOR = new Color(0.2f, 1.0f, .1f, 1.0f);

    private final ArrayList<StatsBarRenderer.StatsBarInfo> bars;
    private final StatsBarRenderer.StatsBarInfo healthBar;
    private final StatsBarRenderer.StatsBarInfo shieldBar;
    private final StatsBarRenderer.StatsBarInfo staminaBar;

    private HUD(){
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(640 * .5f, 360 * .5f, cam);
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        pixel = new Sprite(atlas.findRegion("white_pixel"));


        bars = new ArrayList<>();
        healthBar = new StatsBarRenderer.StatsBarInfo(0,0,StatsBarRenderer.HEALTH_BAR_COLOR, "Health");
        shieldBar = new StatsBarRenderer.StatsBarInfo(0,0, StatsBarRenderer.SHIELD_BAR_COLOR, "Shield");
        staminaBar = new StatsBarRenderer.StatsBarInfo(0,0, StatsBarRenderer.STAMINA_BAR_COLOR, "Stamina");
        bars.add(healthBar);
        bars.add(shieldBar);
        bars.add(staminaBar);
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
            healthBar.value = PlayerController.getInstance().getPlayer().getHealth();
            healthBar.maxValue = PlayerController.getInstance().getPlayer().getMaxHealth();
            shieldBar.value = PlayerController.getInstance().getPlayer().getShield();
            shieldBar.maxValue = PlayerController.getInstance().getPlayer().getMaxShield();
            staminaBar.value = (int)Math.ceil(player.getStamina() * 100);
            staminaBar.maxValue = PlayerController.getInstance().getPlayer().getMaxStamina();
            hudPos.set(HUD_PADDING + StatsBarRenderer.WIDTH*3/2, HUD_PADDING);
            StatsBarRenderer.getInstance().drawStatsBarsInWorld(sb,hudPos,bars, true, 6);
            drawHotbar(sb);

            if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
                drawLeaderboard(sb);
            }
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

    private void drawLeaderboard(SpriteBatch sb) {
        List<Player> playersList = new ArrayList<>(world.getPlayers());
        for (GameObject o : world.getSleepingGameObjects()) {
            if (o instanceof Player) playersList.add((Player) o);
        }
        Player[] players = new Player[playersList.size()];
        playersList.toArray(players);
        Arrays.sort(players, Comparator.comparingInt(player1 -> -player1.getTotalDamage()));

        float lbWidth = LEADERBOARD_COLUMN_SPACING * (3);
        float lbHeight = (players.length + 1) * LEADERBOARD_ROW_SPACING;

//        pixel.setOrigin(0, 0);
        pixel.setPosition((viewport.getWorldWidth() / 2) - lbWidth / 2, viewport.getWorldHeight());
        pixel.setSize(lbWidth, lbHeight);
        pixel.setColor(HOTBAR_COLOR);
        pixel.draw(sb);


        float xStart = ((viewport.getWorldWidth() / 2) - (lbWidth / 2)) + (LEADERBOARD_COLUMN_SPACING * .5f);
        float yStart = viewport.getWorldHeight() - LEADERBOARD_ROW_SPACING * .5f;
        TextDisplay.getInstance().drawTextInWorld(sb, "Name", xStart, yStart, LEADERBOARD_TEXT_SIZE, TextDisplay.WHITE);
        TextDisplay.getInstance().drawTextInWorld(sb, "Total Damage", xStart + LEADERBOARD_COLUMN_SPACING, yStart, LEADERBOARD_TEXT_SIZE, TextDisplay.WHITE);
        TextDisplay.getInstance().drawTextInWorld(sb, "Online", xStart + LEADERBOARD_COLUMN_SPACING * 2, yStart, LEADERBOARD_TEXT_SIZE, TextDisplay.WHITE);
        for (int i = 0; i < players.length; ++i) {
            TextDisplay.getInstance().drawTextInWorld(sb, players[i].getUsername(), xStart, yStart - LEADERBOARD_ROW_SPACING * (i + 1), LEADERBOARD_TEXT_SIZE, TextDisplay.WHITE);
            TextDisplay.getInstance().drawTextInWorld(sb, ""+players[i].getTotalDamage(), xStart + LEADERBOARD_COLUMN_SPACING, yStart - LEADERBOARD_ROW_SPACING * (i + 1), LEADERBOARD_TEXT_SIZE, TextDisplay.WHITE);
            if (world.getGameObjectFromID(players[i].getNetworkID()) != null) {
                pixel.setColor(ONLINE_COLOR);
                pixel.setSize(LEADERBOARD_ROW_SPACING * .5f, LEADERBOARD_ROW_SPACING * .5f);
                pixel.setOriginCenter();
                pixel.setOriginBasedPosition(xStart + LEADERBOARD_COLUMN_SPACING * 2, yStart - LEADERBOARD_ROW_SPACING * (i + 1));
                pixel.draw(sb);
            }
        }
    }



    public void setGameWorld(GameWorld world) {
        this.world = world;
    }
}
