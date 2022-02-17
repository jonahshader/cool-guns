package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import sophomoreproject.game.systems.mapstuff.Spawner;

public interface Biome {
    TiledMapTileLayer.Cell getBackgroundCell(int x, int y);
    TiledMapTileLayer.Cell getForegroundCell(int x, int y);
    double getSelectValue(int x, int y);
    boolean isEnemySpawn(int x, int y);
    Spawner makeSpawner(int x, int y);
    float getSpeedMultiplier(int x, int y);
}
