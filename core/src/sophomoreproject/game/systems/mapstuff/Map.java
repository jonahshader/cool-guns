package sophomoreproject.game.systems.mapstuff;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

import static sophomoreproject.game.singletons.CustomAssetManager.MAP_PACK;

public class Map {
    public static final int LOAD_CHUNK_RADIUS = 3;
    public static final int UNLOAD_CHUNK_RADIUS = 4;

    private HashMap<String, MapChunk> keyToChunk;
    private MapGenerator mapGen;
    private CoolGuns game;

    private int chunkLoadCenterX = 0;
    private int chunkLoadCenterY = 0;

    public TiledMapTileLayer.Cell cobblestoneCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell grassCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell grassDenseCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell grassRedFlowerCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell grassYellowFlowerCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell grassYellowFlowerBiggerCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell hellCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell palaceFloorCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell sandCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell techCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallBottomCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallBottomLeftCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallBottomRightCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallCenterCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallLeftCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallRightCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallTopCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallTopLeftCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell wallTopRightCell = new TiledMapTileLayer.Cell();
    public TiledMapTileLayer.Cell waterCell = new TiledMapTileLayer.Cell();

    public Map(CoolGuns game, long seed) {
        this.game = game;
        TextureAtlas mapAtlas = CustomAssetManager.getInstance().manager.get(MAP_PACK);

        StaticTiledMapTile cobblestoneTile = new StaticTiledMapTile(mapAtlas.findRegion("cobblestone"));
        StaticTiledMapTile grassTile = new StaticTiledMapTile(mapAtlas.findRegion("grass"));
        StaticTiledMapTile grassDenseTile = new StaticTiledMapTile(mapAtlas.findRegion("grass_dense"));
        StaticTiledMapTile grassRedFlowerTile = new StaticTiledMapTile(mapAtlas.findRegion("grass_red_flower"));
        StaticTiledMapTile grassYellowFlowerTile = new StaticTiledMapTile(mapAtlas.findRegion("grass_yellow_flower"));
        StaticTiledMapTile grassYellowFlowerBiggerTile = new StaticTiledMapTile(mapAtlas.findRegion("grass_yellow_flower_bigger"));
        StaticTiledMapTile hellTile = new StaticTiledMapTile(mapAtlas.findRegion("hell"));
        StaticTiledMapTile palaceFloorTile = new StaticTiledMapTile(mapAtlas.findRegion("palace_floor"));
        StaticTiledMapTile sandTile = new StaticTiledMapTile(mapAtlas.findRegion("sand"));
        StaticTiledMapTile techTile = new StaticTiledMapTile(mapAtlas.findRegion("tech"));
        StaticTiledMapTile wallBottomTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_bottom"));
        StaticTiledMapTile wallBottomLeftTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_bottom_left"));
        StaticTiledMapTile wallBottomRightTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_bottom_right"));
        StaticTiledMapTile wallCenterTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_center"));
        StaticTiledMapTile wallLeftTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_left"));
        StaticTiledMapTile wallRightTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_right"));
        StaticTiledMapTile wallTopTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_top"));
        StaticTiledMapTile wallTopLeftTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_top_left"));
        StaticTiledMapTile wallTopRightTile = new StaticTiledMapTile(mapAtlas.findRegion("wall_top_right"));
        StaticTiledMapTile waterTile = new StaticTiledMapTile(mapAtlas.findRegion("water"));

        cobblestoneCell.setTile(cobblestoneTile);
        grassCell.setTile(grassTile);
        grassDenseCell.setTile(grassDenseTile);
        grassRedFlowerCell.setTile(grassRedFlowerTile);
        grassYellowFlowerCell.setTile(grassYellowFlowerTile);
        grassYellowFlowerBiggerCell.setTile(grassYellowFlowerBiggerTile);
        hellCell.setTile(hellTile);
        palaceFloorCell.setTile(palaceFloorTile);
        sandCell.setTile(sandTile);
        techCell.setTile(techTile);
        wallBottomCell.setTile(wallBottomTile);
        wallBottomLeftCell.setTile(wallBottomLeftTile);
        wallBottomRightCell.setTile(wallBottomRightTile);
        wallCenterCell.setTile(wallCenterTile);
        wallLeftCell.setTile(wallLeftTile);
        wallRightCell.setTile(wallRightTile);
        wallTopCell.setTile(wallTopTile);
        wallTopLeftCell.setTile(wallTopLeftTile);
        wallTopRightCell.setTile(wallTopRightTile);
        waterCell.setTile(waterTile);

        keyToChunk = new HashMap<>();
        mapGen = new MapGenerator(this, seed);
    }

    public void run() {
        for (int x = -LOAD_CHUNK_RADIUS; x <= LOAD_CHUNK_RADIUS; ++x) {
            for (int y =  -LOAD_CHUNK_RADIUS; y <= LOAD_CHUNK_RADIUS; ++y) {
                // if this chunk is not loaded,
                if (!keyToChunk.containsKey(MapChunk.coordToKey(x + chunkLoadCenterX, y + chunkLoadCenterY))) {
                    // load it
                    MapChunk newChunk = new MapChunk(mapGen, x + chunkLoadCenterX, y + chunkLoadCenterY, game.batch);
                    keyToChunk.put(newChunk.getKey(), newChunk);
                }
            }
        }

//        ArrayList<MapChunk> chunkRemoveQueue = new ArrayList<>();
//        for (MapChunk chunk : keyToChunk.values()) {
//            if (shouldUnloadChunk(chunk)) {
//                chunkRemoveQueue.add(chunk);
//            }
//        }
//
//        for (MapChunk toUnload : chunkRemoveQueue) {
//            toUnload.dispose();
//            keyToChunk.remove(toUnload.getKey());
//        }
//        chunkRemoveQueue.clear();
    }

    public void render(OrthographicCamera cam) {
        updateLoadCenterChunk(cam);
        for (MapChunk chunk : keyToChunk.values()) {
            chunk.renderBackground(cam);
        }
    }

    private boolean shouldUnloadChunk(MapChunk chunk) {
        return Math.abs(chunk.getX() - chunkLoadCenterX) > UNLOAD_CHUNK_RADIUS
                || Math.abs(chunk.getY() - chunkLoadCenterY) > UNLOAD_CHUNK_RADIUS;
    }

    private void updateLoadCenterChunk(Camera cam) {
        chunkLoadCenterX = (int) Math.floor(cam.position.x / MapChunk.CHUNK_SIZE_PIXELS);
        chunkLoadCenterX = (int) Math.floor(cam.position.y / MapChunk.CHUNK_SIZE_PIXELS);
    }
}
