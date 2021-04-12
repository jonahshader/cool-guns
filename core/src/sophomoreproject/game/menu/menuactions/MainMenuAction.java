package sophomoreproject.game.menu.menuactions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.screens.MainMenuScreen;

public class MainMenuAction implements MenuAction {

    private CoolGuns game;


    public MainMenuAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        game.setScreen(new MainMenuScreen(game));
    }
}
