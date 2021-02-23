package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.utilites.RendingUtilities;

import java.util.ArrayList;

public class Player extends PhysicsObject implements Renderable{

    private static TextureAtlas texAtl = null;


    private final Vector2 PLAYER_SIZE = new Vector2(6*2, 9*2);


    private int accountId;

    public Player(Vector2 position, int accountId, int networkID) {
        super(position, new Vector2(0,0), new Vector2(0,0), networkID);
        this.accountId = accountId;
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");

        }
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    public Player(CreatePlayer packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.accountId = packet.accountId;
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");

        }
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreatePlayer(this));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        // assume object is of type
    }

    public void run(float dt) {
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        RendingUtilities.renderCharacter(position, velocity, PLAYER_SIZE, playerTexFront, playerTexBack,playerTexRight,sb);
    }

    public int getAccountId() {
        return accountId;
    }

    private void loadTextures () {

    }
}
