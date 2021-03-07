package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.GameScreen;

public class PlayGameAction implements MenuAction {
    private CoolGuns game;
    private int accountID;

    public PlayGameAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new GameScreen(game, accountID));
    }
}
