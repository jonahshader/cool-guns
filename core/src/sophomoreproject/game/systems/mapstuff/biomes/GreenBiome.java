package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.enemystuff.Enemy;
import sophomoreproject.game.gameobjects.enemystuff.EnemyInfo;
import sophomoreproject.game.systems.mapstuff.*;

import java.util.Random;

import static sophomoreproject.game.systems.mapstuff.MapChunk.TILE_SIZE;
import static sophomoreproject.game.systems.mapstuff.biomes.SpawnBiome.SPAWN_RADIUS;

public class GreenBiome implements Biome{
    private final Map map;
    private OctaveSet selection;
    private TwistedOctaveSet terrain;

    private OctaveSet yellowFlowerSelect;
    private OctaveSet yellowFlowerBigSelect;
    private OctaveSet redFlowerSelect;
    private TwistedOctaveSet denseGrassSelect;
    private OctaveSet enemySpawnSelect;
    private OctaveSet enemySpawnHighFreqSelect;

    enum TerrainType {
        WATER,
        SAND,
        GRASS_DENSE,
        GRASS_YELLOW,
        GRASS_YELLOW_BIG,
        GRASS_RED,
        GRASS
    }

    private SpawnAction spawnAction = (server, x, y) -> {

        // spawn an enemy at x y with correct diff
        server.spawnAndSendGameObject(new Enemy(new EnemyInfo(getDiffFromWorldPos(x, y)), new Vector2(x, y), server.getGameWorld().getNewNetID()));
//            server.spawnAndSendGameObject(new TestObject(new Vector2((x * TILE_SIZE) + TILE_SIZE/2, (y * TILE_SIZE) + TILE_SIZE/2), server.getGameWorld().getNewNetID()));
    };

    public GreenBiome(Map map, Random random) {
        this.map = map;
        selection = new OctaveSet(random);
        selection.addOctaveFractal(0.001, 1.0, 2.0, 0.5, 2);

        terrain = new TwistedOctaveSet(random, 20);
        terrain.addOctaveFractal(0.004, 1.0, 2.0, 0.5, 5);
        terrain.addTwisterOctaveFractal(0.005, 1.0, 2.0, 0.5, 2);

        yellowFlowerSelect = new OctaveSet(random);
        yellowFlowerSelect.addOctave(13.59153, 1.0);

        yellowFlowerBigSelect = new OctaveSet(random);
        yellowFlowerBigSelect.addOctave(13.58, 1.0);

        redFlowerSelect = new OctaveSet(random);
        redFlowerSelect.addOctave(13.5937, 1.0);

        denseGrassSelect = new TwistedOctaveSet(random, 15);
        denseGrassSelect.addOctaveFractal(0.035, 1.0, 2.0, 0.5, 2);
        denseGrassSelect.addTwisterOctaveFractal(0.01, 1.0, 2.0, 0.5, 2);

        enemySpawnSelect = new OctaveSet(random);
        enemySpawnSelect.addOctaveFractal(0.025, 1.0, 2.0, 0.5, 3);

        enemySpawnHighFreqSelect = new OctaveSet(random);
        enemySpawnHighFreqSelect.addOctave(19.92519, 1.0);

    }

    @Override
    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        TerrainType val = getBackgroundCellEnum(x, y);
        switch (val) {
            case WATER: return map.waterCell;
            case SAND: return map.sandCell;
            case GRASS_DENSE: return map.grassDenseCell;
            case GRASS_YELLOW: return map.grassYellowFlowerCell;
            case GRASS_YELLOW_BIG: return map.grassYellowFlowerBiggerCell;
            case GRASS_RED: return map.grassRedFlowerCell;
            default: return map.grassCell;
        }
    }

    private TerrainType getBackgroundCellEnum(int x, int y) {
        double val = terrain.getValue(x, y);
        if (val < -0.25) {
            return TerrainType.WATER;
        } else if (val < -0.135) {
            return TerrainType.SAND;
        } else if (denseGrassSelect.getValue(x, y) > .5) {
            return TerrainType.GRASS_DENSE;
        } else if (yellowFlowerSelect.getValue(x, y) > 0.95) {
            return TerrainType.GRASS_YELLOW;
        } else if (yellowFlowerBigSelect.getValue(x, y) > 0.98) {
            return TerrainType.GRASS_YELLOW_BIG;
        } else if (redFlowerSelect.getValue(x, y) > 0.94) {
            return TerrainType.GRASS_RED;
        } else {
            return TerrainType.GRASS;
        }
    }

    @Override
    public TiledMapTileLayer.Cell getForegroundCell(int x, int y) {
        if (isEnemySpawn(x, y)) {
            return map.holeCell;
        } else {
            return null;
        }
    }

    @Override
    public double getSelectValue(int x, int y) {
        return selection.getValue(x, y);
    }

    @Override
    public boolean isEnemySpawn(int x, int y) {
        if (x * x + y * y < SPAWN_RADIUS * SPAWN_RADIUS * 1.25) return false;
        float diff = (float) (1 - Math.pow(2, -getDiffFromWorldPos(x, y) * 0.1));
        return (terrain.getValue(x, y) >= -0.25) && (enemySpawnSelect.getValue(x, y) > 0.8 - diff * 0.05) && (enemySpawnHighFreqSelect.getValue(x, y) > (0.5 - diff));
    }

    @Override
    public Spawner makeSpawner(int x, int y) {
        if (isEnemySpawn(x, y)) {
            float xWorld = x * TILE_SIZE + (TILE_SIZE / 2);
            float yWorld = y * TILE_SIZE + (TILE_SIZE / 2);
            float diff = getDiffFromWorldPos(xWorld, yWorld);
            return new Spawner(new Vector2(xWorld, yWorld), 3, 1, 15f + (float)Math.pow(diff, .8) * 1.1f, spawnAction);
        }
        return null;
    }

    @Override
    public float getSpeedMultiplier(int x, int y) {
        switch (getBackgroundCellEnum(x, y)) {
            case WATER: return .4f;
            case SAND: return .8f;
            case GRASS_DENSE: return .7f;
            default: return 1.0f;
        }
    }

    private float getDiffFromWorldPos(float x, float y) {
        return (float)Math.sqrt(x * x + y * y) / MapChunk.CHUNK_SIZE_TILES;
    }
}
