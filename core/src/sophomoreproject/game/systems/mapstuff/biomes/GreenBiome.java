package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.TestObject;
import sophomoreproject.game.gameobjects.enemystuff.Enemy;
import sophomoreproject.game.gameobjects.enemystuff.EnemyInfo;
import sophomoreproject.game.systems.GameServer;
import sophomoreproject.game.systems.mapstuff.*;

import java.util.Random;

import static sophomoreproject.game.systems.mapstuff.MapChunk.TILE_SIZE;

public class GreenBiome implements Biome{
    private final Map map;
    private OctaveSet selection;
    private OctaveSet terrain;

    private OctaveSet yellowFlowerSelect;
    private OctaveSet yellowFlowerBigSelect;
    private OctaveSet redFlowerSelect;
    private OctaveSet denseGrassSelect;
    private OctaveSet enemySpawnSelect;

    private SpawnAction spawnAction = (server, x, y) -> {
        double diff = Math.sqrt(x * x + y * y) / MapChunk.CHUNK_SIZE_TILES;
        // spawn an enemy at x y with correct diff
        server.spawnAndSendGameObject(new Enemy(new EnemyInfo((float)diff), new Vector2((x * TILE_SIZE) + TILE_SIZE/2, (y * TILE_SIZE) + TILE_SIZE/2), server.getGameWorld().getNewNetID()));
//            server.spawnAndSendGameObject(new TestObject(new Vector2((x * TILE_SIZE) + TILE_SIZE/2, (y * TILE_SIZE) + TILE_SIZE/2), server.getGameWorld().getNewNetID()));
    };

    public GreenBiome(Map map, Random random) {
        this.map = map;
        selection = new OctaveSet(random);
        selection.addOctaveFractal(0.002, 1.0, 2.0, 0.5, 2);

        terrain = new OctaveSet(random);
        terrain.addOctaveFractal(0.005, 1.0, 2.0, 0.5, 5);

        yellowFlowerSelect = new OctaveSet(random);
        yellowFlowerSelect.addOctave(13.59153, 1.0);

        yellowFlowerBigSelect = new OctaveSet(random);
        yellowFlowerBigSelect.addOctave(13.58, 1.0);

        redFlowerSelect = new OctaveSet(random);
        redFlowerSelect.addOctave(13.5937, 1.0);

        denseGrassSelect = new OctaveSet(random);
        denseGrassSelect.addOctaveFractal(0.05, 1.0, 2.0, 0.5, 2);

        enemySpawnSelect = new OctaveSet(random);
        enemySpawnSelect.addOctaveFractal(0.033, 1.0, 2.0, 0.5, 4);
    }

    @Override
    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        double val = terrain.getValue(x, y);
        if (val < -0.25) {
            return map.waterCell;
        } else if (val < -0.175) {
            return map.sandCell;
        } else if (denseGrassSelect.getValue(x, y) > .5) {
            return map.grassDenseCell;
        } else if (yellowFlowerSelect.getValue(x, y) > 0.95) {
            return map.grassYellowFlowerCell;
        } else if (yellowFlowerBigSelect.getValue(x, y) > 0.98) {
            return map.grassYellowFlowerBiggerCell;
        } else if (redFlowerSelect.getValue(x, y) > 0.94) {
            return map.grassRedFlowerCell;
        } else {
            return map.grassCell;
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
//        return selection.getValue(x, y) - Math.sqrt(x * x + y * y) * 0.0001;
        return selection.getValue(x, y);
    }

    @Override
    public boolean isEnemySpawn(int x, int y) {
        return (terrain.getValue(x, y) >= -0.25) && (enemySpawnSelect.getValue(x, y) > 0.8);
    }

    @Override
    public Spawner makeSpawner(int x, int y) {
        if (isEnemySpawn(x, y)) {
            return new Spawner(x, y, 5, 5, 3f, 20f, spawnAction);
        }
        return null;
    }
}
