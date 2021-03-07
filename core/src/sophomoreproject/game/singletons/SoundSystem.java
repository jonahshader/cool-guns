package sophomoreproject.game.singletons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public final class SoundSystem {
    private static SoundSystem instance;
    private CustomAssetManager assets;

    private float overallVolume = 1f;

//    enum SoundGroup {
//        BULLET,
//        FOOT_STEP
//    }

    private SoundSystem() {
        assets = CustomAssetManager.getInstance();
    }

    public static SoundSystem getInstance() {
        if (instance == null) {
            instance = new SoundSystem();
        }

        return instance;
    }

    public void playSoundInWorld(Sound s, Vector2 soundPosition, Vector2 cameraPosition, float volume) {

    }

    public void playSoundStandalone(Sound s, float volume, float pan) {
        float realVolume = volume * overallVolume;
    }

    public void setOverallVolume(float overallVolume) {
        this.overallVolume = overallVolume;
    }
}
