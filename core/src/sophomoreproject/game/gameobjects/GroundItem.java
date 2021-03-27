package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateGroundItem;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.InventoryChange;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

public class GroundItem extends PhysicsObject {
    private static final float MAX_AGE = 3 * 60; // 3 minutes
    private static TextureAtlas texAtl = null;
    private Sprite sprite = null;
    private final String textureName;
    private final Color color;
    private final float scale;
    private final Object createRealizedObjectPacket;
    private float age = 0;

    private boolean pickedUp = false;

    // server constructor
    public GroundItem(Vector2 position, int networkID, String textureName, Color color, float scale, Object createRealizedObjectPacket) {
        super(position, new Vector2(0, 0), new Vector2(0, 0), networkID);
        this.textureName = textureName;
        this.color = color;
        this.scale = scale;
        this.createRealizedObjectPacket = createRealizedObjectPacket;
    }

    // client constructor
    public GroundItem(CreateGroundItem packet) {
        super(packet.x, packet.y, 0, 0, 0, 0, packet.netId);
        createRealizedObjectPacket = packet.realizedObject;
        textureName = packet.textureName;
        color = new Color(packet.r, packet.g, packet.b, 1);
        scale = packet.scale;
        System.out.println("Ground item made on client");
        loadTexture();
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateGroundItem(position.x, position.y, networkID, color.r, color.g, color.b,
                scale, textureName, createRealizedObjectPacket));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        // no updates for GroundItem
    }

    @Override
    public void run(float dt, GameServer server) {
        age += dt;
        if (age > MAX_AGE) {
            server.removeObject(networkID);
        }
    }

    public void tryPickup(GameServer gameServer, int retrievingNetId) {
        if (!pickedUp) {
            GameObject realizedObject = null;
            if (createRealizedObjectPacket instanceof CreateInventoryGun) {
                realizedObject = new Gun(((CreateInventoryGun) createRealizedObjectPacket).info,
                        retrievingNetId, gameServer.getGameWorld().getNewNetID());
            }

            if (realizedObject != null) {
                pickedUp = true;
                gameServer.spawnAndSendGameObject(realizedObject);
                // inventory update
                InventoryChange ic = new InventoryChange(retrievingNetId, -1, realizedObject.getNetworkID(), true);
                // transmit
                gameServer.processAndSendInventoryUpdate(ic);
                // remove
                gameServer.removeObject(networkID);
            }
        }
    }

    public boolean collidingWithRectangle(Rectangle rect) {
        return sprite.getBoundingRectangle().overlaps(rect);
    }

    public boolean collidingWithPoint(float xPoint, float yPoint) {
        return sprite.getBoundingRectangle().contains(xPoint, yPoint);
    }

    private void loadTexture() {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }
        if (sprite == null) {
            TextureRegion region = texAtl.findRegion(textureName);
            sprite = new Sprite(region);
            sprite.setScale(scale);
            sprite.setColor(color);
            sprite.setOriginCenter();
        }
    }

    // deliberately not implementing Renderable here so that it doesn't get rendered with everything else
    // GroundItems should be rendered first so that they are behind everything
    public void draw(SpriteBatch sb) {
        sprite.setOriginBasedPosition(position.x, position.y);
        sprite.draw(sb);
    }
}
