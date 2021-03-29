package sophomoreproject.game.systems.dropper;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.systems.GameServer;

public abstract class DropAction {
    public DropAction(float chance) {
        this.chance = chance;
    }

    private float chance;
    public float getChance() { return chance; }
    public void setChance(float chance) { this.chance = chance; }
    public abstract void dropItem(GameServer server, Vector2 pos, float difficulty);
}
