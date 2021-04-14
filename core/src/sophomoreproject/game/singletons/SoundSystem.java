package sophomoreproject.game.singletons;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import sophomoreproject.game.gameobjects.Player;

import java.util.ArrayList;
import java.util.List;

import static sophomoreproject.game.screens.GameScreen.GAME_WIDTH;
import static sophomoreproject.game.singletons.CustomAssetManager.*;

public final class SoundSystem {
    private static SoundSystem instance;
    private CustomAssetManager assets;

    private float overallVolume = 1f;
    private List<Sound> bulletSounds, footstepSounds, enemyBlobSounds;
    private Player player;
    private Music music;

    public enum SoundGroup {
        BULLET,
        FOOT_STEP,
        ENEMY_BLOB,
    }

    private SoundSystem() {
        assets = CustomAssetManager.getInstance();
        music = CustomAssetManager.getInstance().manager.get(GREEN_BIOME_MUSIC, Music.class);
        bulletSounds = new ArrayList<>();
        footstepSounds = new ArrayList<>();
        enemyBlobSounds = assets.getSoundList(LIST_ENEMY_BLOB, LIST_ENEMY_BLOB_COUNT, LIST_ENEMY_BLOB_FORMAT);

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
     * @param volume         the range (0,1) to play the sound in the game
     */
    public void playSoundInWorld(Sound s, Vector2 soundPosition, float volume, float pitch) {
        if (player == null) return;
        float pan = (soundPosition.x - player.position.x) / (GAME_WIDTH * .6f);
        float volScale = 1-Vector2.len(soundPosition.x - player.position.x, soundPosition.y - player.position.y) / 640f;
        volScale = (float)Math.pow(Math.max(0, volScale), 1.5f);
        pan = Math.max(-1, pan);
        pan = Math.min(1, pan);
        if (volScale < 0.01) return; // early return if the sound is barely audible. don't want entire world to emit sounds all the time
        float realVolume = volume * overallVolume * volScale;
        s.play(realVolume, pitch, pan);
    }

    public void playSoundStandalone(Sound s, float volume, float pan) {
        float realVolume = volume * overallVolume;
        s.play(realVolume, 1, pan);
    }

    public void setOverallVolume(float overallVolume) {
        this.overallVolume = overallVolume;
        music.setVolume(overallVolume * .1f);
    }

    public void startMusic() {
        music.setLooping(true);
        music.setVolume(overallVolume * .1f);
        music.play();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void playSoundGroup(SoundGroup s, Vector2 soundPosition, float volume, float pitch) {
        if (player != null) {
            switch (s) {
                case BULLET:
                    playSoundInWorld(bulletSounds.get((int) (Math.random() * bulletSounds.size())), soundPosition, volume, pitch);
                    break;
                case FOOT_STEP:
                    playSoundInWorld(footstepSounds.get((int) (Math.random() * footstepSounds.size())), soundPosition, volume, pitch);
                    break;
                case ENEMY_BLOB:
                    playSoundInWorld(enemyBlobSounds.get((int) (Math.random() * enemyBlobSounds.size())), soundPosition, volume, pitch);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + s);
            }
        }

    }
}
