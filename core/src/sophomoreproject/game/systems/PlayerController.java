package sophomoreproject.game.systems;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.interfaces.Item;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.CreateBullet;
import sophomoreproject.game.packets.UpdatePhysicsObject;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.utilites.MathUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;

// TODO: https://www.gamedevelopment.blog/full-libgdx-game-tutorial-input-controller/
// This class will have all of the controls for the player and the gun

public final class PlayerController implements InputProcessor {
    private static PlayerController instance;
    private Player player = null;
    private GameWorld world = null;
    private Camera cam = null;
    public boolean left,right,up,down,shift;
    public boolean isMouse1Down, isMouse2Down;
    public boolean isDragged;
    public Vector2 mouseLocation = new Vector2();
    private int equippedItemIndex;

    private TextDisplay.TextEntry accountIDString;
    private TextDisplay.TextEntry playerNetIDString;
    private TextDisplay.TextEntry fpsString;
    private TextDisplay.TextEntry clipString;


    private final ArrayList<Object> updatePacketArray = new ArrayList<>();

    public final float PLAYER_ACCELERATION = 1500;
    public final float PLAYER_WALK_SPEED = 100;
    public final float PLAYER_SPRINT_SPEED = 10000;
//    public final float FRICTION = 420;

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
                desiredSpeed.nor().scl(PLAYER_WALK_SPEED);
            }
            if (shift) {
                desiredSpeed.nor().scl(PLAYER_SPRINT_SPEED);
            }


            Vector2 speedDifference = new Vector2(desiredSpeed);
            Vector2 tempVel = new Vector2(player.velocity);

            speedDifference.sub(player.velocity);
            speedDifference.nor().scl(PLAYER_ACCELERATION);

            if (!playerMoving && speedDifference.len()*dt > player.velocity.len()) {
                player.acceleration.set(0,0);
                player.velocity.set(0,0);
            } else {
                player.acceleration.set(speedDifference);
            }

            cam.position.x = player.position.x;
            cam.position.y = player.position.y;


            sendUpdatePacketToServer();
/*
            if (Gdx.input.justTouched()) {
                Vector3 mouseWorldCoords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
                Vector2 mouseWorldCoords2D = new Vector2(mouseWorldCoords.x, mouseWorldCoords.y);
                Vector2 playerToMouse = mouseWorldCoords2D.sub(player.position);
                playerToMouse.nor();
                playerToMouse.scl(500);
                CreateBullet b = new CreateBullet(new UpdatePhysicsObject(-1, player.position.x, player.position.y, playerToMouse.x, playerToMouse.y,
                        0f, 0f), player.getNetworkID(), 3f);
                ClientNetwork.getInstance().sendPacket(b);
            }
*/

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
            }
        }

        System.out.println("Item index " + equippedItemIndex + " equipped.");
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
        changeEquippedItem(-Math.round(amountY) + equippedItemIndex);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}

