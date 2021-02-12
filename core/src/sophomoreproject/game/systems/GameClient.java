package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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
    }

    public void run(float dt) {
        world.clientOnly(dt, client);
        world.update(dt);
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        world.draw(sb, sr);
    }
}
