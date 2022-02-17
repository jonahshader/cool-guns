package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.RandomXS128;
import sophomoreproject.game.systems.mapstuff.biomes.Biome;
import sophomoreproject.game.systems.mapstuff.biomes.GreenBiome;
import sophomoreproject.game.systems.mapstuff.biomes.SpawnBiome;

import java.util.ArrayList;

public class MapGenerator {
    private RandomXS128 random;

    private ArrayList<Biome> biomes;

    public MapGenerator(Map map, long seed) {
        random = new RandomXS128(seed);

        biomes = new ArrayList<>();
        biomes.add(new GreenBiome(map, random));
        biomes.add(new SpawnBiome(map));
    }

    public Spawner getSpawner(int x, int y) {
        return getSelectedBiome(x, y).makeSpawner(x, y);
    }

    public Biome getSelectedBiome(int x, int y) {
        Biome selectedBiome = biomes.get(0);
        double selectVal = selectedBiome.getSelectValue(x, y);
        for (int i = 1; i < biomes.size(); ++i) {
            double newSelectVal = biomes.get(i).getSelectValue(x, y);
            if (newSelectVal > selectVal) {
                selectedBiome = biomes.get(i);
                selectVal = newSelectVal;
            }
        }
        return selectedBiome;
    }

    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        return getSelectedBiome(x, y).getBackgroundCell(x, y);
    }

    public TiledMapTileLayer.Cell getForegroundCell(int x, int y) {
        return getSelectedBiome(x, y).getForegroundCell(x, y);
    }

    public float getSpeedMultiplier(int x, int y) {
        return getSelectedBiome(x, y).getSpeedMultiplier(x, y);
    }
}
