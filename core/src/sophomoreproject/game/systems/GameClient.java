package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.clientlisteners.ObjectCreationListener;

public class GameClient {
    private final ClientNetwork client = ClientNetwork.getInstance();
    private GameWorld world;

    public GameClient() {
        client.addListener(new ObjectCreationListener(world));
    }

    public void run(float dt) {
        world.update(dt);
    }

    public void draw(SpriteBatch sb) {
        world.draw(sb);
    }
}
