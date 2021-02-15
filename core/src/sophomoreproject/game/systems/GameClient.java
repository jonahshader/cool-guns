package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.clientlisteners.ObjectCreationListener;
import sophomoreproject.game.networking.clientlisteners.ObjectUpdateListener;
import sophomoreproject.game.packets.RequestGameData;

public class GameClient {
    private final ClientNetwork client = ClientNetwork.getInstance();
    private GameWorld world;
    private int accountID;

    public GameClient(int accountID) {
        this.accountID = accountID;
        world = new GameWorld();
        client.addListener(new ObjectCreationListener(world));
        client.addListener(new ObjectUpdateListener(world));

        // request game data
        client.sendPacket(new RequestGameData(accountID));
        Player player = new Player(new Vector2(), accountID, -1);
        PlayerController.getInstance().setPlayer(player);
        world.queueAddObject(player);
    }

    public void run(float dt) {
        world.clientOnly(dt, client);
        world.update(dt);
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        world.draw(sb, sr);
    }
}
