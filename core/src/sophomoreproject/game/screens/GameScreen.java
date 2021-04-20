package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.gameobjects.Player;
import sophomoreproject.game.singletons.*;
import sophomoreproject.game.systems.GameClient;
import sophomoreproject.game.systems.PlayerController;
import sophomoreproject.game.systems.mapstuff.Map;
import sophomoreproject.game.systems.marker.MarkerSystem;

import static sophomoreproject.game.singletons.CustomAssetManager.SPRITE_PACK;
import static sophomoreproject.game.systems.GameServer.GAME_SEED;

public class GameScreen implements Screen {
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 540;
    public static final int HUD_WIDTH = 640;
    public static final int HUD_HEIGHT = 360;
    private OrthographicCamera worldCamera;
    private Camera hudCamera;
    private Viewport worldViewport;
    private Viewport hudViewport;
    private CoolGuns game;
    private GameClient gameClient;
    private Map map;
    private Sprite spawnIcon;

    private int accountID;
    private boolean connectionError = false;
    private boolean tutorial;

    public GameScreen(CoolGuns game, int accountID, boolean tutorial) {
        this.game = game;
        this.accountID = accountID;
        this.tutorial = tutorial;

        worldCamera = new OrthographicCamera();
        worldViewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT, worldCamera);

        hudCamera = new OrthographicCamera();
        hudViewport = new ExtendViewport(HUD_WIDTH, HUD_HEIGHT, hudCamera);
        gameClient = new GameClient(accountID);
        Gdx.input.setInputProcessor(PlayerController.getInstance());
        PlayerController.getInstance().setCam(worldCamera);
        map = new Map(game, GAME_SEED);
        PlayerController.getInstance().setMapGen(map.getMapGen());



        SoundSystem.getInstance().startMusic();

        TextureAtlas atlas = CustomAssetManager.getInstance().manager.get(SPRITE_PACK);
        spawnIcon = new Sprite(atlas.findRegion("spawn_icon"));
        spawnIcon.setOriginCenter();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        map.run();
        gameClient.run(delta);
        PlayerController.getInstance().run(delta);
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
        spawnIcon.draw(game.batch);

        gameClient.draw(delta, game.batch, game.shapeRenderer);
        // render markers
        MarkerSystem.getInstance().render(game.batch, worldViewport, worldCamera);
        game.batch.end();

        // render HUD
        hudViewport.apply(true);
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        TextDisplay.getInstance().draw(game.batch, hudViewport);
        game.batch.end();

        HUD.getInstance().draw(game.batch);

        if (tutorial) {
            hudViewport.apply(true);
            game.batch.setProjectionMatrix(hudCamera.combined);
            game.batch.begin();
            Tutorial.getInstance().draw(game.batch);
            game.batch.end();
        }

//        if (Gdx.input.justTouched()) {
//            System.out.println("Just clicked in world coords: " + worldCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f)).toString());
//        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        // update viewport and projection matrix
        worldViewport.update(width, height);
        hudViewport.update(width, height);
        HUD.getInstance().resize(width, height);
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
