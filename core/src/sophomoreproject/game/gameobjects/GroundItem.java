package sophomoreproject.game.gameobjects;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;

public class GroundItem extends PhysicsObject {
    public GroundItem(Vector2 position, Vector2 velocity, Vector2 acceleration, int networkID) {
        super(position, velocity, acceleration, networkID);
    }

    public GroundItem(float x, float y, float xVel, float yVel, float xAccel, float yAccel, int networkID) {
        super(x, y, xVel, yVel, xAccel, yAccel, networkID);
    }

    @Override
    public void addCreatePacketToBuffer(ArrayList<Object> createPacketBuffer) {

    }

    @Override
    public void receiveUpdate(Object updatePacket) {

    }

    @Override
    public void run(float dt, GameServer server) {

    }
}
