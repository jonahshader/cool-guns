package sophomoreproject.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.ClientNetwork;

public class GameClient {
    private final ClientNetwork client = ClientNetwork.getInstance();
    private GameWorld world;

    public GameClient() {
        client.addListener(new Listener(){
            @Override
            public void received(Connection c, Object o) {
//                if (o instanceof )
            }
        });
    }

    public void run(float dt) {
        world.update(dt);
    }

    public void draw(SpriteBatch sb) {
        world.draw(sb);
    }
}
