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
import sophomoreproject.game.menu.menuactions.SoundScreenAction;
import sophomoreproject.game.menu.menuactions.VolumeMenuAction;
import sophomoreproject.game.singletons.CustomAssetManager;

import static sophomoreproject.game.singletons.CustomAssetManager.MENU_FONT;

public class AudioMenuScreen implements Screen {

    private Camera audioMenuCamera;
    private Viewport audioMenuViewport;
    private CoolGuns game;
    private Menu audioMenu;

    public AudioMenuScreen(CoolGuns game) {
        this.game = game;

        audioMenuCamera = new OrthographicCamera();
        audioMenuViewport = new FitViewport(1000, 600, audioMenuCamera);

        audioMenu = new Menu(CustomAssetManager.getInstance().manager.get(MENU_FONT), audioMenuCamera, 50);

        audioMenu.addMenuItem("Back", new OptionsAction(game));
        audioMenu.addMenuItem("Sound", new SoundScreenAction(game));
        audioMenu.addMenuItem("Volume", new VolumeMenuAction(game));



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        audioMenu.run(delta);


        audioMenuViewport.apply();
        audioMenuCamera.update();
        game.batch.setProjectionMatrix(audioMenuCamera.combined);
        game.shapeRenderer.setProjectionMatrix(audioMenuCamera.combined);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        audioMenu.drawShape(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.begin();
        audioMenu.drawText(game.batch);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {
        audioMenuViewport.update(width, height);
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
