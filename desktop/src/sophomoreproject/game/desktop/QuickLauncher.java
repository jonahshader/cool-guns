package sophomoreproject.game.desktop;

public class QuickLauncher {
    private static final int LOCAL_PORT = 25565;
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> ServerLauncher.main(new String[]{""+LOCAL_PORT}));
        serverThread.start();
        DesktopLauncher.mainLocalHost(LOCAL_PORT);
    }
}
