package sophomoreproject.game.desktop;

public class QuickLauncher {
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> ServerLauncher.main(new String[]{"1234"}));
        serverThread.start();
        DesktopLauncher.mainLocalHost();
    }
}
