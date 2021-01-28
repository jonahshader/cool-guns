package sophomoreproject.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


//TODO: https://www.gamedevelopment.blog/asset-manager-libgdx-tutorial/




public final class CustomAssetManager {
    private static CustomAssetManager instance;

    public final AssetManager manager = new AssetManager();

    //Textures
    public final String spritePack = "graphics/spritesheets/sprites.pack";

    public void loadImages() {
        manager.load(spritePack, TextureAtlas.class);
    }

    //Sounds
/*    public final String testSound = "audio/music/"

    public void loadSounds() {
        manager.load(testSound, Sound.class);
    }
*/

 /*   public BitmapFont fontHelper(String filepath, float scale) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator();

    }
*/



    private CustomAssetManager() {
        // load assets here
    }

    public static CustomAssetManager getInstance() {
        if (instance == null) {
            instance = new CustomAssetManager();
        }

        return instance;
    }


}
