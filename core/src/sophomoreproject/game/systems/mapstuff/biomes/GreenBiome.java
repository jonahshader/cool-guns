package sophomoreproject.game.systems.mapstuff.biomes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import sophomoreproject.game.systems.mapstuff.Map;
import sophomoreproject.game.systems.mapstuff.Octave;
import sophomoreproject.game.systems.mapstuff.OctaveSet;

import java.util.Random;

public class GreenBiome implements Biome{
    private Map map;
    private OctaveSet selection;
    private OctaveSet terrain;

    private OctaveSet yellowFlowerSelect;
    private OctaveSet yellowFlowerBigSelect;
    private OctaveSet redFlowerSelect;
    private OctaveSet denseGrassSelect;

    public GreenBiome(Map map, Random random) {
        this.map = map;
        selection = new OctaveSet(random);
        selection.addOctaveFractal(0.002, 1.0, 2.0, 0.5, 2);

        terrain = new OctaveSet(random);
        terrain.addOctaveFractal(0.005, 1.0, 2.0, 0.5, 4);

        yellowFlowerSelect = new OctaveSet(random);
        yellowFlowerSelect.addOctave(13.59153, 1.0);

        yellowFlowerBigSelect = new OctaveSet(random);
        yellowFlowerBigSelect.addOctave(13.58, 1.0);

        redFlowerSelect = new OctaveSet(random);
        redFlowerSelect.addOctave(13.5937, 1.0);

        denseGrassSelect = new OctaveSet(random);
        denseGrassSelect.addOctaveFractal(0.05, 1.0, 2.0, 0.5, 2);
    }

    @Override
    public TiledMapTileLayer.Cell getBackgroundCell(int x, int y) {
        double val = terrain.getValue(x, y);
        if (val < -0.25) {
            return map.waterCell;
        } else if (val < -0.175) {
            return map.sandCell;
        } else if (denseGrassSelect.getValue(x, y) > .75) {
            return map.grassDenseCell;
        } else if (yellowFlowerSelect.getValue(x, y) > 0.95) {
            return map.grassYellowFlowerCell;
        } else if (yellowFlowerBigSelect.getValue(x, y) > 0.98) {
            return map.grassYellowFlowerBiggerCell;
        } else if (redFlowerSelect.getValue(x, y) > 0.92) {
            return map.grassRedFlowerCell;
        } else {
            return map.grassCell;
        }
    }

    @Override
    public TiledMapTileLayer.Cell getForegroundCell(int x, int y) {
        return null;
    }

    @Override
    public double getSelectValue(int x, int y) {
//        return selection.getValue(x, y) - Math.sqrt(x * x + y * y) * 0.0001;
        return selection.getValue(x, y);
    }
}
