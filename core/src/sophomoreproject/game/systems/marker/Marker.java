package sophomoreproject.game.systems.marker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.singletons.TextDisplay;

public class Marker {
    private final Sprite sprite;
    private final Vector2 position;
    private boolean inactive = false;
    private String label;
    private Color labelColor;

    public Marker(TextureRegion texture, Vector2 position, float scale, String label, Color labelColor) {
        this.position = position;
        this.label = label;
        this.labelColor = labelColor;
        sprite = new Sprite(texture);
        sprite.setScale(scale);
        sprite.setOriginCenter();

        MarkerSystem.getInstance().registerMarker(this);
    }

    public boolean isInactive() { return inactive; }
    public Vector2 getPosition() { return position; }
    public void drawIcon(SpriteBatch sb, float xCenter, float yCenter, float opacity) {
        sprite.setOriginBasedPosition(xCenter, yCenter);
        sprite.setColor(1, 1, 1, opacity);
        sprite.draw(sb);
        if (label != null && !label.isEmpty()) {
            TextDisplay.getInstance().drawTextInWorld(sb, label, xCenter, yCenter - 16, .2f, labelColor);
        }
    }
}
