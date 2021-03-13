package sophomoreproject.game.packets;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class RequestLogin {
    public String username;
    public String password;

    public RequestLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RequestLogin(){} // no arg constructor for KryoNet internal usage
}
