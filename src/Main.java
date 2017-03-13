import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

/**
 * Created by fajek on 2/16/17.
 */
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
        primaryStage.setTitle("Aplikacja");
        primaryStage.setScene(new Scene(root, 750, 750));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
