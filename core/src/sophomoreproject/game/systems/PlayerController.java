package sophomoreproject.game.systems;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;

// TODO: https://www.gamedevelopment.blog/full-libgdx-game-tutorial-input-controller/
// This class will have all of the controls for the player and the gun

public final class PlayerController implements InputProcessor {
    private static PlayerController instance;
    private Player player = null;
    private Camera cam = null;
    public boolean left,right,up,down,shift;
    public boolean isMouse1Down, isMouse2Down;
    public boolean isDragged;
    public Vector2 mouseLocation = new Vector2();

    public final float PLAYER_ACCELERATION = 1000;
    public final float PLAYER_WALK_SPEED = 100;
    public final float PLAYER_SPRINT_SPEED = 200;
//    public final float FRICTION = 420;

    private  PlayerController() {
    }

    public static PlayerController getInstance() {
        if (instance == null)
            instance = new PlayerController();
        return instance;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public void run(float dt) {
        if (player != null && cam != null) {
//            if (player.velocity.len() > PLAYER_TOP_SPEED) {
//                player.velocity.nor().scl(PLAYER_TOP_SPEED);
//
//            }

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
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}

