package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.utilites.RendingUtilities;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.gameobjects.gunstuff.Gun;

import java.util.ArrayList;

public class Player extends PhysicsObject implements Renderable{

    private static TextureAtlas texAtl = null;
    private static TextureRegion[] textures = null;

    private final Vector2 PLAYER_SIZE = new Vector2(1.5f, 1.5f);

    private final String username;

    private ArrayList<Item> inventory = new ArrayList<>();
    private int inventorySize = 8;

    private int accountId;

    //Server side constructor
    public Player(Vector2 position, int accountId, int networkID, String username, GameServer server) {
        super(position, new Vector2(0,0), new Vector2(0,0), networkID);
        this.accountId = accountId;
        this.username = username;
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;

        GunInfo gunInfo = new GunInfo();
        Gun gun = new Gun(gunInfo, networkID, server.getGameWorld().getNewNetID());
        server.spawnAndSendGameObject(gun);
        inventory.add(gun);

        for (int i = 1; i < inventorySize; ++i) {
            inventory.add(null);
        }
    }

    //Client side constructor
    public Player(CreatePlayer packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.accountId = packet.accountId;
        this.username = packet.username;

        // populate inventory from inventory packets
        for (Object invP : packet.inventoryPackets) {
            if (invP == null) {
                inventory.add(null);
            } else if (invP instanceof CreateInventoryGun) {
                inventory.add(new Gun((CreateInventoryGun) invP));
            }
        }

        loadTextures();
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

    public void run(float dt, GameServer server) {
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        RendingUtilities.renderCharacter(position, velocity, PLAYER_SIZE, sb, textures);
//        for (Item item : inventory) {
//            if (item != null && item.isEquipped()) {
//                item.draw(sb,sr);
//            }
//        }
        TextDisplay.getInstance().drawTextInWorld(sb, username, position.x, position.y + 24, .25f, new Color(1f, 1f, 1f, 1f));
    }

    public int getAccountId() {
        return accountId;
    }

    private void loadTextures () {
        if (texAtl == null) {
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

    public ArrayList<Item> getInventory() {
        return inventory;
    }
}
