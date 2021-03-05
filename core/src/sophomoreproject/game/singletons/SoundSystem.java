package sophomoreproject.game.singletons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public final class SoundSystem {
    private static SoundSystem instance;
    private CustomAssetManager assets;

    private float overallVolume = 1f;
    private ArrayList<Sound> bulletSounds, footstepSounds;

    enum SoundGroup {
        BULLET,
        FOOT_STEP
    }

    private SoundSystem() {
        assets = CustomAssetManager.getInstance();
        bulletSounds = new ArrayList<>();
        footstepSounds = new ArrayList<>();

        //  bulletSound.add(assets.manager.get());
        // footstepSound.add(assets.manager.get());
    }

    public static SoundSystem getInstance() {
        if (instance == null) {
            instance = new SoundSystem();
        }

        return instance;
    }

    /**
     * play sound with calculated panning related to sound and camera position
     * if sound position is left of camera position the panning is negative
     * if sound position is right of camera position the panning is positive
     *
     * @param s              the sound play
     * @param soundPosition  the position of the sound in game world
     * @param cameraPosition the position of the camera in game world
     * @param volume         the range (0,1) to play the sound in the game
     */
    public void playSoundInWorld(Sound s, Vector2 soundPosition, Vector2 cameraPosition, float volume) {
        float prePan = soundPosition.x - cameraPosition.x;
        float realVolume = volume * overallVolume;
        s.play(realVolume, 1, prePan / 320);
    }

    public void playSoundStandalone(Sound s, float volume, float pan) {
        float realVolume = volume * overallVolume;
        s.play(realVolume, 1, pan);
    }

    public void setOverallVolume(float overallVolume) {
        this.overallVolume = overallVolume;

    }

    public void playSoundGroup(SoundGroup s, Vector2 soundPosition, Vector2 cameraPosition, float volume) {
        switch (s) {

            case BULLET:

                playSoundInWorld(bulletSounds.get((int) (Math.random() * bulletSounds.size())), soundPosition, cameraPosition, volume);
                break;
            case FOOT_STEP:

                playSoundInWorld(footstepSounds.get((int) (Math.random() * footstepSounds.size())), soundPosition, cameraPosition, volume);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}
