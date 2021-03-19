package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.VolumeMenuScreen;

public class VolumeMenuAction implements MenuAction {
    private CoolGuns game;
    private int accountID;

    public VolumeMenuAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new VolumeMenuScreen(game, accountID));
    }
}
