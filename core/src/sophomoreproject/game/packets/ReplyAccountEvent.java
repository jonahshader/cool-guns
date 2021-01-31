package sophomoreproject.game.packets;

public class ReplyAccountEvent {
    public enum AccountEvent {
        ACCOUNT_CREATED,
        ACCOUNT_CREATE_FAILED,
        ACCOUNT_LOGGED_IN,
        ACCOUNT_LOG_IN_FAILED
    }

    public AccountEvent event;
    public long accountID;

    public ReplyAccountEvent(AccountEvent event, long accountID) {
        this.event = event;
        this.accountID = accountID;
    }
}
