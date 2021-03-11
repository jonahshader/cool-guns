package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.RandomXS128;
import sophomoreproject.game.systems.mapstuff.biomes.Biome;
import sophomoreproject.game.systems.mapstuff.biomes.GreenBiome;

import java.util.ArrayList;

public class MapGenerator {
    private RandomXS128 random;

    private ArrayList<Biome> biomes;

    public MapGenerator(Map map, long seed) {
        random = new RandomXS128(seed);

        biomes = new ArrayList<>();
        biomes.add(new GreenBiome(map, random));
    }

    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        // TODO: write a function that selects max value from all biome selection functions

        // for now, we only have the GreenBiome so ill just return that
        return biomes.get(0).getBackgroundCell(x, y);
    }

    public TiledMapTileLayer.Cell getForegroundCell(int x, int y) {
        return biomes.get(0).getForegroundCell(x, y);
    }
}
