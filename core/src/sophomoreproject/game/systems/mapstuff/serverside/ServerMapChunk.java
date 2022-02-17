package sophomoreproject.game.systems.mapstuff.serverside;

import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.interfaces.CollisionReceiver;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.mapstuff.MapChunk;
import sophomoreproject.game.systems.mapstuff.MapGenerator;
import sophomoreproject.game.systems.mapstuff.Spawner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ServerMapChunk {
    private final ArrayList<Spawner> spawners;
    private final HashMap<Integer, PhysicsObject> idToContainedObject; // map of objects that exist in this chunk
    private final HashMap<Integer, Player> idToContainedPlayer;
    private final HashMap<Integer, CollisionReceiver> idToContainedCollisionReceiver;
    private final int x;
    private final int y;

    public ServerMapChunk(MapGenerator mapGen, int x, int y) {
        this.x = x;
        this.y = y;
        spawners = MapChunk.getSpawners(x, y, mapGen);

        idToContainedObject = new HashMap<>();
        idToContainedPlayer = new HashMap<>();
        idToContainedCollisionReceiver = new HashMap<>();
    }

    public void run(float dt, GameServer server) {
        // run spawners
        spawners.forEach(spawner -> spawner.run(dt, server));
    }

    public void resetContainedObjects() {
        idToContainedObject.clear();
        idToContainedPlayer.clear();
        idToContainedCollisionReceiver.clear();
    }

    public void registerContainedObject(PhysicsObject obj) {
        idToContainedObject.put(obj.getNetworkID(), obj);
        if (obj instanceof Player) {
            idToContainedPlayer.put(obj.getNetworkID(), (Player)obj);
        }
        if (obj instanceof CollisionReceiver) {
            idToContainedCollisionReceiver.put(obj.getNetworkID(), (CollisionReceiver) obj);
        }
    }

    public String getKey() {
        return x + " " + y;
    }

    public boolean shouldUnload() {
        return idToContainedPlayer.size() == 0;
    }

    public Collection<PhysicsObject> getContainedObjects() {
        return idToContainedObject.values();
    }

    public Collection<Player> getContainedPlayers() {
        return idToContainedPlayer.values();
    }

    public Collection<CollisionReceiver> getContainedCollisionReceivers() {
        return idToContainedCollisionReceiver.values();
    }
}
