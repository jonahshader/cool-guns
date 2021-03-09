package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.clientlisteners.*;
import sophomoreproject.game.packets.RequestGameData;

import java.util.ArrayList;

public class GameClient {
    private GameWorld world;
    private int accountID;


    public GameClient(int accountID) {
        this.accountID = accountID;
        world = new GameWorld();
        PlayerController.getInstance().setGameWorld(world);
        ClientNetwork client = ClientNetwork.getInstance();
        client.addListener(new ObjectCreationListener(world, this));
        client.addListener(new ObjectRemoveListener(world));
        client.addListener(new ObjectUpdateListener(world));
        client.addListener(new SleepListener(world));

        // request game data
        client.sendPacket(new RequestGameData(accountID));
    }

    public void run(float dt) {
        world.update(dt);
    }

    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        world.draw(dt, sb, sr);
    }

    public void setClientControlledPlayer(Player player) {
        PlayerController.getInstance().setPlayer(player);
    }

    public int getAccountID() {
        return accountID;
    }
}
