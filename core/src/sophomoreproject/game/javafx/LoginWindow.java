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

    /**
     *
     * @author tommyjaboro
     */
    public class LoginWindow extends Application {


        @Override
        public void start(Stage primaryStage)
        {

            GridPane gridp = new GridPane();
            gridp.setAlignment(Pos.CENTER);
            gridp.setHgap(10);
            gridp.setVgap(10);


            Button button = new Button("Sign in");
            HBox Enter = new HBox(10);
            Enter.setAlignment(Pos.CENTER);
            Enter.getChildren().add(button);
            gridp.add(Enter, 1, 4);
            final Text actiontarget = new Text();
            gridp.add(actiontarget, 1, 6);

//            button.setOnAction((ActionEvent) -> {
//                actiontarget.setText("Test Successful");
//            });

            Text Desc = new Text("CoolGuns Login");
            Desc.setFont(Font.font("Times New Roman",25));
            gridp.add(Desc, 0, 0, 2, 1);

            TextField Userbox = new TextField();
            gridp.add(Userbox, 1, 1);

            Label user = new Label("Username:");
            gridp.add(user, 0, 1);

            PasswordField passBox = new PasswordField();
            gridp.add(passBox, 1, 2);

            Label passbox = new Label("Password:");
            gridp.add(passbox, 0, 2);



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


