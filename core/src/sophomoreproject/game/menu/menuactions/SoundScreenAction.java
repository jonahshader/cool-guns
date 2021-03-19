package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.SoundScreen;

public class SoundScreenAction implements MenuAction {
    private CoolGuns game;
    private int accountID;

    public SoundScreenAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new SoundScreen(game, accountID));
    }
}
