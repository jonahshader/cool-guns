package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.shape.Rectangle;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.Menu;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.menu.MenuItem;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameClient;

public class MainMenuScreen implements Screen {

    private Camera mainMenuCamera;
    private Viewport mainMenuViewport;
    private CoolGuns game;
    private Menu mainMenu;
    private GameClient gameClient;
    private int accountID;



    public MainMenuScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;

        mainMenuCamera = new OrthographicCamera();
        mainMenuViewport = new FitViewport(1000, 600, mainMenuCamera);
        gameClient = new GameClient(accountID);

        mainMenu = new Menu(CustomAssetManager.getInstance().manager.get("myfont.ttf"));

        //mainMenu.addMenuItem("Play Game", new PlayGameAction());
        //mainMenu.addMenuItem("Edit Character", new EditAction());
        //mainMenu.addMenuItem("Options", new OptionsAction());
        //mainMenu.addMenuItem("Credits", new CreditsAction());
        //mainMenu.addMenuItem("Exit", new ExitAction());

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /*gameClient.run(delta);
        mainMenuViewport.apply();
        mainMenuCamera.update();
        game.batch.setProjectionMatrix(mainMenuCamera.combined);
        game.shapeRenderer.setProjectionMatrix(mainMenuCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        game.batch.begin();

    }

    @Override
    public void resize(int width, int height) {
        mainMenuViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
