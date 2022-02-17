package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.clientlisteners.*;
import sophomoreproject.game.packets.RequestGameData;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.HUD;
import sophomoreproject.game.singletons.SoundSystem;
import sophomoreproject.game.systems.marker.Marker;

import java.util.ArrayList;

import static sophomoreproject.game.gameobjects.Player.MARKER_TEXT_COLOR;
import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public class GameClient {
    private final GameWorld world;
    private final int accountID;


    public GameClient(int accountID) {
        this.accountID = accountID;
        world = new GameWorld();
        PlayerController.getInstance().setGameWorld(world);
        HUD.getInstance().setGameWorld(world);
        ClientNetwork client = ClientNetwork.getInstance();
        client.addListener(new ObjectCreationListener(world, this));
        client.addListener(new ObjectRemoveListener(world));
        client.addListener(new ObjectUpdateListener(world));
        client.addListener(new SleepListener(world));

        // request game data
        client.sendPacket(new RequestGameData(accountID));

        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        new Marker(atlas.findRegion("spawn_icon"), new Vector2(), 1.0f, "Spawn", MARKER_TEXT_COLOR);
    }

    public void run(float dt) {
        world.update(dt);
    }

    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        world.draw(dt, sb, sr);
    }

    public void setClientControlledPlayer(Player player) {
        PlayerController.getInstance().setPlayer(player);
        HUD.getInstance().setPlayer(player);
        SoundSystem.getInstance().setPlayer(player);
    }

    public int getAccountID() {
        return accountID;
    }
}
