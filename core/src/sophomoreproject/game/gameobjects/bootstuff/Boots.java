package sophomoreproject.game.gameobjects.bootstuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.packets.CreateBoots;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;



public class Boots extends Item  {
    private final BootsInfo info;

    private static TextureAtlas texAtl = null;
    private Sprite bootsSprite = null;
    private Sprite bootsIcon = null;

    //Client Constructor
    public Boots(CreateBoots packet) {
        this.info = packet.info;
        this.ownerNetId = packet.ownerNetId;
        this.networkID = packet.netId;

        loadTextures();

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    //Server Constructor
    public Boots(BootsInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.networkID = netId;

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateBoots(this));
    }

    @Override
    public void run(float dt, GameServer server) { }

    @Override
    public void renderIcon(SpriteBatch sb, float size, float x, float y) {
        float widthToHeight = bootsIcon.getRegionHeight() / (float) bootsIcon.getRegionWidth();
        bootsIcon.setSize(size, size * widthToHeight);
        bootsIcon.setPosition(x, y);
        bootsIcon.setColor(info.r,info.g,info.b,1);
        bootsIcon.draw(sb);
    }

    @Override
    public GroundItem toGroundItem(GameServer server) {
        return new GroundItem(new Vector2(position), server.getGameWorld().getNewNetID(), info.getTextureName(),
                new Color(info.r,info.g,info.b,1), 1f,
                new CreateBoots(info, -1, server.getGameWorld().getNewNetID()));
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) { }

    public BootsInfo getInfo() {
        return info;
    }

    public int getOwnerNetId() {
        return ownerNetId;
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }
        if (bootsSprite == null) {
            bootsIcon = new Sprite(texAtl.findRegion(info.getTextureName()));
            bootsIcon.setColor(info.r, info.g, info.b, 1f);
            bootsSprite = new Sprite(bootsIcon);
            bootsSprite.setOriginCenter();
        }
    }

    @Override
    public void manualReload() { }

    @Override
    public void updateItem(float dt, boolean usedOnce, boolean using, Vector2 angle, Player player) { }
}
