package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class MapChunk implements Disposable {
    public static final int CHUNK_SIZE_TILES = 32; // length of side of chunk in tiles
    public static final int TILE_SIZE = 16; // size in pixels
    public static final int CHUNK_SIZE_PIXELS = CHUNK_SIZE_TILES * TILE_SIZE;

    private static final int[] backgroundLayers = { 0 };    // list of background layers
    private static final int[] foregroundLayers = { 1 };    // list of foreground layers
    private static final int[] allLayers = { 0, 1 };

    private TiledMap map;
    private MapRenderer mapRenderer;
    private TiledMapTileLayer background, foreground;

    private MapGenerator mapGen;
    private final int x;
    private final int y;

    public MapChunk(MapGenerator mapGen, int x, int y, SpriteBatch batch) {
        this.mapGen = mapGen;
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
                foreground.setCell(xx, yy,
                        mapGen.getForegroundCell(x * CHUNK_SIZE_TILES + xx, y * CHUNK_SIZE_TILES + yy));
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1, batch);
    }

    public static ArrayList<Spawner> getSpawners(int xChunk, int yChunk, MapGenerator mapGen) {
        ArrayList<Spawner> spawners = new ArrayList<>();
        for (int yy = 0; yy < CHUNK_SIZE_TILES; ++yy) {
            for (int xx = 0; xx < CHUNK_SIZE_TILES; ++xx) {
                Spawner spawner = mapGen.getSpawner(xChunk * CHUNK_SIZE_TILES + xx, yChunk * CHUNK_SIZE_TILES + yy);
                if (spawner != null)
                    spawners.add(spawner);
            }
        }
        return spawners;
    }

    public ArrayList<Spawner> getSpawners() {
        ArrayList<Spawner> spawners = new ArrayList<>();
        for (int yy = 0; yy < CHUNK_SIZE_TILES; ++yy) {
            for (int xx = 0; xx < CHUNK_SIZE_TILES; ++xx) {
                Spawner spawner = mapGen.getSpawner(x * CHUNK_SIZE_TILES + xx, y * CHUNK_SIZE_TILES + yy);
                if (spawner != null)
                    spawners.add(spawner);
            }
        }
        return spawners;
    }

    public String getKey() {
        return x + " " + y;
    }

    public static String coordToKey(int x, int y) {
        return x + " " + y;
    }

    public void render(OrthographicCamera cam) {
        cam.translate(-getXInTiles() * TILE_SIZE, -getYInTiles() * TILE_SIZE);
        cam.update();
        mapRenderer.setView(cam);
        mapRenderer.render(allLayers);
//        mapRenderer.render(foregroundLayers);
        cam.translate(getXInTiles() * TILE_SIZE, getYInTiles() * TILE_SIZE);
        cam.update();
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
