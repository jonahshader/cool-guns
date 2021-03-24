package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sophomoreproject.game.CoolGuns;

public class FrameRateScreen implements Screen {

    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private CoolGuns game;
    private int accountID;
    //private long diff, start = System.currentTimeMillis();

    /*public void sleep(int fps) {
        if(fps>0){
            diff = System.currentTimeMillis() - start;
            long targetDelay = 1000/fps;
            if (diff < targetDelay) {
                try{
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {}
            }
            start = System.currentTimeMillis();
        }
    }*/

    Label frameRateLabel = new Label("Set Frame Rate (FPS):", skin);
    TextField frameRate = new TextField("", skin);
    TextButton saveButton = new TextButton("Save", skin);
    TextButton backButton = new TextButton("Back", skin);
    Label savedMsg = new Label("", skin);

    public FrameRateScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        frameRateLabel.setX(200);
        frameRateLabel.setY(425);
        frameRateLabel.setWidth(200);
        frameRateLabel.setHeight(30);

        frameRate.setX(200);
        frameRate.setY(400);
        frameRate.setWidth(210);
        frameRate.setHeight(30);
        stage.addActor(frameRate);

        saveButton.setX(250);
        saveButton.setY(350);
        saveButton.setWidth(100);
        saveButton.setHeight(30);

        backButton.setX(250);
        backButton.setY(250);
        backButton.setWidth(100);
        backButton.setHeight(30);

        savedMsg.setX(250);
        savedMsg.setY(300);
        savedMsg.setWidth(100);
        savedMsg.setHeight(30);

        stage.addActor(frameRateLabel);
        stage.addActor(frameRate);
        stage.addActor(saveButton);
        stage.addActor(backButton);
        stage.addActor(savedMsg);

        saveButton.addListener(new ClickListener() {

            String frame_Rate = frameRate.getText();
            int frame__Rate = Integer.parseInt(frame_Rate);
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //sleep(frame__Rate);
                savedMsg.setText("Frame Rate is set to " + frame_Rate + "FPS");
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ScreenMenuScreen(game, accountID));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}
