package sophomoreproject.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.CoolGuns;

public class GameScreen implements Screen {
    private PerspectiveCamera worldCamera;
    private Viewport worldViewport;
    private CoolGuns game;

    private int accountID;

    public GameScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;

        worldCamera = new PerspectiveCamera();
        worldViewport = new ExtendViewport(800, 450, worldCamera);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        worldViewport.apply();
    }

    @Override
    public void resize(int width, int height) {
        // update viewport and projection matrix
        worldViewport.update(width, height);
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.shapeRenderer.setProjectionMatrix(worldCamera.combined);

        // clear background

        // render sprites
        game.batch.begin();
        // render here!
        game.batch.end();
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
