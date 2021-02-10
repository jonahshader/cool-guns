package sophomoreproject.game.singletons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


//TODO: https://www.gamedevelopment.blog/asset-manager-libgdx-tutorial/


public final class CustomAssetManager {
    private static CustomAssetManager instance;

    public final AssetManager manager = new AssetManager();

    //Textures
    public static final String SPRITE_PACK = "graphics/spritesheets/sprites.pack";

    public void loadImages() {
        manager.load(SPRITE_PACK, TextureAtlas.class);
    }

    //Sounds
    public final String openSound = "audio/sounds/open.wav";
    public final String closeSound = "audio/sounds/open.wav";
    public final String menuSound = "audio/sounds/menu_mouse.wav";

    public void loadSounds() {
        manager.load(openSound, Sound.class);
        manager.load(closeSound, Sound.class);
        manager.load(menuSound, Sound.class);
    }

    //fonts
    public final String menuFont = "graphics/fonts/myfont.ttf";

    public void loadFonts() {
        manager.load(menuFont, BitmapFont.class);
    }

 //is this necessary?
    /*   public BitmapFont fontHelper(String filepath, float scale) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator();

    }
*/

/*    // Particle Effects
    public final String somePE = "graphics/effects/PE1";

    public void loadParticleEffects(){
        ParticleEffectParameter pef = new ParticleEffectParameter();
        pef.atlasFile = "images/images.pack";
        manager.load(somePE, ParticleEffect.class, pef);
    }
*/


    public static CustomAssetManager getInstance() {
        if (instance == null) {
            instance = new CustomAssetManager();
        }
        return instance;
    }


}
