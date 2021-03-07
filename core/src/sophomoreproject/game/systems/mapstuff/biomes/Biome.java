package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public interface Biome {
    TiledMapTileLayer.Cell getBackgroundCell(int x, int y);
    TiledMapTileLayer.Cell getForegroundCell(int x, int y);
    double getSelectValue(int x, int y);
}
