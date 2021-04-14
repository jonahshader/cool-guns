package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.gunstuff.AttackInfo;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.gameobjects.shieldstuff.Shield;
import sophomoreproject.game.interfaces.CollisionReceiver;
import sophomoreproject.game.interfaces.Renderable;
import sophomoreproject.game.interfaces.Shadow;
import sophomoreproject.game.packets.CreatePlayer;
import sophomoreproject.game.packets.InventoryChange;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.packets.UpdatePlayer;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.singletons.StatsBarRenderer;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.utilites.RendingUtilities;

import java.util.ArrayList;

public class Player extends PhysicsObject implements Renderable, CollisionReceiver, Shadow {

    private static TextureAtlas texAtl = null;
    private static TextureRegion[] textures = null;
    private static Sprite shadow;

    private static final Vector2 PLAYER_SIZE = new Vector2(1.5f, 1.5f);
    public static final int STAMINA_MAX = 1;
    public static final float STAMINA_REGEN_PER_SECOND = .1f;
    public static final int BASE_MAX_HEALTH = 5;

    private final String username;

    private Vector2 lookDirection = new Vector2();
    private Vector2 barPos = new Vector2();
    private ArrayList<Integer> inventory = new ArrayList<>();
    private Rectangle hitbox;
    private int inventorySize = 8;

    private int accountId;

    private int health = BASE_MAX_HEALTH;
    private int maxHealth = BASE_MAX_HEALTH;
    private int shield = 0;
    private int maxShield = 0;
    private float stamina = STAMINA_MAX;

    private boolean queueAttacked = false;

    private ArrayList<StatsBarRenderer.StatsBarInfo> bars;
    private StatsBarRenderer.StatsBarInfo healthBar;
    private StatsBarRenderer.StatsBarInfo shieldBar;
    private StatsBarRenderer.StatsBarInfo staminaBar;
    private StatsBarRenderer.StatsBarInfo armorBar;

    //Server side constructor
    public Player(Vector2 position, int accountId, int networkID, String username) {
        super(position, new Vector2(0,0), new Vector2(0,0), networkID);
        this.accountId = accountId;
        this.username = username;
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;

        // create empty inventory
        for (int i = 0; i < inventorySize; ++i) {
            inventory.add(null);
        }
        hitbox = new Rectangle(position.x - PLAYER_SIZE.x * 8, position.y - PLAYER_SIZE.y * 8, PLAYER_SIZE.x * 16, PLAYER_SIZE.y * 16);
    }

    //Client side constructor
    public Player(CreatePlayer packet) {
        super(packet.u.x, packet.u.y,
                packet.u.xVel, packet.u.yVel,
                packet.u.xAccel, packet.u.yAccel, packet.u.netID);
        this.accountId = packet.accountId;
        this.username = packet.username;
        this.health = packet.health;
        this.maxHealth = packet.maxHealth;
        this.shield = packet.shield;
        this.maxShield = packet.maxShield;
        this.stamina = packet.stamina;

        // populate inventory from inventory packets
        inventory.addAll(packet.inventoryItems);

        loadTextures();
        hitbox = new Rectangle(position.x - PLAYER_SIZE.x * 8, position.y - PLAYER_SIZE.y * 8, PLAYER_SIZE.x * 16, PLAYER_SIZE.y * 16);
        updateFrequency = ServerUpdateFrequency.SEND_ONLY;

        bars = new ArrayList<>();
        healthBar = new StatsBarRenderer.StatsBarInfo(health, maxHealth, StatsBarRenderer.HEALTH_BAR_COLOR, "Health");
        shieldBar = new StatsBarRenderer.StatsBarInfo(shield,maxShield, StatsBarRenderer.SHIELD_BAR_COLOR, "Shield");
        staminaBar = new StatsBarRenderer.StatsBarInfo((int)Math.ceil(stamina * 100),STAMINA_MAX * 100, StatsBarRenderer.STAMINA_BAR_COLOR, "Stamina");
//        armorBar = new StatsBarRenderer.StatsBarInfo(6,15, StatsBarRenderer.ARMOR_BAR_COLOR);
        bars.add(healthBar);
        bars.add(shieldBar);
        bars.add(staminaBar);
//        bars.add(armorBar);
    }

    @Override
    public void updatePhysics(float dt) {
        super.updatePhysics(dt);
        hitbox.setPosition(position.x - PLAYER_SIZE.x * 8, position.y - PLAYER_SIZE.y * 8);
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {
        createPacketBuffer.add(new CreatePlayer(this));
    }

    @Override
    public void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer) {
        super.addUpdatePacketToBuffer(updatePacketBuffer);
        updatePacketBuffer.add(new UpdatePlayer(networkID, lookDirection.x, lookDirection.y, health, maxHealth, shield, maxShield, stamina));
    }

    @Override
    public void updateFromPacket(UpdatePhysicsObject packet) {
        packet.xAccel = 0;
        packet.yAccel = 0; // set acceleration to zero, should make game appear less laggy when the connection is bad
        super.updateFromPacket(packet);
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        // assume object is of type
        UpdatePlayer packet = (UpdatePlayer) updatePacket;
        lookDirection.set(packet.xLook, packet.yLook);
        health = packet.health;
        maxHealth = packet.maxHealth;
        shield = packet.shield;
        maxShield = packet.maxShield;
        stamina = packet.stamina;
    }

    public void run(float dt, GameServer server) {
        // die lol
        if (health <= 0) {
            // empty inventory
            for (Integer i : inventory) if (i != null) {
                server.removeObject(i);
                server.processAndSendInventoryUpdate(new InventoryChange(networkID, -1, i, false));
            }

            // reset parameters
            position.set(0, 0);
            velocity.set(0, 0);
            acceleration.set(0, 0);
            health = BASE_MAX_HEALTH;

            // give starter gun
            GunInfo starterGunInfo = new GunInfo();
            starterGunInfo.loadStarterGun();
            Gun starterGun = new Gun(starterGunInfo, networkID, server.getGameWorld().getNewNetID());
            server.spawnAndSendGameObject(starterGun);
            server.processAndSendInventoryUpdate(new InventoryChange(networkID, 0, starterGun.getNetworkID(), true));

            // force update to send new position, velocity, accel, etc
            server.queueForceUpdate(networkID);
        }
    }

    @Override
    public void draw(float dt, SpriteBatch sb, ShapeRenderer sr) {
        // update bars
        healthBar.value = health;
        healthBar.maxValue = maxHealth;
        shieldBar.value = shield;
        shieldBar.maxValue = maxShield;
        staminaBar.value = (int)Math.ceil(stamina * 100);
        staminaBar.maxValue = STAMINA_MAX * 100;
        // render stuff
        RendingUtilities.renderCharacter(position, lookDirection, PLAYER_SIZE, sb, textures);
        TextDisplay.getInstance().drawTextInWorld(sb, username, position.x, position.y - 24, .25f, new Color(1f, 1f, 1f, 1f));
        barPos.set(position);
        barPos.y += (PLAYER_SIZE.y / 2)* textures[6].getRegionHeight();
        StatsBarRenderer.getInstance().drawStatsBarsInWorld(sb,barPos,bars, false);
    }

    public void updateLookDirection(Vector2 lookDirection) {
        this.lookDirection.set(lookDirection);
    }

    public int getAccountId() {
        return accountId;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
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

            shadow = new Sprite(texAtl.findRegion("shadow"));
            shadow.setScale(2, 1);
            shadow.setOriginCenter();
            shadow.setColor(1, 1, 1, .8f);
        }
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Integer> getInventory() {
        return inventory;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void addToInventory(int itemNetID, int itemIndex) {
        boolean success = false;
        if (itemIndex > 0) {
            if (inventory.get(itemIndex) == null) {
                inventory.set(itemIndex, itemNetID);
                success = true;
            }
        } else {
            // put in first empty slot
            for (int i = 0; i < inventory.size(); ++i) {
                if (inventory.get(i) == null) {
                    inventory.set(i, itemNetID);
                    success = true;
                    break;
                }
            }
        }
        if (!success)
            System.out.println("WARNING: failed to put item into player inventory!");
    }

    public void removeFromInventory(int itemNetID) {
        boolean success = false;
        for (int i = 0; i < inventory.size(); ++i) {
            if (inventory.get(i) != null && inventory.get(i) == itemNetID) {
                inventory.set(i, null);
                success = true;
                break;
            }
        }
        if (!success)
            System.out.println("WARNING: tried removing inventory item that doesn't exist in player inventory!");
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getShield() {
        return shield;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public float getStamina() {
        return stamina;
    }

    public int getMaxStamina() {
        return STAMINA_MAX * 100;
    }

    public boolean isJustAttacked() {
        if (queueAttacked) {
            queueAttacked = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getRadius() {
        return 8;
    }

    @Override
    public CollisionGroup getCollisionGroup() {
        return CollisionGroup.PLAYER;
    }

    @Override
    public boolean checkCollidingGroup(CollisionGroup otherCollisionGroup) {
        return otherCollisionGroup == CollisionGroup.ENEMY;
    }

    @Override
    public int receiveAttack(AttackInfo attack, int attackerNetID) {
        // TODO: shield
        int shieldLeftBeforeDamage = shield;
        int healthLeftBeforeDamage = health;

        shield -= Math.round(attack.damage);
        if (shield < 0) {
            health += shield;
            shield = 0;
        }
        if (health < 0) {
            health = 0;
        }

        queueAttacked = true;
        velocity.add(attack.xKnockback, attack.yKnockback);

        return (healthLeftBeforeDamage - health) + (shieldLeftBeforeDamage - shield);
    }

    @Override
    public void drawShadow(SpriteBatch sb) {
        shadow.setOriginBasedPosition(position.x, position.y - 12);
        shadow.draw(sb);
    }
}
