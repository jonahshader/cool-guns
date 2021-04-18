package sophomoreproject.game.systems.marker;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.singletons.CustomAssetManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;

public final class MarkerSystem {
    private static MarkerSystem instance;
    private static final float RING_INSET = 60;
    private final List<Marker> activeMarkers = Collections.synchronizedList(new ArrayList<>());
    private final Vector2 renderPos = new Vector2();
    private static final float OPACITY = .35f;
    private Sprite arrow;

    private MarkerSystem() {
        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        arrow = new Sprite(atlas.findRegion("arrow"));
        arrow.setOriginCenter();
        arrow.setColor(new Color(1, 1, 1, OPACITY));
    }

    public static synchronized MarkerSystem getInstance() {
        if (instance == null) {
            instance = new MarkerSystem();
        }
        return instance;
    }

    // this method should only be used by marker. markers register themselves in the marker constructor
    public void registerMarker(Marker m) {
        synchronized (activeMarkers) {
            activeMarkers.add(m);
        }
    }

    public void render(SpriteBatch sb, Viewport worldViewport, Camera worldCamera) {
        synchronized (activeMarkers) {
            activeMarkers.removeIf(Marker::isInactive); // remove inactive markers first
            int minDim = (int)Math.min(worldViewport.getWorldWidth(), worldViewport.getWorldHeight());
            float xMinVisible = worldCamera.position.x - worldViewport.getWorldWidth() / 2;
            float xMaxVisible = worldCamera.position.x + worldViewport.getWorldWidth() / 2;
            float yMinVisible = worldCamera.position.y - worldViewport.getWorldHeight() / 2;
            float yMaxVisible = worldCamera.position.y + worldViewport.getWorldHeight() / 2;
            for (Marker m : activeMarkers) {
                if (m.getPosition().x < xMinVisible || m.getPosition().x > xMaxVisible || m.getPosition().y < yMinVisible || m.getPosition().y > yMaxVisible) {
                    renderPos.set(m.getPosition());
                    renderPos.sub(worldCamera.position.x, worldCamera.position.y);
                    renderPos.nor().scl(minDim * .5f - RING_INSET);
                    m.drawIcon(sb, renderPos.x + worldCamera.position.x, renderPos.y + worldCamera.position.y, OPACITY);
                    arrow.setRotation(renderPos.angleDeg());
                    renderPos.scl(1.1f);
                    arrow.setOriginBasedPosition(renderPos.x + worldCamera.position.x, renderPos.y + worldCamera.position.y);
                    arrow.draw(sb);
                }
            }
        }
    }
}
