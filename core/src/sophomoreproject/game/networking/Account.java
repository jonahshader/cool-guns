package sophomoreproject.game.networking;

import java.io.Serializable;

public class Account implements Serializable {
    public String username;
    public String password;
    public int accountID;

    public Account(String username, String password, int accountID) {
        this.username = username;
        this.password = password;
        this.accountID = accountID;
    }
}
