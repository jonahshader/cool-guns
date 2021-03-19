package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.FrameRateScreen;

public class FrameRateAction implements MenuAction {
    private CoolGuns game;
    private int accountID;

    public FrameRateAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new FrameRateScreen(game, accountID));
    }
}
