package sophomoreproject.game.gameobjects.shieldstuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.bootstuff.BootsInfo;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.packets.CreateBoots;
import sophomoreproject.game.packets.CreateShield;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

public class Shield extends Item  {
    private final ShieldInfo info;

    private static TextureAtlas texAtl = null;
    private Sprite shieldSprite = null;
    private Sprite shieldIcon = null;

    //Client Constructor
    public Shield(CreateShield packet) {
        this.info = packet.info;
        this.ownerNetId = packet.ownerNetId;
        this.networkID = packet.netId;

        loadTextures();

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    //Server Constructor
    public Shield(ShieldInfo info, int ownerNetId, int netId) {
        this.info = info;
        this.ownerNetId = ownerNetId;
        this.networkID = netId;

        updateFrequency = ServerUpdateFrequency.SEND_ONLY;
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreateShield(this));
    }

    @Override
    public void run(float dt, GameServer server) { }

    @Override
    public void renderIcon(SpriteBatch sb, float size, float x, float y) {
        float widthToHeight = shieldIcon.getRegionHeight() / (float) shieldIcon.getRegionWidth();
        shieldIcon.setSize(size, size * widthToHeight);
        shieldIcon.setPosition(x, y);
        shieldIcon.setColor(info.r,info.g,info.b,1);
        shieldIcon.draw(sb);
    }

    @Override
    public GroundItem toGroundItem(GameServer server) {
        return new GroundItem(new Vector2(position), server.getGameWorld().getNewNetID(), info.getTextureName(),
                new Color(info.r,info.g,info.b,1), 1f,
                new CreateShield(info, -1, server.getGameWorld().getNewNetID()));
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) { }
    //Shield position needs to be updated

    public ShieldInfo getInfo() {
        return info;
    }

    public int getOwnerNetId() {
        return ownerNetId;
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
        }
        if (shieldSprite == null) {
            shieldIcon = new Sprite(texAtl.findRegion(info.getTextureName()));
            shieldIcon.setColor(info.r, info.g, info.b, 1f);
            shieldSprite = new Sprite(shieldIcon);
            shieldSprite.setOriginCenter();
        }
    }


//    @Override
//    public void updateItem(float dt, boolean usedOnce, boolean using, Vector2 angle, Player player) {
//        position.x = player.position.x;
//        position.y = player.position.y;
//    }

    @Override
    public void manualReload() { }
}
