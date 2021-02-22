package sophomoreproject.game.packets;

public class UpdateSleepState {
    public int networkID;
    public boolean sleeping;

    public UpdateSleepState(int networkID, boolean sleeping) {
        this.networkID = networkID;
        this.sleeping = sleeping;
    }

    public UpdateSleepState() { }

    @Override
    public String toString() {
        return "ID: " + networkID + " state: " + (sleeping ? "sleeping" : "awake");
    }
}
