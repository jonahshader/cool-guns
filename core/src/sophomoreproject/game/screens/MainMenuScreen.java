package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
//import javafx.scene.shape.Rectangle;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.Menu;
import sophomoreproject.game.menu.menuactions.ExitGameAction;
import sophomoreproject.game.menu.menuactions.OptionsAction;
import sophomoreproject.game.menu.menuactions.PlaySingleGameAction;
import sophomoreproject.game.singletons.CustomAssetManager;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class MainMenuScreen implements Screen {

    private Camera mainMenuCamera;
    private Viewport mainMenuViewport;
    private CoolGuns game;
    private Menu mainMenu;



    public MainMenuScreen(CoolGuns game) {
        this.game = game;

        mainMenuCamera = new OrthographicCamera();
        mainMenuViewport = new FitViewport(1000, 600, mainMenuCamera);

        mainMenu = new Menu(CustomAssetManager.getInstance().manager.get(MENU_FONT), mainMenuCamera, 50);

        mainMenu.addMenuItem("Single Player", new PlaySingleGameAction(game));
        //mainMenu.addMenuItem("Edit Character", new EditAction());
        mainMenu.addMenuItem("Options", new OptionsAction(game));
       //mainMenu.addMenuItem("Credits", new CreditsAction());
        mainMenu.addMenuItem("Exit", new ExitGameAction());

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        mainMenu.run(delta);


        mainMenuViewport.apply();
        mainMenuCamera.update();
        game.batch.setProjectionMatrix(mainMenuCamera.combined);
        game.shapeRenderer.setProjectionMatrix(mainMenuCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mainMenu.drawShape(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.begin();
        mainMenu.drawText(game.batch);
        game.batch.end();



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
