package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.AudioMenuScreen;
import sophomoreproject.game.screens.GameScreen;
import sophomoreproject.game.screens.OptionsScreen;

public class AudioMenuAction implements MenuAction{

    private CoolGuns game;
    private int accountID;

    public AudioMenuAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new AudioMenuScreen(game, accountID));
    }
}
