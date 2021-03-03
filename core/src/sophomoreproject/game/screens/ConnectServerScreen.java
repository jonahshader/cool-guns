package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.javafx.LoginWindow;
import sophomoreproject.game.javafx.ServerConnectWindow;
import sophomoreproject.game.networking.ClientNetwork;

import java.awt.event.ActionEvent;


public class ConnectServerScreen implements Screen {
    CoolGuns game;


    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));;



    Label ipLabel = new Label("IP:", skin);
    TextField ip = new TextField("", skin);
    TextField port = new TextField("", skin);
    Label portLabel = new Label("Port:", skin);
    TextButton connectButton = new TextButton("Connect", skin);
    Label errorMsg = new Label("", skin);

    public ConnectServerScreen(CoolGuns game) {
        this.game = game;

    }


    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        ipLabel.setX(200);
        ipLabel.setY(425);
        ipLabel.setWidth(200);
        ipLabel.setHeight(30);

        ip.setX(200);
        ip.setY(400);
        ip.setWidth(210);
        ip.setHeight(30);
        stage.addActor(ip);

        portLabel.setX(200);
        portLabel.setY(370);
        portLabel.setWidth(200);
        portLabel.setHeight(30);

        port.setX(200);
        port.setY(345);
        port.setWidth(210);
        port.setHeight(30);

        connectButton.setX(250);
        connectButton.setY(285);
        connectButton.setWidth(100);
        connectButton.setHeight(30);

        errorMsg.setX(200);
        errorMsg.setY(200);
        errorMsg.setWidth(100);
        errorMsg.setHeight(30);


        stage.addActor(port);
        stage.addActor(ip);
        stage.addActor(ipLabel);
        stage.addActor(portLabel);
        stage.addActor(connectButton);
        stage.addActor(errorMsg);

            connectButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int p = Integer.parseInt(port.getText());
                    String i = ip.getText();
                    if (ClientNetwork.getInstance().tryConnect(i, p))
                        game.setScreen( new LoginScreen(game));
                    else
                        errorMsg.setText("Connect Unsuccessful");
                }
            });




//        connectButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String ip = ipLabel.getText().toString();
//                int port = Integer.parseInt(portLabel.getText().toString());
//                if (ClientNetwork.getInstance().tryConnect(ip, port))
//                    errorMsg.setText("Ip must be 9 digits");

//            }
//        });

    }


    @Override
    public void render(float delta) {
        // set clear color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // apply clear color to screen
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
