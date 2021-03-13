package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class MapChunk implements Disposable {
    public static final int CHUNK_SIZE_TILES = 32; // length of side of chunk in tiles
    public static final int TILE_SIZE = 16; // size in pixels
    public static final int CHUNK_SIZE_PIXELS = CHUNK_SIZE_TILES * TILE_SIZE;

    private static final int[] backgroundLayers = { 0 };    // list of background layers
    private static final int[] foregroundLayers = { 1 };    // list of foreground layers

    private TiledMap map;
    private MapRenderer mapRenderer;
    private TiledMapTileLayer background, foreground;

    private final int x, y;

    public MapChunk(MapGenerator mapGen, int x, int y, SpriteBatch batch) {
        this.x = x;
        this.y = y;

        map = new TiledMap();

        background = new TiledMapTileLayer(CHUNK_SIZE_TILES, CHUNK_SIZE_TILES, TILE_SIZE, TILE_SIZE);
        foreground = new TiledMapTileLayer(CHUNK_SIZE_TILES, CHUNK_SIZE_TILES, TILE_SIZE, TILE_SIZE);

        MapLayers layers = map.getLayers();
        layers.add(background);
        layers.add(foreground);

        for (int yy = 0; yy < CHUNK_SIZE_TILES; ++yy) {
            for (int xx = 0; xx < CHUNK_SIZE_TILES; ++xx) {
                //TODO: world gen
                // background.setCell(xx, yy, cell)
                background.setCell(xx, yy,
                        mapGen.getBackgroundCell(x * CHUNK_SIZE_TILES + xx, y * CHUNK_SIZE_TILES + yy));
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1, batch);
    }

    public String getKey() {
        return x + " " + y;
    }

    public static String coordToKey(int x, int y) {
        return x + " " + y;
    }

    public void renderBackground(OrthographicCamera cam) {
        cam.translate(-getXInTiles() * TILE_SIZE, -getYInTiles() * TILE_SIZE);
        cam.update();
        mapRenderer.setView(cam);
        mapRenderer.render(backgroundLayers);
        cam.translate(getXInTiles() * TILE_SIZE, getYInTiles() * TILE_SIZE);
        cam.update();
    }

    public void renderForeground(OrthographicCamera cam) {
        mapRenderer.setView(cam);
        mapRenderer.render(foregroundLayers);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    public int getXInTiles() {
        return x * CHUNK_SIZE_TILES;
    }

    public int getYInTiles() {
        return y * CHUNK_SIZE_TILES;
    }
}
