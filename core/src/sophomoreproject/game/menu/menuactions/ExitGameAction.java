package sophomoreproject.game.menu.menuactions;

import com.badlogic.gdx.Gdx;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;

public class ExitGameAction implements MenuAction {

    @Override
    public void execute() {
        Gdx.app.exit();
    }
}
