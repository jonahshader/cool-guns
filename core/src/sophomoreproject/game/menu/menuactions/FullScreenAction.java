package sophomoreproject.game.menu.menuactions;

import com.badlogic.gdx.Gdx;
import sophomoreproject.game.menu.MenuAction;

public class FullScreenAction implements MenuAction {
    @Override
    public void execute() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }
}
