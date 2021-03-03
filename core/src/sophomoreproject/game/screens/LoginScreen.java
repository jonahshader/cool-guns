package sophomoreproject.game.screens;

import com.badlogic.gdx.Gdx;
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
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.javafx.LoginWindow;

/**
 * this is the screen that first pops up when you launch the game.
 * it will ask the user to register or login to an account.
 * if the account already exists and user clicks "register", display "Account already exists!"
 * if the account doesn't exist and user clicks "login", display "Account doesn't exist!"
 * if the user attempts to register an account without a username or a password, display "Invalid username/password!"
 */

public class LoginScreen implements Screen {
    CoolGuns game;

    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));;


    Label loginLabel = new Label("Username:", skin);
    TextField userName = new TextField("", skin);
    Label passwordLabel = new Label("Password:", skin);
    TextField password = new TextField("", skin);
    TextButton loginButton = new TextButton("Login", skin);
    TextButton registerButton = new TextButton("Register", skin);
    Label errorMsg = new Label("", skin);

    public LoginScreen(CoolGuns game) {
        this.game = game;
    }


    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        loginLabel.setX(200);
        loginLabel.setY(425);
        loginLabel.setWidth(200);
        loginLabel.setHeight(30);

        userName.setX(200);
        userName.setY(400);
        userName.setWidth(210);
        userName.setHeight(30);
        stage.addActor(userName);

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
        stage.addActor(userName);
        stage.addActor(loginLabel);
        stage.addActor(passwordLabel);
        stage.addActor(loginButton);
        stage.addActor(registerButton);
        stage.addActor(errorMsg);



        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (userName.getText().length() < 7) {
                    errorMsg.setText("Your username must be at least 7 characters");
                    return;
                }

                if (password.getText().length() < 7) {
                    errorMsg.setText("Your password must be at least 7 characters");
                    return;
                }
            }
        });
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