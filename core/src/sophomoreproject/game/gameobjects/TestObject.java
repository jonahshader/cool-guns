package sophomoreproject.game.gameobjects;

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

    public TestObject(Vector2 position) {
        super(position, new Vector2((float)ranNumPosNeg(), (float)ranNumPosNeg()), new Vector2());
        if (texAtlas == null) {
            texAtlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
            tex = texAtlas.findRegion("troll");
        }

        xFactor = (int)(Math.random() * 4) + 1;
        yFactor = (int)(Math.random() * 4) + 1;

        updateFrequency = ServerUpdateFrequency.CONSTANT;
    }

    public TestObject(CreateTestObject packet) {
        super(packet.u.x, packet.u.y, packet.u.xVel, packet.u.yVel, packet.u.xAccel, packet.u.yAccel);
        if (texAtlas == null) {
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
    public void run(float dt) {
        acceleration.x += Math.cos(lifeTime / (xFactor * Math.PI)) * dt;
        acceleration.y += Math.sin(lifeTime / (yFactor * Math.PI)) * dt;

        lifeTime += dt;
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(tex, position.x, position.y, 8f, 8f);
    }

    public void debugDraw(ShapeRenderer sr) {
        sr.ellipse(position.x, position.y, 8f, 8f);
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
