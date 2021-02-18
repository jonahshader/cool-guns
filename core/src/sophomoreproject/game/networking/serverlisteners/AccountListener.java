package sophomoreproject.game.networking.serverlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.Account;
import sophomoreproject.game.networking.Accounts;
import sophomoreproject.game.networking.ConnectedAccount;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;

import java.util.HashMap;

public class AccountListener implements Listener {
    private Accounts accounts;
    private HashMap<Integer, ConnectedAccount> usersLoggedIn;
    private HashMap<Connection, Integer> connectionToAccountID;

    public AccountListener(Accounts accounts, HashMap<Integer, ConnectedAccount> usersLoggedIn) {
        this.accounts = accounts;
        this.usersLoggedIn = usersLoggedIn;
        connectionToAccountID = new HashMap<>();
    }

    @Override
    public void disconnected(Connection connection) {
        if (connectionToAccountID.containsKey(connection)) {
            int disconnectedAccountID = connectionToAccountID.get(connection);
            usersLoggedIn.remove(disconnectedAccountID);
            connectionToAccountID.remove(connection);
        }
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof RequestNewAccount) {
            RequestNewAccount packet = (RequestNewAccount) o; // cast object to more specific class
            ReplyAccountEvent rae;
            if (accounts.tryAddAccount(packet.username, packet.password)) {
                int accountID = accounts.tryGetAccountID(packet.username, packet.password);
                // success
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_CREATED, accountID);
            } else {
                // fail
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_CREATE_FAILED, -1);
            }
            // reply with event
            c.sendTCP(rae);
        } else if (o instanceof RequestLogin) {
            RequestLogin packet = (RequestLogin) o;
            int accountID = accounts.tryGetAccountID(packet.username, packet.password);
            ReplyAccountEvent rae;

            if (usersLoggedIn.containsKey(accountID)) {
                // ERROR. login failed (already logged in
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_ALREADY_LOGGED_IN, -1);
            } else if (accountID < 0) {
                // ERROR. login failed
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_LOG_IN_FAILED, -1);
            } else {
                // success
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_LOGGED_IN, accountID);

                // add account to collection of logged in users
                usersLoggedIn.put(accountID, new ConnectedAccount(c, accounts.getAccountByUsername(packet.username)));
                connectionToAccountID.put(c, accountID);
            }
            // reply with event
            c.sendTCP(rae);
        }
    }
}
