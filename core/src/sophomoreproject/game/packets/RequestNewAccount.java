package sophomoreproject.game.packets;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class RequestNewAccount {
    public String username;
    public String password;

    public RequestNewAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RequestNewAccount(TextField userName, TextField password){} // no arg constructor for KryoNet internal usage
}
