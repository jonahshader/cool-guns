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
    public static final String SPRITE_PACK = "graphics/spritesheets/sprites.atlas";

    public void loadImages() {
        manager.load(SPRITE_PACK, TextureAtlas.class);
    }

    //Sounds
    public static final String OPEN_SOUND = "audio/sounds/open.ogg";
    public static final String CLOSE_SOUND = "audio/sounds/close.ogg";
    public static final String MENU_SOUND = "audio/sounds/menu_mouseover.ogg";

    public void loadSounds() {
        manager.load(OPEN_SOUND, Sound.class);
        manager.load(CLOSE_SOUND, Sound.class);
        manager.load(MENU_SOUND, Sound.class);
    }

    //fonts
    public static final String MENU_FONT = "graphics/fonts/dogica.fnt";

    public void loadFonts() {
        manager.load(MENU_FONT, BitmapFont.class);
    }


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
