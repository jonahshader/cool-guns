package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateTestObject;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public class TestObject extends PhysicsObject implements Renderable {
    private static TextureAtlas texAtlas = null;
    private static TextureRegion tex = null;
    private double lifeTime = 0.0;
    private int xFactor;
    private int yFactor;

    public TestObject(Vector2 position, int netID, boolean client) {
        super(position, new Vector2((float)ranNumPosNeg(), (float)ranNumPosNeg()), new Vector2(), netID);
        if (texAtlas == null && client) {
            texAtlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
            tex = texAtlas.findRegion("troll");
        }

        xFactor = (int)(Math.random() * 6) + 1;
        yFactor = (int)(Math.random() * 6) + 1;

        updateFrequency = ServerUpdateFrequency.CONSTANT;
    }

    public TestObject(CreateTestObject packet, boolean client) {
        super(packet.u.x, packet.u.y, packet.u.xVel, packet.u.yVel, packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        if (texAtlas == null && client) {
            texAtlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
            tex = texAtlas.findRegion("troll");
        }

        xFactor = packet.xFactor;
        yFactor = packet.yFactor;

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
    public void run(float dt) {
//        velocity.x = (float) (Math.cos(lifeTime * Math.PI * .5 * (xFactor)) * 3200 * dt);
//        velocity.y = (float) (Math.sin(lifeTime * Math.PI * .5 * (yFactor)) * 3200 * dt);
        velocity.x = (float) ranNumPosNeg() * 30;
        velocity.y = (float) ranNumPosNeg() * 30;

        lifeTime += dt;
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.draw(tex, position.x, position.y, 48f, 32f);
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
}
