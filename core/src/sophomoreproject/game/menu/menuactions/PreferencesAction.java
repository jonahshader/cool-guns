package sophomoreproject.game.menu.menuactions;

import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.PreferencesScreen;

public class PreferencesAction implements MenuAction {
    private CoolGuns game;


    public PreferencesAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new PreferencesScreen(game));
    }
}
