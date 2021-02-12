package sophomoreproject.game.packets;

public class RequestNewAccount {
    public String username;
    public String password;

    public RequestNewAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RequestNewAccount(){} // no arg constructor for KryoNet internal usage
}
