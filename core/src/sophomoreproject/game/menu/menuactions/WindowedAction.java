package sophomoreproject.game.menu.menuactions;

import com.badlogic.gdx.Gdx;
import sophomoreproject.game.menu.MenuAction;

public class WindowedAction implements MenuAction {
    @Override
    public void execute() {
        Gdx.graphics.setWindowedMode(1280, 720);
    }
}
