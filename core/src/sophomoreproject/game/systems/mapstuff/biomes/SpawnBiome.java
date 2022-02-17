package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import sophomoreproject.game.systems.mapstuff.Map;
import sophomoreproject.game.systems.mapstuff.Spawner;

public class SpawnBiome implements Biome{
    public static final float SPAWN_RADIUS = 16;
    private static final float S_R_2 = SPAWN_RADIUS * SPAWN_RADIUS;
    private Map map;

    public SpawnBiome(Map map) {
        this.map = map;
    }

    @Override
    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        return map.cobblestoneCell;
    }

    @Override
    public TiledMapTileLayer.Cell getForegroundCell(int x, int y) {
        return null;
    }

    @Override
    public double getSelectValue(int x, int y) {
        return x * x + y * y < S_R_2 ? 999 : -999;
    }

    @Override
    public boolean isEnemySpawn(int x, int y) {
        return false;
    }

    @Override
    public Spawner makeSpawner(int x, int y) {
        return null;
    }

    @Override
    public float getSpeedMultiplier(int x, int y) {
        return 1;
    }
}
