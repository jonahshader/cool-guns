package sophomoreproject.game.screens;

import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TempBypassScreen implements Screen {
    private CoolGuns game;
    private int accountID;
    boolean loggedIn = false;

    public TempBypassScreen(CoolGuns game) {
        this.game = game;

        Scanner scanner = new Scanner(System.in);
        boolean connected = false;
        int accountID = -1;

        while (!connected) {
            System.out.print("Enter ip (without port): ");
            String ip = scanner.nextLine();
            System.out.print("Enter port: ");
            int port = scanner.nextInt();

            if (ClientNetwork.getInstance().tryConnect(ip, port)) {
                connected = true;
            } else {
                System.out.println("Connection failed!");
            }
        }
//        if (!ClientNetwork.getInstance().tryConnect("127.0.0.1", 1234)) System.exit(-1);
//        connected = true;

        final ReplyAccountEvent[] rEvent = {null};
        ClientNetwork.getInstance().addListener(new Listener(){
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof ReplyAccountEvent) {
                    rEvent[0] = (ReplyAccountEvent) o;
                }
            }
        });

        while (!loggedIn) {
            System.out.print("(R)egister or (L)ogin: ");
                String nextLine = scanner.nextLine();
//                System.out.println("Next line...?:" + nextLine);
            while (nextLine.length() == 0) {
                nextLine = scanner.nextLine();
            }
            boolean register = nextLine.toLowerCase().toCharArray()[0] == 'r';
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (register) {
                ClientNetwork.getInstance().sendPacket(new RequestNewAccount(username, password));
            } else {
                ClientNetwork.getInstance().sendPacket(new RequestLogin(username, password));
            }
            while (rEvent[0] == null) {} // chill until we get a reply from the server

            switch (rEvent[0].event) {
                case ACCOUNT_CREATED:
                    System.out.println("Account created successfully! Please login");
                    break;
                case ACCOUNT_CREATE_FAILED:
                    System.out.println("Account create failed! Account already exists!");
                    break;
                case ACCOUNT_LOGGED_IN:
                    System.out.println("Logged in successfully!");
                    accountID = rEvent[0].accountID;
                    loggedIn = true;
                    break;
                case ACCOUNT_LOG_IN_FAILED:
                    System.out.println("Log in failed! Account does not exists!");
                    break;
                default:
                    break;
            }
            rEvent[0] = null; // clear this so that we wait for the next packet again (if nessesary)
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (loggedIn) {
            game.setScreen(new GameScreen(game, accountID));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
