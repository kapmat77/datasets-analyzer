package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

//public class DatasetsAnalyzer extends Application {
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

public class DatasetsAnalyzer {

    public static void main(String[] args) {
        String path1 = "src/resources/eurofxref-hist.csv";
        String path2 = "src/resources/various-bitcoin-currency-statist.csv";
        Dataset datasetEuro = new Dataset(1, "Euro", path1);
        Dataset datasetBitcoin = new Dataset(2, "Bitcoin", path2);

        datasetEuro.printAttributes();
        datasetEuro.printRecords();

        datasetBitcoin.printAttributes();
        datasetBitcoin.printRecords();
    }
}
