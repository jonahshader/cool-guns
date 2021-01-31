package sophomoreproject.game.desktop;

import sophomoreproject.game.networking.ServerNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerLauncher {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String ip;
        int port = -1;

        try {
            System.out.print("Enter port: ");
            port = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (port != -1) {
            // create server
            ServerNetwork server = new ServerNetwork(port);

            try {
                while(true) {
                    Thread.sleep(20);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
