package sophomoreproject.game.desktop;

import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.singletons.CustomAssetManager;
import sophomoreproject.game.systems.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerLauncher {
    private static final double LOOP_TIME = 1/5.0;
    private static final double NANOS_TO_SECONDS = 1e-9;

    private static final long LOOP_TIME_NANOS = (long) (LOOP_TIME/NANOS_TO_SECONDS);


    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int port = -1;
        try {
            System.out.print("Enter port: ");
            port = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        int port = 1234;
        if (port != -1) {
            // create server
            ServerNetwork server = new ServerNetwork(port);
            GameServer gameServer = new GameServer(server);

            long lastTime = System.nanoTime();
            long time = lastTime;
            while(true) {
                do {
                    time = System.nanoTime();
                } while ((time - lastTime) < LOOP_TIME_NANOS);
                gameServer.run((float) Math.max(((time - lastTime) * NANOS_TO_SECONDS), LOOP_TIME * 0.5));
                lastTime = time;
            }
        }

    }
}
