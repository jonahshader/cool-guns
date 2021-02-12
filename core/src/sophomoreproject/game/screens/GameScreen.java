package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.systems.GameClient;

public class GameScreen implements Screen {
    private Camera worldCamera;
    private Viewport worldViewport;
    private CoolGuns game;
    private GameClient gameClient;

    private int accountID;

    public GameScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;

        worldCamera = new OrthographicCamera();
        worldViewport = new ExtendViewport(800, 450, worldCamera);
        gameClient = new GameClient(accountID);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameClient.run(delta);
        worldViewport.apply();
        worldCamera.update();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.shapeRenderer.setProjectionMatrix(worldCamera.combined);

        // clear background
        // set clear color
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        // apply clear color to screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render sprites
        game.batch.begin();
        gameClient.draw(game.batch, game.shapeRenderer);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            System.out.println("Just clicked in world coords: " + worldCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f)).toString());
        }
    }

    @Override
    public void resize(int width, int height) {
        // update viewport and projection matrix
        worldViewport.update(width, height);

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
