package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.SoundScreen;

public class SoundScreenAction implements MenuAction {
    private CoolGuns game;


    public SoundScreenAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new SoundScreen(game));
    }
}
