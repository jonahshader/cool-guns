package sophomoreproject.game.singletons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class SettingsHandler {
    private static SettingsHandler instance;
    private Properties properties;

    private SettingsHandler() {
        properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("settings.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized SettingsHandler getInstance() {
        if (instance == null) {
            instance = new SettingsHandler();
        }

        return instance;
    }
}
