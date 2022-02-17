package sophomoreproject.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sophomoreproject.game.CoolGuns;

public class DesktopLauncher {
	public static void main (String[] arg) {
	    if (arg.length == 1) {
	        // launch server
            ServerLauncher.main(arg);
        } else {
            LwjglApplicationConfiguration config = makeConfig();
            new LwjglApplication(new CoolGuns(false, 0), config);
        }

    }

    public static void mainLocalHost(int localPort) {
	    LwjglApplicationConfiguration config = makeConfig();
        new LwjglApplication(new CoolGuns(true, localPort), config);
    }

    private static LwjglApplicationConfiguration makeConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = false;
        config.foregroundFPS = 165;
        config.backgroundFPS = 60;
        config.width = 1280;
        config.height = 720;
//        config.fullscreen = true;
        return config;
    }
}
