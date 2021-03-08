package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.utilites.RendingUtilities;

import java.util.ArrayList;

public class Bullet extends PhysicsObject implements Renderable {
    public enum BulletType {
        STANDARD,
        FIRE,
        VOLTAGE,
        ACID
    }
    private int creatorNetId;
    private static TextureAtlas texAtl = null;
    private static TextureRegion texture = null;
    private float bulletSize;

    //Server side constructor
    public Bullet(Vector2 position, Vector2 velocity, Vector2 acceleration, int creatorNetId, int networkID, float bulletSize) {
        super(position, velocity, acceleration, networkID);
        this.creatorNetId = creatorNetId;
        this.bulletSize = bulletSize;
        updateFrequency = ServerUpdateFrequency.ONCE;
    }

    //Client side constructor
    public Bullet(CreateBullet packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.creatorNetId = packet.creatorNetId;
        this.bulletSize = packet.bulletSize;
        updateFrequency = ServerUpdateFrequency.ONCE;
        loadTextures();
    }


    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateBullet(this));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
    }

    @Override
    public void run(float dt) {
    }

    public int getCreatorNetId() {
        return creatorNetId;
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
            texture = texAtl.findRegion("white_pixel");
        }
    }

    public float getBulletSize() {
        return bulletSize;
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        float width = bulletSize * texture.getRegionWidth();
        float height = bulletSize * texture.getRegionHeight();
        sb.draw(texture, position.x, position.y, width, height);

    }
}
