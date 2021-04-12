package sophomoreproject.game.menu.menuactions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import sophomoreproject.game.CoolGuns;
import sophomoreproject.game.menu.MenuAction;
import sophomoreproject.game.networking.ClientNetwork;
import sophomoreproject.game.networking.ServerNetwork;
import sophomoreproject.game.networking.serverlisteners.AccountListener;
import sophomoreproject.game.networking.serverlisteners.RelaySendOnlyPacketsListener;
import sophomoreproject.game.packets.ReplyAccountEvent;
import sophomoreproject.game.packets.RequestLogin;
import sophomoreproject.game.packets.RequestNewAccount;
import sophomoreproject.game.screens.GameScreen;
import sophomoreproject.game.systems.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlaySingleGameAction implements MenuAction {
    private CoolGuns game;
    private int accountID;
    private static final double LOOP_TIME = 1 / 40.0;
    private static final double NANOS_TO_SECONDS = 1e-9;
    private static final int LOCAL_PORT = 25565;
    private static final long LOOP_TIME_NANOS = (long) (LOOP_TIME / NANOS_TO_SECONDS);

    public PlaySingleGameAction(CoolGuns game) {
        this.game = game;

    }

    @Override
    public void execute() {
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                int port = LOCAL_PORT;


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
                    while (true) {
                        do {
                            time = System.nanoTime();
                        } while ((time - lastTime) < LOOP_TIME_NANOS);
                        gameServer.run((float) Math.max(((time - lastTime) * NANOS_TO_SECONDS), LOOP_TIME * 0.00005));
                        lastTime = time;
                    }
                }
            }
        };
        serverThread.start();
        final ReplyAccountEvent[] rEvent = {null};
        ClientNetwork.getInstance().addListener(new Listener(){
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof ReplyAccountEvent) {
                    rEvent[0] = (ReplyAccountEvent) o;
                }
            }
        });

        if(ClientNetwork.getInstance().tryConnect("localhost", LOCAL_PORT)){
            System.out.println("Successfully connected to internal server!");
        } else {
            System.out.println("Error! Failed to connect to internal server.");
        }

        ClientNetwork.getInstance().sendPacket(new RequestNewAccount("", ""));
        try {
            while (rEvent[0] == null) {
                Thread.sleep(250);
                System.out.println(".");
            } // chill until we get a reply from the server
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

                break;
            case ACCOUNT_LOG_IN_FAILED:
                System.out.println("Log in failed! Account does not exists!");
                break;
            case ACCOUNT_ALREADY_LOGGED_IN:
                System.out.println("Log in failed! Account current in use!");
                break;
            default:
                break;
        }
        ClientNetwork.getInstance().sendPacket(new RequestLogin("", ""));
        try {
            while (rEvent[0] == null) {
                Thread.sleep(250);
                System.out.println(".");
            } // chill until we get a reply from the server
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

                break;
            case ACCOUNT_LOG_IN_FAILED:
                System.out.println("Log in failed! Account does not exists!");
                break;
            case ACCOUNT_ALREADY_LOGGED_IN:
                System.out.println("Log in failed! Account current in use!");
                break;
            default:
                break;
        }
        game.setScreen(new GameScreen(game, accountID));
    }
}
