package sophomoreproject.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import static sophomoreproject.game.screens.GameScreen.HUD_HEIGHT;
import static sophomoreproject.game.screens.GameScreen.HUD_WIDTH;
import static sophomoreproject.game.singletons.CustomAssetManager.MAP_PACK;
import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public final class Tutorial {
    private static Tutorial instance;

    public class TutorialMessage {
        public float width, height;
        public float x, y;
        public float textScale;
        public String message;
        public TextureRegion texture;

        public TutorialMessage(float width, float height, float x, float y, float textScale, String message, TextureRegion texture) {
            this.width = width;
            this.height = height;
            this.textScale = textScale;
            this.message = message;
            this.texture = texture;
            this.x = x;
            this.y = y;
        }

        public void draw(SpriteBatch sb) {
            TextDisplay.getInstance().drawTextBoxInWorld(sb, message, x - width/2, y + height/2, width, height, textScale, TextDisplay.WHITE, MESSAGE_BODY);
            if (texture != null) {
                sb.draw(texture, x - texture.getRegionWidth() / 2f, y - height/2f);
            }
        }
    }

    private final Color MESSAGE_BODY = new Color(.6f, .6f, .6f, .4f);

    private List<TutorialMessage> messages = new ArrayList<>();
    private int tutorialIndex = 0;

    private Tutorial() {
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        TextureAtlas mapAtlas = CustomAssetManager.getInstance().manager.get(MAP_PACK);
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Welcome to Cool Guns! Press Space to move through the tutorial.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "This is the spawn area. Nothing can hurt you and your health regenerates within the circle.", atlas.findRegion("spawn_icon")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Press W to move up \nPress S to move down\nPress A to move left\nPress D to move right\nLeft click to shoot your weapon\nPress R to reload your weapon manually.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Hold Left Shift when walking to sprint, but watch out for the cool-down period!", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Hold Tab to see the leaderboard (for multiplayer)", null));
        messages.add(new TutorialMessage(256f, 128f, -140 + HUD_WIDTH/2f, -80 + HUD_HEIGHT/2f, .3f, "Here are your stats. The yellow bar is your stamina, the red bar is your health, and the blue bar is your shield. The shield bar only shows up after you equip a shield.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Important items will have indicators in a ring around you to show you where to go. Other players and the spawn icon will show up here.", atlas.findRegion("arrow")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Enemy spawns are scattered around the map and appear more frequently as you travel farther from spawn.", mapAtlas.findRegion("hole")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Kill enemies to get new weapons, shields, and shoes. Enemies get stronger as you travel farther from spawn and drop better loot.", atlas.findRegion("enemy")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, -100 + HUD_HEIGHT/2f, .3f, "Scrolling your mouse wheel or pressing 1 to 8 will change your selected item.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Press F when standing over an item to pick it up. Press G to drop the currently selected item.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Shoes make you move faster, and their effects stack up, so holding more shoes means more speed.", atlas.findRegion("speed_shoes")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Shields let you take additional damage before your health is affected. Their effects also stack up.", atlas.findRegion("shield")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Different terrains have different effects on your speed, so think again about taking that shortcut...", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "If you lose all your health, you will die and lose your items and respawn at the spawn area", atlas.findRegion("spawn_icon")));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Items that are dropped by enemies will have randomly offset stats, so there are chances to get extremely powerful or lame items from the same location.", null));
        messages.add(new TutorialMessage(256f, 128f, 0 + HUD_WIDTH/2f, 0 + HUD_HEIGHT/2f, .3f, "Go get some guns and have fun!", null));
    }

    public void draw(SpriteBatch sb) {
        if (tutorialIndex < messages.size()) {
            System.out.println("drawing tutorial num " + tutorialIndex);
            messages.get(tutorialIndex).draw(sb);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            tutorialIndex++;
        }
    }

    public static synchronized Tutorial getInstance() {
        if (instance == null) {
            instance = new Tutorial();
        }
        return instance;
    }
}
