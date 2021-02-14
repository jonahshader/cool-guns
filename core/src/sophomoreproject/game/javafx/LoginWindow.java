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


public class LoginWindow extends Application {


    @Override
    public void start(Stage primaryStage)
    {
        GridPane gridp = new GridPane();
        gridp.setAlignment(Pos.CENTER);
        gridp.setHgap(10);
        gridp.setVgap(10);


        Button signIn = new Button("Sign in");
        HBox Enter = new HBox(10);
        Enter.setAlignment(Pos.CENTER);
        final Text actiontarget = new Text();

        signIn.setOnAction((ActionEvent) -> {
            actiontarget.setText("Test Successful");
        });

        Text Desc = new Text("CoolGuns Login");
        Desc.setFont(Font.font("Times New Roman",25));
        gridp.add(Desc, 0, 0, 2, 1);

        TextField userField = new TextField();
        gridp.add(userField, 1, 1);

        Label userLabel = new Label("Username:");
        gridp.add(userLabel, 0, 1);

        PasswordField passField = new PasswordField();
        gridp.add(passField, 1, 2);

        Label passLabel = new Label("Password:");
        gridp.add(passLabel, 0, 2);


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


