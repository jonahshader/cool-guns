package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sophomoreproject.game.CoolGuns;

public class SoundScreen implements Screen {

    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private CoolGuns game;
    private int accountID;

    Label soundLabel = new Label("Sound (on / off):", skin);
    TextField sound = new TextField("", skin);
    TextButton saveButton = new TextButton("Save", skin);
    TextButton backButton = new TextButton("Back", skin);
    Label savedMsg = new Label("", skin);

    public SoundScreen(CoolGuns game, int accountID) {
        this.game = game;
        this.accountID = accountID;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        soundLabel.setX(200);
        soundLabel.setY(425);
        soundLabel.setWidth(200);
        soundLabel.setHeight(30);

        sound.setX(200);
        sound.setY(400);
        sound.setWidth(210);
        sound.setHeight(30);
        stage.addActor(sound);

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

        stage.addActor(soundLabel);
        stage.addActor(sound);
        stage.addActor(saveButton);
        stage.addActor(backButton);
        stage.addActor(savedMsg);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String soundText = sound.getText();
                if (soundText.equalsIgnoreCase("on")) {
                    savedMsg.setText("Sound is On!");
                }
                else{
                    savedMsg.setText("Sound is Off!");
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AudioMenuScreen(game, accountID));
            }
        });
    }


        @Override
        public void render (float delta){
            Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.draw();
        }

        @Override
        public void resize ( int width, int height){
            stage.getViewport().update(width, height, true);
        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {

        }

        @Override
        public void dispose () {
            stage.dispose();
            skin.dispose();
        }
    }
