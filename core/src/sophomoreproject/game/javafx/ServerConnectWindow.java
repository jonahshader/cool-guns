package sophomoreproject.game.javafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sophomoreproject.game.networking.ClientNetwork;


public class ServerConnectWindow extends Application {


    @Override
    public void start(Stage primaryStage)
    {

        GridPane gridp = new GridPane();
        gridp.setAlignment(Pos.CENTER);
        gridp.setHgap(10);
        gridp.setVgap(10);


        Button connect = new Button("Connect");
        HBox Enter = new HBox(10);
        Enter.setAlignment(Pos.CENTER);
        final Text actiontarget = new Text();

        Text Desc = new Text("CoolGuns Servers");
        Desc.setFont(Font.font("Times New Roman",25));
        gridp.add(Desc, 0, 0, 2, 1);

        TextField ipField = new TextField();
        gridp.add(ipField, 1, 1);

        Label ipLabel = new Label("IP Address:");
        gridp.add(ipLabel, 0, 1);

        TextField portField = new PasswordField();
        gridp.add(portField, 1, 2);

        Label portLabel = new Label("Port:");
        gridp.add(portLabel, 0, 2);


        connect.setOnAction((ActionEvent) -> {
            int port = Integer.parseInt(portField.getCharacters().toString());
            String ip = ipField.getCharacters().toString();
            if (ClientNetwork.getInstance().tryConnect(ip, port))
                primaryStage.close();
            else
                actiontarget.setText("Connect Unsuccessful");

        });


        Scene scene = new Scene(gridp, 300, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
