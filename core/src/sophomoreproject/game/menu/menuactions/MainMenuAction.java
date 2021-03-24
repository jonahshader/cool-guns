package sophomoreproject.game.menu.menuactions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.MainMenuScreen;

public class MainMenuAction implements MenuAction {

    private CoolGuns game;
    private int accountID;

    public MainMenuAction(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        game.setScreen(new MainMenuScreen(game, accountID));
    }
}
