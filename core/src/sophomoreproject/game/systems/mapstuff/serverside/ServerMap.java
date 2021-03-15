package sophomoreproject.game.systems.mapstuff.serverside;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.PhysicsObject;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.GameWorld;
import sophomoreproject.game.systems.mapstuff.MapChunk;
import sophomoreproject.game.systems.mapstuff.MapGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static sophomoreproject.game.systems.mapstuff.Map.LOAD_CHUNK_RADIUS;

public class ServerMap {
    private final HashMap<String, ServerMapChunk> keyToChunk = new HashMap<>();
    private final MapGenerator mapGen;
    private final GameServer gameServer;

    public ServerMap(MapGenerator mapGen, GameServer gameServer) {
        this.mapGen = mapGen;
        this.gameServer = gameServer;
    }

    //TODO: might need to lock and unlock world stuff in this method
    public void update() {
        // remove objects from chunks
        for (ServerMapChunk chunk : keyToChunk.values()) {
            chunk.resetContainedObjects();
        }
        // repopulate chunks with objects. players first so that surrounding chunks are loaded
        for (PhysicsObject obj : gameServer.getGameWorld().getPlayers()) {
            registerObject(obj);
        }
        // next do non-player objects
        for (PhysicsObject obj : gameServer.getGameWorld().getPhysicsObjects()) {
            if (!(obj instanceof Player)) {
                registerObject(obj);
            }
        }

        // unload unused maps
        ArrayList<String> chunksToUnload = new ArrayList<>();
        for (ServerMapChunk chunk : keyToChunk.values()) {
            if (chunk.shouldUnload()) {
                chunksToUnload.add(chunk.getKey());
            }
        }
        for (String s : chunksToUnload) {
            keyToChunk.remove(s);
        }
        chunksToUnload.clear();
    }

    public void run(float dt) {
        for (ServerMapChunk chunk : keyToChunk.values()) {
            chunk.run(dt, gameServer);
        }
    }

    public Collection<Player> getNearbyPlayers(Vector2 position) {
        ServerMapChunk chunk = keyToChunk.get(MapChunk.worldPosToKey(position));
        if (chunk == null) {
            return null;
        } else {
            return chunk.getContainedPlayers();
        }
    }

    public Collection<PhysicsObject> getNearbyPhysicsObjects(Vector2 position) {
        ServerMapChunk chunk = keyToChunk.get(MapChunk.worldPosToKey(position));
        if (chunk == null) {
            return null;
        } else {
            return chunk.getContainedObjects();
        }
    }

    private void registerObject(PhysicsObject obj) {
        int xChunk = obj.getChunkX();
        int yChunk = obj.getChunkY();

        if (obj instanceof Player) {
            for (int x = -LOAD_CHUNK_RADIUS; x <= LOAD_CHUNK_RADIUS; ++x) {
                for (int y = -LOAD_CHUNK_RADIUS; y <= LOAD_CHUNK_RADIUS; ++y) {
                    ServerMapChunk chunk;
                    // if this chunk is not loaded,
                    if (!keyToChunk.containsKey(MapChunk.coordToKey(x + xChunk, y + yChunk))) {
                        // load it
                        ServerMapChunk newChunk = new ServerMapChunk(mapGen, x + xChunk, y + yChunk);
                        keyToChunk.put(newChunk.getKey(), newChunk);
                        chunk = newChunk;
                    } else {
                        chunk = keyToChunk.get(MapChunk.coordToKey(x + xChunk, y + yChunk));
                    }
                    chunk.registerContainedObject(obj);
                }
            }
        } else {
            // since this is not a player, don't make a new chunk for it. we only want to load chunks around players
            for (int x = -LOAD_CHUNK_RADIUS; x <= LOAD_CHUNK_RADIUS; ++x) {
                for (int y = -LOAD_CHUNK_RADIUS; y <= LOAD_CHUNK_RADIUS; ++y) {
                    if (keyToChunk.containsKey(MapChunk.coordToKey(x + xChunk, y + yChunk))) {
                        keyToChunk.get(MapChunk.coordToKey(x + xChunk, y + yChunk)).registerContainedObject(obj);
                    }
                }
            }
        }
    }
}
