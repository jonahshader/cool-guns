package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {
    protected long networkID;

    long getNetworkID() { return networkID; }

    abstract void run(float dt);
    abstract void draw(SpriteBatch sb);
}
