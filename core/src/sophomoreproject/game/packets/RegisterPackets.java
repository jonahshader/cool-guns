package sophomoreproject.game.packets;

import com.esotericsoftware.kryo.Kryo;
import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;

import java.util.ArrayList;

public class RegisterPackets {
    // all packet objects must be registered here so that the client and server are
    // aware of the possible packets that can be sent
    public static void registerPackets(Kryo kryo) {
        kryo.register(CreateBullet.class);
        kryo.register(CreatePlayer.class);
        kryo.register(CreateSleeping.class);
        kryo.register(CreateTestObject.class);
        kryo.register(RemoveObject.class);
        kryo.register(ReplyAccountEvent.AccountEvent.class);
        kryo.register(ReplyAccountEvent.class);
        kryo.register(RequestGameData.class);
        kryo.register(RequestLogin.class);
        kryo.register(RequestNewAccount.class);
        kryo.register(UpdatePhysicsObject.class);
        kryo.register(UpdateSleepState.class);
    }
}
