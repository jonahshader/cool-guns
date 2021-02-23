package sophomoreproject.game.networking;

import com.esotericsoftware.kryonet.Connection;

public class ConnectedAccount {
    private Connection c;
    private Account account;

    public ConnectedAccount(Connection c, Account account) {
        this.c = c;
        this.account = account;
    }

    public Connection getC() {
        return c;
    }

    public Account getAccount() {
        return account;
    }
}
