package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.ScreenMenuScreen;

public class ScreenMenuAction implements MenuAction {

    private CoolGuns game;
    private int accountID;

    public ScreenMenuAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new ScreenMenuScreen(game, accountID));
    }
}
