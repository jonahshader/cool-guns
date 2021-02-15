package sophomoreproject.game.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonetty.kryo.KryoNetty;

public class RegisterPackets {
    // all packet objects must be registered here so that the client and server are
    // aware of the possible packets that can be sent
//    public static void registerPackets(Kryo kryo) {
//        kryo.register(CreatePlayer.class);
//        kryo.register(CreateTestObject.class);
//        kryo.register(ReplyAccountEvent.AccountEvent.class);
//        kryo.register(ReplyAccountEvent.class);
//        kryo.register(RequestGameData.class);
//        kryo.register(RequestLogin.class);
//        kryo.register(RequestNewAccount.class);
//        kryo.register(UpdatePhysicsObject.class);
//    }

    public static KryoNetty makeKryoNetty() {
        KryoNetty kryoNetty = new KryoNetty()
                .register(CreatePlayer.class,
                        CreateTestObject.class,
                        ReplyAccountEvent.AccountEvent.class,
                        ReplyAccountEvent.class,
                        RequestGameData.class,
                        RequestLogin.class,
                        RequestNewAccount.class,
                        UpdatePhysicsObject.class);
        return kryoNetty;
    }

}
