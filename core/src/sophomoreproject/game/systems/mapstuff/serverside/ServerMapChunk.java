package sophomoreproject.game.systems.mapstuff.serverside;

import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.systems.mapstuff.MapGenerator;
import sophomoreproject.game.systems.mapstuff.Spawner;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerMapChunk {
    private ArrayList<Spawner> spawners;
    private HashMap<Integer, Player> idToPlayer;

    public ServerMapChunk(MapGenerator mapGen, int x, int y) {

    }
}
