package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/MainWindow.fxml"));
        primaryStage.setTitle("Datasets analyzer");
        primaryStage.setScene(new Scene(root, 1200, 695));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
