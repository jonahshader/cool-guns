package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * network id is a unique id that is assigned by the server.
 */

public abstract class GameObject {
    enum ServerUpdateFrequency {
        CONSTANT,
        ONCE,
        SEND_ONLY
    }
    protected long networkID;
    protected ServerUpdateFrequency updateFrequency;

    long getNetworkID() { return networkID; }
    ServerUpdateFrequency getUpdateFrequency() { return updateFrequency; }

    abstract void run(float dt);
    abstract void draw(SpriteBatch sb);
}
