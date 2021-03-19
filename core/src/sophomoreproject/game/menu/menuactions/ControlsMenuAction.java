package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.ControlsMenuScreen;

public class ControlsMenuAction implements MenuAction {

    private CoolGuns game;
    private int accountID;

    public ControlsMenuAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new ControlsMenuScreen(game, accountID));
    }
}
