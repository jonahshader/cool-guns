package sophomoreproject.game.packets;

import sophomoreproject.game.gameobjects.TestObject;

public class CreateTestObject {
    public UpdatePhysicsObject u;
    public int xFactor;
    public int yFactor;

    public CreateTestObject(TestObject toSend) {
        u = new UpdatePhysicsObject(toSend.getNetworkID(), toSend.position, toSend.velocity, toSend.acceleration);
        xFactor = toSend.getxFactor();
        yFactor = toSend.getyFactor();
    }

    public CreateTestObject(){} // no arg constructor for KryoNet internal usage
}
