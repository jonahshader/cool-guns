package sophomoreproject.game.desktop;

import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.networking.serverlisteners.RelaySendOnlyPacketsListener;
import sophomoreproject.game.systems.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerLauncher {
    private static final double LOOP_TIME = 1/40.0;
    private static final double NANOS_TO_SECONDS = 1e-9;

    private static final long LOOP_TIME_NANOS = (long) (LOOP_TIME/NANOS_TO_SECONDS);


    public static void main(String[] args) {
        int port = -1;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                System.out.print("Enter port: ");
                port = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (port != -1) {
            // create server
            ServerNetwork server = new ServerNetwork(port);
            GameServer gameServer = new GameServer(server);

            // add some listeners here (that can't be added elsewhere)
            server.addListener(new AccountListener(server.getAccounts(),
                    server.getUsersLoggedIn(),
                    server.getConnectionIdToAccountID(),
                    gameServer));

            server.addListener(new RelaySendOnlyPacketsListener(server,
                    gameServer));

            long lastTime = System.nanoTime();
            long time;
            while(true) {
                do {
                    time = System.nanoTime();
                } while ((time - lastTime) < LOOP_TIME_NANOS);
                gameServer.run((float) Math.max(((time - lastTime) * NANOS_TO_SECONDS), LOOP_TIME * 0.00005));
                lastTime = time;
            }
        }
    }
}
