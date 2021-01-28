package sophomoreproject.game.packets;

import com.esotericsoftware.kryo.Kryo;

public class RegisterPackets {
    // all packet objects must be registered here so that the client and server are
    // aware of the possible packets that can be sent
    public static void registerPackets(Kryo kryo) {
        kryo.register(RequestGameData.class);
    }
}
