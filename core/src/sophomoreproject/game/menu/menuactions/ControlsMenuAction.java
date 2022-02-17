package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.ControlsMenuScreen;

public class ControlsMenuAction implements MenuAction {

    private CoolGuns game;


    public ControlsMenuAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new ControlsMenuScreen(game));
    }
}
