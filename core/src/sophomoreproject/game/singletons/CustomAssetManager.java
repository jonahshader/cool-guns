package sophomoreproject.game.singletons;

import com.badlogic.gdx.assets.AssetManager;

public final class CustomAssetManager {
    private static CustomAssetManager instance;

    private AssetManager assMan;

    //TODO: https://www.gamedevelopment.blog/asset-manager-libgdx-tutorial/

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
