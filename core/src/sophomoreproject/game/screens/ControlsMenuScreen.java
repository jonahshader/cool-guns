package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.Menu;
import sophomoreproject.game.menu.menuactions.OptionsAction;
import sophomoreproject.game.menu.menuactions.PreferencesAction;
import sophomoreproject.game.singletons.CustomAssetManager;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class ControlsMenuScreen implements Screen {

    private Camera controlsMenuCamera;
    private Viewport controlsMenuViewport;
    private CoolGuns game;
    private Menu controlsMenu;

    public ControlsMenuScreen(CoolGuns game, int accountID) {
        this.game = game;


        controlsMenuCamera = new OrthographicCamera();
        controlsMenuViewport = new FitViewport(1000, 600, controlsMenuCamera);

        controlsMenu = new Menu(CustomAssetManager.getInstance().manager.get(MENU_FONT), controlsMenuCamera, 50);

        controlsMenu.addMenuItem("Back", new OptionsAction(game, accountID));
        controlsMenu.addMenuItem("Preferences", new PreferencesAction(game, accountID));

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        controlsMenu.run(delta);


        controlsMenuViewport.apply();
        controlsMenuCamera.update();
        game.batch.setProjectionMatrix(controlsMenuCamera.combined);
        game.shapeRenderer.setProjectionMatrix(controlsMenuCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        controlsMenu.drawShape(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.begin();
        controlsMenu.drawText(game.batch);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {
        controlsMenuViewport.update(width, height);
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
