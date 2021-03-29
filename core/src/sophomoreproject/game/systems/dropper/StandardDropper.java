package sophomoreproject.game.systems.dropper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.GroundItem;
import sophomoreproject.game.gameobjects.gunstuff.Gun;
import sophomoreproject.game.gameobjects.gunstuff.GunInfo;
import sophomoreproject.game.packets.CreateInventoryGun;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.systems.GameServer;

public class StandardDropper {
    private ItemDropper dropper;

    public StandardDropper() {
        dropper = new ItemDropper();
        dropper.addDropAction(new DropAction(.1f) {
            @Override
            public void dropItem(GameServer server, Vector2 pos, float difficulty) {
                GunInfo gunInfo = new GunInfo();
                gunInfo.r = (float) Math.sqrt(LocalRandom.RAND.nextFloat());
                gunInfo.g = (float) Math.sqrt(LocalRandom.RAND.nextFloat());
                gunInfo.b = (float) Math.sqrt(LocalRandom.RAND.nextFloat());
                gunInfo.loadGunTypeDefaults(Gun.GunType.values()[LocalRandom.RAND.nextInt(Gun.GunType.values().length)], true);
                gunInfo.randomize(.5f);
                gunInfo.scaleScore((float)Math.sqrt(difficulty * .2));
                server.spawnAndSendGameObject(new GroundItem(new Vector2(pos), server.getGameWorld().getNewNetID(),
                        gunInfo.getTextureName(), new Color(gunInfo.r, gunInfo.g, gunInfo.b, 1), 1f,
                        new CreateInventoryGun(gunInfo, -1, server.getGameWorld().getNewNetID())));

            }
        });

    }

    public boolean tryDropItem(GameServer server, Vector2 pos, float difficulty) {
        return dropper.tryDropItem(server, pos, difficulty);
    }
}
