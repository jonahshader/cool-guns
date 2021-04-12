package sophomoreproject.game.packets;

import com.esotericsoftware.kryo.Kryo;
import sophomoreproject.game.gameobjects.bootstuff.BootsInfo;
import sophomoreproject.game.gameobjects.enemystuff.EnemyInfo;
import sophomoreproject.game.gameobjects.gunstuff.AttackInfo;
import sophomoreproject.game.gameobjects.gunstuff.Bullet;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.interfaces.CollisionReceiver;

import java.util.ArrayList;

public class RegisterPackets {
    // all packet objects must be registered here so that the client and server are
    // aware of the possible packets that can be sent
    public static void registerPackets(Kryo kryo) {
        kryo.register(AttackInfo.class);
        kryo.register(AttackPlayer.class);
        kryo.register(BootsInfo.class);
        kryo.register(CreateBoots.class);
        kryo.register(CreateBullet.class);
        kryo.register(CreateEnemy.class);
        kryo.register(CreateGroundItem.class);
        kryo.register(CreateInventoryGun.class);
        kryo.register(CreatePlayer.class);
        kryo.register(CreateShield.class);
        kryo.register(CreateSleeping.class);
        kryo.register(CreateTestObject.class);
        kryo.register(InventoryChange.class);
        kryo.register(RemoveObject.class);
        kryo.register(ReplyAccountEvent.AccountEvent.class);
        kryo.register(ReplyAccountEvent.class);
        kryo.register(RequestDropInventoryItem.class);
        kryo.register(RequestGameData.class);
        kryo.register(RequestLogin.class);
        kryo.register(RequestNewAccount.class);
        kryo.register(RequestPickupGroundItem.class);
        kryo.register(UpdateEnemy.class);
        kryo.register(UpdateItem.class);
        kryo.register(UpdatePhysicsObject.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(UpdateSleepState.class);

        kryo.register(GunInfo.class);
        kryo.register(Gun.FiringMode.class);
        kryo.register(Gun.GunType.class);
        kryo.register(Bullet.BulletType.class);

        kryo.register(EnemyInfo.class);

        kryo.register(ArrayList.class);
    }
}
