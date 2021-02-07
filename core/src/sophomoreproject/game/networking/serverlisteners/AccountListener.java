package sophomoreproject.game.networking.serverlisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.networking.Accounts;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;

public class AccountListener extends Listener {
    private Accounts accounts;

    public AccountListener(Accounts accounts) {
        this.accounts = accounts;
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
            if (accountID < 0) {
                // ERROR. login failed
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_LOG_IN_FAILED, -1);
            } else {
                // success
                rae = new ReplyAccountEvent(ReplyAccountEvent.AccountEvent.ACCOUNT_LOGGED_IN, accountID);
            }
            // reply with event
            c.sendTCP(rae);
        }
    }
}
