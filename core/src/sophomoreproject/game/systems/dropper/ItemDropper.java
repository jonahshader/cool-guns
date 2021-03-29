package sophomoreproject.game.systems.dropper;

import com.badlogic.gdx.math.Vector2;
import sophomoreproject.game.singletons.LocalRandom;
import sophomoreproject.game.systems.GameServer;

import java.util.ArrayList;
import java.util.List;

public class ItemDropper {
    private List<DropAction> dropActions = new ArrayList<>();
    private List<Float> accumulatedCorrectedChances = new ArrayList<>();

    public void addDropAction(DropAction action) {
        dropActions.add(action);
    }

    public boolean tryDropItem(GameServer server, Vector2 pos, float difficulty) {
        accumulatedCorrectedChances.clear();
        accumulatedCorrectedChances.add(dropActions.get(0).getChance());
        for (int i = 1; i < dropActions.size(); ++i) {
            accumulatedCorrectedChances.add(accumulatedCorrectedChances.get(i-1) + dropActions.get(i).getChance());
        }
        float ranNum = LocalRandom.RAND.nextFloat();
        for (int i = 0; i < accumulatedCorrectedChances.size(); ++i) {
            if (ranNum < accumulatedCorrectedChances.get(i)) {
                dropActions.get(i).dropItem(server, pos, difficulty);
                return true;
            }
        }
        return false;
    }
}
