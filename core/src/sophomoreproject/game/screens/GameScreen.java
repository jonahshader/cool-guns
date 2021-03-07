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
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.singletons.TextDisplay;
import sophomoreproject.game.systems.GameClient;
import sophomoreproject.game.systems.PlayerController;
import sophomoreproject.game.systems.mapstuff.Map;

public class GameScreen implements Screen {
    private OrthographicCamera worldCamera;
    private Camera hudCamera;
    private Viewport worldViewport;
    private Viewport hudViewport;
    private CoolGuns game;
    private GameClient gameClient;
    private Map map;



    private int accountID;

    public GameScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;

        worldCamera = new OrthographicCamera();
        worldViewport = new ExtendViewport(640, 360, worldCamera);

        hudCamera = new OrthographicCamera();
        hudViewport = new ExtendViewport(640, 360, hudCamera);
        gameClient = new GameClient(accountID);
        Gdx.input.setInputProcessor(PlayerController.getInstance());
        PlayerController.getInstance().setCam(worldCamera);

        map = new Map(game, 81528512);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        PlayerController.getInstance().run(delta);
        map.run();
        gameClient.run(delta);
        worldViewport.apply();
        worldCamera.update();


        // clear background
        // set clear color
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        // apply clear color to screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render sprites
        map.render(worldCamera);

        game.batch.setProjectionMatrix(worldCamera.combined);
        game.shapeRenderer.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        gameClient.draw(game.batch, game.shapeRenderer);
        game.batch.end();

        // render HUD
        hudViewport.apply(true);
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        TextDisplay.getInstance().draw(game.batch, hudViewport);
        game.batch.end();


        if (Gdx.input.justTouched()) {
            System.out.println("Just clicked in world coords: " + worldCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f)).toString());
        }
    }

    @Override
    public void resize(int width, int height) {
        // update viewport and projection matrix
        worldViewport.update(width, height);
        hudViewport.update(width, height);
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
