package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;

public class Player extends PhysicsObject implements Renderable{

    private static TextureAtlas texAtl = null;
    private static TextureRegion playerTex = null;

    private int accountId;

    public Player(Vector2 position, int accountId) {
        super(position, new Vector2(0,0), new Vector2(0,0));
        this.accountId = accountId;
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/stuff.pack");
            playerTex = texAtl.findRegion("fronttroll");
        }
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreatePlayer(this));
    }

    public void run(float dt) {
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(playerTex, position.x, position.y);
    }

    public int getAccountId() {
        return accountId;
    }
}
