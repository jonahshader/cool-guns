package sophomoreproject.game.packets;

public class RequestGameData {
    int accountID;

    public RequestGameData(int accountID) {
        this.accountID = accountID;
    }

    public RequestGameData(){} // no arg constructor for KryoNet internal usage
}
