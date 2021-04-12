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

public class VolumeMenuScreen implements Screen {

    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private CoolGuns game;


    Label volumeLabel = new Label("Volume (from 1 to 100):", skin);
    TextField volume = new TextField("", skin);
    TextButton saveButton = new TextButton("Save", skin);
    TextButton backButton = new TextButton("Back", skin);
    Label savedMsg = new Label("", skin);

    public VolumeMenuScreen(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        volumeLabel.setX(200);
        volumeLabel.setY(425);
        volumeLabel.setWidth(200);
        volumeLabel.setHeight(30);

        volume.setX(200);
        volume.setY(400);
        volume.setWidth(210);
        volume.setHeight(30);
        stage.addActor(volume);

        saveButton.setX(250);
        saveButton.setY(350);
        saveButton.setWidth(100);
        saveButton.setHeight(30);

        backButton.setX(250);
        backButton.setY(250);
        backButton.setWidth(100);
        backButton.setHeight(30);

        savedMsg.setX(212);
        savedMsg.setY(300);
        savedMsg.setWidth(100);
        savedMsg.setHeight(30);

        stage.addActor(volumeLabel);
        stage.addActor(volume);
        stage.addActor(saveButton);
        stage.addActor(backButton);
        stage.addActor(savedMsg);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String soundText = volume.getText();
                savedMsg.setText("Volume is set to " + soundText + "%");
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AudioMenuScreen(game));
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


