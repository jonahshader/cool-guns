package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.ConnectServerScreen;

public class PlayMultiGameAction implements MenuAction {
    private CoolGuns game;

    public PlayMultiGameAction(CoolGuns game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.setScreen(new ConnectServerScreen(game));
    }
}
