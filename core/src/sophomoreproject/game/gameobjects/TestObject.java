package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateTestObject;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;

public class TestObject extends PhysicsObject implements Renderable {
    private static TextureAtlas texAtlas = null;
    private static TextureRegion tex = null;
    private double lifeTime = 0.0;
    private int xFactor;
    private int yFactor;

    public TestObject(Vector2 position) {
        super(position, new Vector2((float)ranNumPosNeg(), (float)ranNumPosNeg()), new Vector2());
        if (texAtlas == null) {
            texAtlas = CustomAssetManager.getInstance().manager.get("stuff.pack");
            tex = texAtlas.findRegion("test_object");
        }

        xFactor = (int)(Math.random() * 4) + 1;
        yFactor = (int)(Math.random() * 4) + 1;

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
        sb.draw(tex, position.x, position.y);
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
