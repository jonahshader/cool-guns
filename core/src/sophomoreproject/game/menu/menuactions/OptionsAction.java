package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.GameScreen;
import sophomoreproject.game.screens.OptionsScreen;

public class OptionsAction implements MenuAction {

    private CoolGuns game;
    private int accountID;

    public OptionsAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new OptionsScreen(game));
    }
}
