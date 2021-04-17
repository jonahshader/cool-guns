package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;


/**
 * this is the screen that first pops up when you launch the game.
 * it will ask the user to register or login to an account.
 * if the account already exists and user clicks "register", display "Account already exists!"
 * if the account doesn't exist and user clicks "login", display "Account doesn't exist!"
 * if the user attempts to register an account without a username or a password, display "Invalid username/password!"
 */

public class LoginScreen implements Screen {

    private final CoolGuns game;
    private int accountID = -1;
    boolean loggedIn = false;


    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));



    Label loginLabel = new Label("Username:", skin);
    TextField username = new TextField("", skin);
    Label passwordLabel = new Label("Password:", skin);
    TextField password = new TextField("", skin);
    TextButton loginButton = new TextButton("Login", skin);
    TextButton registerButton = new TextButton("Register", skin);
    Label errorMsg = new Label("", skin);
    final AtomicReference<ReplyAccountEvent> rEvent = new AtomicReference<>(null);

    public LoginScreen(CoolGuns game) {
        this.game = game;
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        loginLabel.setX(200);
        loginLabel.setY(425);
        loginLabel.setWidth(200);
        loginLabel.setHeight(30);

        username.setX(200);
        username.setY(400);
        username.setWidth(210);
        username.setHeight(30);
        stage.addActor(username);

        passwordLabel.setX(200);
        passwordLabel.setY(370);
        passwordLabel.setWidth(200);
        passwordLabel.setHeight(30);

        password.setX(200);
        password.setY(345);
        password.setWidth(210);
        password.setHeight(30);

        loginButton.setX(315);
        loginButton.setY(285);
        loginButton.setWidth(100);
        loginButton.setHeight(30);

        registerButton.setX(200);
        registerButton.setY(285);
        registerButton.setWidth(100);
        registerButton.setHeight(30);

        errorMsg.setX(200);
        errorMsg.setY(200);
        errorMsg.setWidth(100);
        errorMsg.setHeight(30);

        stage.addActor(password);
        stage.addActor(username);
        stage.addActor(loginLabel);
        stage.addActor(passwordLabel);
        stage.addActor(loginButton);
        stage.addActor(registerButton);
        stage.addActor(errorMsg);



        ClientNetwork.getInstance().addListener(new Listener() {
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof ReplyAccountEvent) {
                    rEvent.set((ReplyAccountEvent) o);
                }
            }
        });


        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String u = username.getText();
                String p = password.getText();

                ClientNetwork.getInstance().sendPacket(new RequestNewAccount(u, p));

                try {
                    while (rEvent.get() == null) {
                        Thread.sleep(250);
                        System.out.println(".");
                    } // chill until we get a reply from the server
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                switch (rEvent.get().event) {
                    case ACCOUNT_CREATED:
                        errorMsg.setText("Account created successfully! Please login");
                        break;
                    case ACCOUNT_CREATE_FAILED:
                        errorMsg.setText("Account create failed! Account already exists!");
                        break;
                    case ACCOUNT_LOGGED_IN:
                        errorMsg.setText("Logged in successfully!");
                        accountID = rEvent.get().accountID;
                        loggedIn = true;
                        break;
                    case ACCOUNT_LOG_IN_FAILED:
                        errorMsg.setText("Log in failed! Account does not exists!");
                        break;
                    case ACCOUNT_ALREADY_LOGGED_IN:
                        errorMsg.setText("Log in failed! Account current in use!");
                        break;
                    default:
                        break;
                }
                rEvent.set(null); // clear this so that we wait for the next packet again (if nessesary)

            }

        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                login();
            }
        });
    }

    private void login() {
        String u = username.getText();
        String p = password.getText();

        ClientNetwork.getInstance().sendPacket(new RequestLogin(u, p));

        try {
            while (rEvent.get() == null) {
                Thread.sleep(50);
                System.out.println(".");
            } // chill until we get a reply from the server
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (rEvent.get().event) {
            case ACCOUNT_CREATED:
                errorMsg.setText("Account created successfully! Please login");
                break;
            case ACCOUNT_CREATE_FAILED:
                errorMsg.setText("Account create failed! Account already exists!");
                break;
            case ACCOUNT_LOGGED_IN:
                errorMsg.setText("Logged in successfully!");
                accountID = rEvent.get().accountID;
                loggedIn = true;
                game.setScreen(new GameScreen(game, accountID));
                break;
            case ACCOUNT_LOG_IN_FAILED:
                errorMsg.setText("Log in failed! Account does not exists!");
                break;
            case ACCOUNT_ALREADY_LOGGED_IN:
                errorMsg.setText("Log in failed! Account current in use!");
                break;
            default:
                break;
        }
        rEvent.set(null); // clear this so that we wait for the next packet again (if nessesary)
    }

    @Override
    public void render(float delta) {
        // set clear color
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        // apply clear color to screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            login();
        }
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
