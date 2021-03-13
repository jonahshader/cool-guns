package sophomoreproject.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sophomoreproject.game.CoolGuns;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = makeConfig();
		new LwjglApplication(new CoolGuns(false), config);
    }

    public static void mainLocalHost() {
	    LwjglApplicationConfiguration config = makeConfig();
        new LwjglApplication(new CoolGuns(true), config);
    }

    private static LwjglApplicationConfiguration makeConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.foregroundFPS = 165;
        return config;
    }
}
