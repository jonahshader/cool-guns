package sophomoreproject.game.systems;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.bootstuff.Boots;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.shieldstuff.Shield;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.RequestDropInventoryItem;
import sophomoreproject.game.packets.RequestPickupGroundItem;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.utilites.MathUtilities;

import java.util.ArrayList;

import static sophomoreproject.game.utilites.CharacterUtilities.accelerateTowardsTargetVelocity;

// TODO: https://www.gamedevelopment.blog/full-libgdx-game-tutorial-input-controller/
// This class will have all of the controls for the player and the gun

public final class PlayerController implements InputProcessor {
    private static final float SERVER_UPDATE_DELAY = 1/30f;
    private static PlayerController instance;
    private Player player = null;
    private GameWorld world = null;
    private Camera cam = null;
    public boolean left,right,up,down,shift;
    public boolean isMouse1Down, isMouse2Down;
    public boolean isDragged;
    public Vector2 mouseLocation = new Vector2();
    private static final float STAMINA_REGEN_RESUME_DELAY = 1.5f;
    private float staminaRegenResumeTimer = 0;

    private int equippedItemIndex;
    private float serverUpdateDelayTimer = 0;


    private TextDisplay.TextEntry accountIDString;
    private TextDisplay.TextEntry playerNetIDString;
    private TextDisplay.TextEntry fpsString;
    private TextDisplay.TextEntry clipString;


    private final ArrayList<Object> updatePacketArray = new ArrayList<>();

    public static final float BASE_PLAYER_ACCELERATION = 1200;
    public static final float BASE_PLAYER_WALK_SPEED = 80;
    private float currentPlayerAcceleration = BASE_PLAYER_ACCELERATION;
    private float currentPlayerWalkSpeed = BASE_PLAYER_WALK_SPEED;
    public final float PLAYER_SPRINT_SCALAR = 1.8f;
//    public final float FRICTION = 420;

    public float shieldRegenTimer = 0;
    private float shieldFloatBuffer = 0;


    private PlayerController() {
        accountIDString = new TextDisplay.TextEntry("temp");
        playerNetIDString = new TextDisplay.TextEntry("temp");
        fpsString = new TextDisplay.TextEntry("temp");
        clipString = new TextDisplay.TextEntry("temp");

        TextDisplay.getInstance().addHudText(accountIDString, TextDisplay.TextPosition.TOP_LEFT);
        TextDisplay.getInstance().addHudText(playerNetIDString, TextDisplay.TextPosition.TOP_LEFT);
        TextDisplay.getInstance().addHudText(fpsString, TextDisplay.TextPosition.TOP_LEFT);
        TextDisplay.getInstance().addHudText(clipString, TextDisplay.TextPosition.TOP);
    }

    public synchronized static PlayerController getInstance() {
        if (instance == null)
            instance = new PlayerController();
        return instance;
    }

    public void setPlayer(Player player) {
        this.player = player;

        accountIDString.entry = "Account ID: " + player.getAccountId();
        playerNetIDString.entry = "Net ID: " + player.getNetworkID();
    }

    public void setGameWorld(GameWorld world) {
        this.world = world;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public void run(float dt) {
        fpsString.entry = "FPS: " + Math.round(1/dt);
        if (player != null && cam != null) {

            updatePlayerStats(dt);

            player.acceleration.set(0,0);
            boolean playerMoving = false;
            Vector2 desiredSpeed = new Vector2();

            if (left) {
                playerMoving = true;
                desiredSpeed.x = -1;
            }
            if (right) {
                playerMoving = true;
                desiredSpeed.x = 1;
            }
            if (up) {
                playerMoving = true;
                desiredSpeed.y = 1;
            }
            if (down) {
                playerMoving = true;
                desiredSpeed.y = -1;
            }
            if (playerMoving) {
                desiredSpeed.nor().scl(currentPlayerWalkSpeed);
            }
            if (shift && player.getStamina() > 0) {
                desiredSpeed.scl(PLAYER_SPRINT_SCALAR);
                accelerateTowardsTargetVelocity(desiredSpeed, currentPlayerAcceleration * PLAYER_SPRINT_SCALAR, player, dt);
                staminaRegenResumeTimer = STAMINA_REGEN_RESUME_DELAY;
                player.setStamina(player.getStamina() - dt);
            } else {
                accelerateTowardsTargetVelocity(desiredSpeed, currentPlayerAcceleration, player, dt);
            }



//            Vector2 speedDifference = new Vector2(desiredSpeed);
//
//            speedDifference.sub(player.velocity);
//            speedDifference.nor().scl(PLAYER_ACCELERATION);
//
//            if (!playerMoving && speedDifference.len()*dt > player.velocity.len()) {
//                player.acceleration.set(0,0);
//                player.velocity.set(0,0);
//            } else {
//                player.acceleration.set(speedDifference);
//            }

            cam.position.x = player.position.x;
            cam.position.y = player.position.y;


            // update server periodically
            if (serverUpdateDelayTimer > 0) serverUpdateDelayTimer -= dt;
            if (serverUpdateDelayTimer <= 0) {
                sendUpdatePacketToServer();
                serverUpdateDelayTimer += SERVER_UPDATE_DELAY;
            }


            Vector3 mouseWorldCoords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
            Vector2 mouseWorldCoords2D = new Vector2(mouseWorldCoords.x, mouseWorldCoords.y);
            Vector2 playerToMouse = mouseWorldCoords2D.sub(player.position);
            playerToMouse.nor();
            player.updateLookDirection(playerToMouse);
            if (player.getInventory().get(equippedItemIndex) != null) {
                Object gameObj = world.getGameObjectFromID(player.getInventory().get(equippedItemIndex));
                if (gameObj != null) {
                    Item gameItem = (Item) gameObj;
                    gameItem.setEquipped(true);
                    gameItem.updateItem(dt,Gdx.input.justTouched() && isMouse1Down, isMouse1Down,
                            playerToMouse, player);

                    if (gameItem instanceof Gun) {
                        clipString.entry = "Clip: " + ((Gun)gameItem).getCurrentClip();
                    }
                } else {
                    // inventory item not found!
                    System.out.println("Player inventory item not found! Should never happen!");
                }
            }
        }
    }

    /**s
     * generates an update packet and sends it to the server to be redistributed
     */
    private void sendUpdatePacketToServer() {
        player.addUpdatePacketToBuffer(updatePacketArray);
        for (Integer item : player.getInventory()) {
            if (item != null) {
                Object gameObj = world.getGameObjectFromID(item);
                if (gameObj != null) {
                    Item gameItem = (Item) gameObj;
                    gameItem.addUpdatePacketToBuffer(updatePacketArray);
                } else {
                    System.out.println("Player inventory item not found! Should never happen!");
                }
            }
        }


        ClientNetwork.getInstance().sendAllPackets(updatePacketArray);
        updatePacketArray.clear();
    }

    private void changeEquippedItem(int newIndex) {
        if (player.getInventory().get(equippedItemIndex) != null) {
            Object gameObj = world.getGameObjectFromID(player.getInventory().get(equippedItemIndex));
            if (gameObj != null) {
                Item gameItem = (Item) gameObj;
                gameItem.setEquipped(false);
            }
        }

        equippedItemIndex = newIndex;
        equippedItemIndex = MathUtilities.wrap(equippedItemIndex, 0, player.getInventorySize());
        if (player.getInventory().get(equippedItemIndex) != null) {
            Object gameObj = world.getGameObjectFromID(player.getInventory().get(equippedItemIndex));
            if (gameObj != null) {
                Item gameItem = (Item) gameObj;
                gameItem.setEquipped(true);
                if (gameItem instanceof Gun) {
                    System.out.println("Equipped gun score: " + ((Gun) gameItem).getInfo().getGeneralScore());
                }
            }
        }

        System.out.println("Item index " + equippedItemIndex + " equipped.");
    }

    private void updatePlayerStats(float dt) {
        // loop through inventory to calculate player stats
        currentPlayerWalkSpeed = BASE_PLAYER_WALK_SPEED;
        currentPlayerAcceleration = BASE_PLAYER_ACCELERATION;
        player.setMaxHealth(Player.BASE_MAX_HEALTH);
        player.setMaxShield(0);
        float shieldRegenRate = 0;
        float shieldDelay = 0;
        int numShields = 0;

        for (int i = 0; i < player.getInventorySize(); ++i) {
            if (player.getInventory().get(i) != null) {
                Item item = (Item)world.getGameObjectFromID(player.getInventory().get(i));
                if (item != null) {
                    if (item instanceof Boots) {
                        currentPlayerWalkSpeed += ((Boots)item).getInfo().speed;
                        currentPlayerAcceleration += ((Boots)item).getInfo().acceleration;
                    }
                    else if (item instanceof Shield) {
                        player.setMaxShield(Math.round(player.getMaxShield()+ ((Shield)item).getInfo().capacity));
                        player.setMaxHealth(Math.round(player.getMaxHealth()+ ((Shield)item).getInfo().health));
                        shieldRegenRate += ((Shield) item).getInfo().regenRate;
                        numShields++;
                        shieldDelay += ((Shield) item).getInfo().regenDelay;
                    }
                }
            }
        }

        if (numShields > 0) {
            shieldDelay /= numShields;
        }
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
        if (staminaRegenResumeTimer <= 0) {
            if (player.getStamina() < Player.STAMINA_MAX) {
                player.setStamina(player.getStamina() + Player.STAMINA_REGEN_PER_SECOND * dt);
            }
        } else {
            staminaRegenResumeTimer -= dt;
        }

        if (player.isJustAttacked()) {
            shieldRegenTimer = 0;
        }
        if (shieldRegenTimer < shieldDelay) {
            shieldRegenTimer += dt;
        } else {
            shieldFloatBuffer += shieldRegenRate*dt;
            int shield = player.getShield();
            shield += (int) shieldFloatBuffer;
            shieldFloatBuffer -= (int) shieldFloatBuffer;
            if (shield > player.getMaxShield())
                shield = player.getMaxShield();
            player.setShield(shield);
        }


    }

    // Later we will have adjustable controls.
    @Override
    public boolean keyDown(int keycode) {
        boolean keyProc = false;
        switch (keycode)
        {
            case Keys.A:
                left = true;
                keyProc = true;
                break;
            case Keys.D:
                right = true;
                keyProc = true;
                break;
            case Keys.W:
                up = true;
                keyProc = true;
                break;
            case Keys.S:
                down = true;
                keyProc = true;
                break;
            case Keys.SHIFT_LEFT:
                shift = true;
                keyProc = true;
                break;
            case Keys.NUM_1:
                changeEquippedItem(0);
                keyProc = true;
                break;
            case Keys.NUM_2:
                changeEquippedItem(1);
                keyProc = true;
                break;
            case Keys.NUM_3:
                changeEquippedItem(2);
                keyProc = true;
                break;
            case Keys.NUM_4:
                changeEquippedItem(3);
                keyProc = true;
                break;
            case Keys.NUM_5:
                changeEquippedItem(4);
                keyProc = true;
                break;
            case Keys.NUM_6:
                changeEquippedItem(5);
                keyProc = true;
                break;
            case Keys.NUM_7:
                changeEquippedItem(6);
                keyProc = true;
                break;
            case Keys.NUM_8:
                changeEquippedItem(7);
                keyProc = true;
                break;
            case Keys.R:
                Item gun = (Item) world.getGameObjectFromID(player.getInventory().get(equippedItemIndex));
                if (gun != null)
                    gun.manualReload();
                keyProc = true;
                break;
            case Keys.F:
                // try pickup
                int pickupAttempts = 0;
                int emptySlots = 0;
                for (int i = 0; i < player.getInventory().size(); ++i) emptySlots += player.getInventory().get(i) == null ? 1 : 0;
                for (GroundItem g : world.getGroundItems()) {
                    if (pickupAttempts >= emptySlots)
                        break;
                    if (MathUtilities.circleCollisionDetection(player.position.x, player.position.y, 8f, g.position.x, g.position.y, 8f)) {
                        ClientNetwork.getInstance().sendPacket(new RequestPickupGroundItem(player.getNetworkID(), g.getNetworkID()));
                        ++pickupAttempts;
                    }
                }
                System.out.println("Tried picking up " + pickupAttempts + " items.");
                break;
            case Keys.G:
                // try drop
                if (player.getInventory().get(equippedItemIndex) != null) {
                    ClientNetwork.getInstance().sendPacket(new RequestDropInventoryItem(player.getNetworkID(), player.getInventory().get(equippedItemIndex)));
                }
                break;
        }
        return keyProc;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProc = false;
        switch (keycode)
        {
            case Keys.A:
                left = false;
                keyProc = true;
                break;
            case Keys.D:
                right = false;
                keyProc = true;
                break;
            case Keys.W:
                up = false;
                keyProc = true;
                break;
            case Keys.S:
                down = false;
                keyProc = true;
                break;
            case Keys.SHIFT_LEFT:
                shift = false;
                keyProc = true;
                break;
        }
        return keyProc;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0){
            isMouse1Down = true;
        }else if(button == 1){
            isMouse2Down = true;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        if(button == 0){
            isMouse1Down = false;
        }else if(button == 1){
            isMouse2Down = false;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        changeEquippedItem(Math.round(amountY) + equippedItemIndex);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public int getInventorySize() {
        return player.getInventorySize();
    }

    public boolean playerIsNull() {
        return player == null;
    }

    public int getEquippedItemIndex() {
        return equippedItemIndex;
    }

    public Player getPlayer() {
        return player;
    }
}

