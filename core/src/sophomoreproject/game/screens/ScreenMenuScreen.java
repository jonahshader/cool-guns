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
import sophomoreproject.game.menu.menuactions.FrameRateAction;
import sophomoreproject.game.menu.menuactions.FullScreenAction;
import sophomoreproject.game.menu.menuactions.OptionsAction;
import sophomoreproject.game.menu.menuactions.WindowedAction;
import sophomoreproject.game.singletons.CustomAssetManager;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class ScreenMenuScreen implements Screen {
    private Camera screenMenuCamera;
    private Viewport screenMenuViewport;
    private CoolGuns game;
    private Menu screenMenu;

    public ScreenMenuScreen(CoolGuns game) {
        this.game = game;


        screenMenuCamera = new OrthographicCamera();
        screenMenuViewport = new FitViewport(1000, 600, screenMenuCamera);

        screenMenu = new Menu(CustomAssetManager.getInstance().manager.get(MENU_FONT), screenMenuCamera, 50);

        screenMenu.addMenuItem("Back", new OptionsAction(game));
        screenMenu.addMenuItem("FullScreen", new FullScreenAction());
        screenMenu.addMenuItem("Windowed", new WindowedAction());
//        screenMenu.addMenuItem("FrameRate", new FrameRateAction(game, accountID));
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        screenMenu.run(delta);


        screenMenuViewport.apply();
        screenMenuCamera.update();
        game.batch.setProjectionMatrix(screenMenuCamera.combined);
        game.shapeRenderer.setProjectionMatrix(screenMenuCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        screenMenu.drawShape(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.begin();
        screenMenu.drawText(game.batch);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {
        screenMenuViewport.update(width, height);
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
