package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */

import java.util.ArrayList;
import java.util.List;

//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }


//    public static void main(String[] args) {
//        launch(args);
//    }

public class Main {
    public static void main(String[] args) {
        DatasetAnalyzer datasetAnalyzer = new DatasetAnalyzer();
        datasetAnalyzer.testAnalyzer();
    }
}
