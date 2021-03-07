package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.utilites.RendingUtilities;

import java.util.ArrayList;

public class Player extends PhysicsObject implements Renderable{

    private static TextureAtlas texAtl = null;
    private static TextureRegion[] textures = null;

    private final Vector2 PLAYER_SIZE = new Vector2(1.5f, 1.5f);

    private final String username;


    private int accountId;

    public Player(Vector2 position, int accountId, int networkID, String username, boolean client) {
        super(position, new Vector2(0,0), new Vector2(0,0), networkID);
        this.accountId = accountId;
        this.username = username;
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
        loadTextures(client);
    }

    public Player(CreatePlayer packet, boolean client) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.accountId = packet.accountId;
        this.username = packet.username;
        loadTextures(client);

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
        RendingUtilities.renderCharacter(position, velocity, PLAYER_SIZE, sb, textures);
        TextDisplay.getInstance().drawTextInWorld(sb, username, position.x, position.y + 24, .25f, new Color(1f, 1f, 1f, 1f));
    }

    public int getAccountId() {
        return accountId;
    }

    private void loadTextures (boolean client) {
        if (texAtl == null && client) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");

            textures = new TextureRegion[8];
            textures[0] = texAtl.findRegion("player_right");
            textures[1] = texAtl.findRegion("player_top_right");
            textures[2] = texAtl.findRegion("player_back");
            textures[3] = texAtl.findRegion("player_top_left");
            textures[4] = texAtl.findRegion("player_left");
            textures[5] = texAtl.findRegion("player_bottom_left");
            textures[6] = texAtl.findRegion("player_front");
            textures[7] = texAtl.findRegion("player_bottom_right");
        }
    }

    public String getUsername() {
        return username;
    }
}
