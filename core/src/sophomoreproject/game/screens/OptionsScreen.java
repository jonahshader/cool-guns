package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.Menu;
import sophomoreproject.game.menu.menuactions.*;
import sophomoreproject.game.singletons.CustomAssetManager;
import com.badlogic.gdx.Screen;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class OptionsScreen implements Screen {

    private Camera optionsCamera;
    private Viewport optionsViewport;
    private CoolGuns game;
    private Menu optionsMenu;

    public OptionsScreen(CoolGuns game, int accountID) {
        this.game = game;


        optionsCamera = new OrthographicCamera();
        optionsViewport = new FitViewport(1000, 600, optionsCamera);

        optionsMenu = new Menu(CustomAssetManager.getInstance().manager.get(MENU_FONT), optionsCamera);

        optionsMenu.addMenuItem("Audio", new AudioMenuAction(game, accountID));
        optionsMenu.addMenuItem("Screen", new ScreenMenuAction(game, accountID));
        optionsMenu.addMenuItem("Controls", new ControlsMenuAction(game, accountID));


    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        optionsMenu.run(delta);


        optionsViewport.apply();
        optionsCamera.update();
        game.batch.setProjectionMatrix(optionsCamera.combined);
        game.shapeRenderer.setProjectionMatrix(optionsCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        optionsMenu.drawShape(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.begin();
        optionsMenu.drawText(game.batch);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {
        optionsViewport.update(width, height);
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
