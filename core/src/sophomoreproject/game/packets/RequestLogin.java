package sophomoreproject.game.packets;

public class RequestLogin {
    public String username;
    public String password;

    public RequestLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}