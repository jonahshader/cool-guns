package sophomoreproject.game.gameobjects.gunstuff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.GameObject;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;

import static sophomoreproject.game.singletons.LocalRandom.RAND;

//This is owned by a player or it is in an inventory (like a shop).

public class Gun extends GameObject implements Renderable {


    public enum FiringMode {
        AUTO,
        SEMI_AUTO,
        BURST,
        CHARGE
    }

    public enum GunType {
        PISTOL,
        RIFLE,
        SMG,
        LMG,
        SHOTGUN
    }

    //Client Constructor
    public Gun(CreateInventoryGun packet, int ownerNetId) {
        this.ownerNetId = ownerNetId;
        this.info = packet.info;
        loadTextures();
    }

    //Server Constructor
    public Gun(GunInfo info) {
        this.info = info;
    }

    private GunInfo info;

    private static TextureAtlas texAtl = null;
    private Sprite gunSprite = null;
    private float firingTimer;
    private final ArrayList<Object> bulletPackets = new ArrayList<>();
    private Vector2 angle = new Vector2();
    private Vector2 position = new Vector2();
    private int ownerNetId;

    @Override
    public void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer) {

    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {

    }

    @Override
    public void receiveUpdate(Object updatePacket) {

    }

    @Override
    public void run(float dt) {
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        gunSprite.setFlip(false,angle.x < 0);
        gunSprite.setRotation(angle.angleDeg());
        gunSprite.setPosition(position.x, position.y);
    }

    public void localRun (float dt, boolean shooting, boolean oneShot, Vector2 angle, Player player) {
        this.angle = angle;
        this.position = new Vector2(player.position);
        Vector2 offset = new Vector2(angle);
        offset.scl(info.gunHoldRadius);
        position.add(offset);


        if (firingTimer > 0) {
            firingTimer = firingTimer - dt;
        } else if (info.firingMode == FiringMode.AUTO && shooting){
            shoot(angle, player);
        } else if (info.firingMode == FiringMode.SEMI_AUTO && oneShot) {
            shoot(angle, player);
        }
    }

    private void shoot(Vector2 angle, Player player) {
        firingTimer = firingTimer + info.fireRate;
        Vector2 baseVelocity = new Vector2(angle);
        baseVelocity.scl(info.bulletSpeed);


        for (int b = 0; b < info.bulletsPerShot; b++) {
            Vector2 uniqueVel = new Vector2(baseVelocity);
            uniqueVel.rotateRad((float) RAND.nextGaussian()*info.spread)
                    .scl((float) (info.bulletSpeed*Math.pow(Math.E, RAND.nextGaussian() * info.bulletSpeedVariation)));
            bulletPackets.add(new CreateBullet(new UpdatePhysicsObject(-1, player.position.x, player.position.y, uniqueVel.x, uniqueVel.y,
                    0f, 0f), player.getNetworkID(), info.bulletSize));
        }
        ClientNetwork.getInstance().sendAllPackets(bulletPackets);
        bulletPackets.clear();
    }

    private void loadTextures () {
        if (texAtl == null) {
            texAtl = CustomAssetManager.getInstance().manager.get("graphics/spritesheets/sprites.atlas");
            gunSprite = new Sprite(texAtl.findRegion("default_gun"));
        }
    }

    public GunInfo getInfo() {
        return info;
    }

    public int getOwnerNetId() {
        return ownerNetId;
    }
}
