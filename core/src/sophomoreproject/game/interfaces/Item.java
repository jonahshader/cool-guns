package sophomoreproject.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.packets.UpdateItem;

import java.util.ArrayList;

public abstract class Item extends GameObject implements Renderable{
    protected Vector2 position = new Vector2();
    protected Vector2 angle = new Vector2();
    protected int ownerNetId;
    private boolean equipped = false;
    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }
    public abstract void renderIcon(SpriteBatch sb, float size, float x, float y);
    public abstract void updateItem(float dt, boolean usedOnce, boolean using, Vector2 angle, Player player);

    @Override
    public void addUpdatePacketToBuffer(ArrayList<Object> updatePacketBuffer) {
        updatePacketBuffer.add(new UpdateItem(networkID, equipped, position.x, position.y, angle.x, angle.y));
    }

    @Override
    public void receiveUpdate(Object updatePacket) {
        if (updatePacket instanceof UpdateItem) {
            UpdateItem update = (UpdateItem) updatePacket;
            setEquipped(update.equipped);
            position.x = update.xPos;
            position.y = update.yPos;
            angle.x = update.xAngle;
            angle.y = update.yAngle;
        }
    }
}
