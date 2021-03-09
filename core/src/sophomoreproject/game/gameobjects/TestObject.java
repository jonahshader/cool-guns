package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateTestObject;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.utilites.RendingUtilities;

import java.util.ArrayList;

public class TestObject extends PhysicsObject implements Renderable {
    private static GameServer gameServer = null;

    private static TextureAtlas texAtl = null;
    private static TextureRegion[] textures = null;

    private static final float LIFE_SPAN = 30f;

    private double lifeTime = 0.0;
    private int xFactor;
    private int yFactor;

    private final Vector2 PLAYER_SIZE = new Vector2(1, 1);

    public TestObject(Vector2 position, int netID) {
        super(position, new Vector2((float)ranNumPosNeg() * 30, (float)ranNumPosNeg() * 30), new Vector2(), netID);

        xFactor = (int)(Math.random() * 6) + 1;
        yFactor = (int)(Math.random() * 6) + 1;

        updateFrequency = ServerUpdateFrequency.CONSTANT;
    }

    public TestObject(CreateTestObject packet) {
        super(packet.u.x, packet.u.y, packet.u.xVel, packet.u.yVel, packet.u.xAccel, packet.u.yAccel, packet.u.netID);

        xFactor = packet.xFactor;
        yFactor = packet.yFactor;
        loadTextures(true);

        updateFrequency = ServerUpdateFrequency.CONSTANT;
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateTestObject(this));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        // no special packet besides the physics update one
    }

    @Override
    public void run(float dt, GameServer server) {
//        velocity.x = (float) (Math.cos(lifeTime * Math.PI * .5 * (xFactor)) * 3200 * dt);
//        velocity.y = (float) (Math.sin(lifeTime * Math.PI * .5 * (yFactor)) * 3200 * dt);

        if (lifeTime > LIFE_SPAN) {
            gameServer.removeObject(networkID);
        }


        velocity.x = (float) ranNumPosNeg() * 30;
        velocity.y = (float) ranNumPosNeg() * 30;


        lifeTime += dt;
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        RendingUtilities.renderCharacter(position, velocity, PLAYER_SIZE,sb,textures);
//        debugDraw(sr);
    }

    public void debugDraw(ShapeRenderer sr) {
        sr.ellipse(position.x, position.y, 32f, 32f);
    }

    public int getxFactor() {
        return xFactor;
    }

    public int getyFactor() {
        return yFactor;
    }

    private static double ranNumPosNeg() {
        return (Math.random() * 2) - 1;
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

    public static void setGameServer(GameServer gameServer) {
        TestObject.gameServer = gameServer;
    }
}
