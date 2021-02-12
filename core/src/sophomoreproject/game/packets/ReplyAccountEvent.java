package sophomoreproject.game.packets;

public class ReplyAccountEvent {
    public enum AccountEvent {
        ACCOUNT_CREATED,
        ACCOUNT_CREATE_FAILED,
        ACCOUNT_LOGGED_IN,
        ACCOUNT_LOG_IN_FAILED
    }

    public AccountEvent event;
    public int accountID;

    public ReplyAccountEvent(AccountEvent event, int accountID) {
        this.event = event;
        this.accountID = accountID;
    }

    public ReplyAccountEvent(){} // no arg constructor for KryoNet internal usage
}
