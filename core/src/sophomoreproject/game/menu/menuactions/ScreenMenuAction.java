package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.ScreenMenuScreen;

public class ScreenMenuAction implements MenuAction {

    private CoolGuns game;


    public ScreenMenuAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new ScreenMenuScreen(game));
    }
}
