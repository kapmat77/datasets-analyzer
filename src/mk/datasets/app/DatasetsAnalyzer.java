package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */

import java.util.ArrayList;
import java.util.List;

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

    private static List<Dataset> datasets = new ArrayList<>();

    public static void main(String[] args) {
        String path1 = "src/resources/eurofxref-hist.csv";
        String path2 = "src/resources/various-bitcoin-currency-statist.csv";
        Dataset datasetEuro = new Dataset(1, "Euro", path1);
        Dataset datasetBitcoin = new Dataset(2, "Bitcoin", path2);

        datasets.add(datasetEuro);
        datasets.add(datasetBitcoin);


//        datasetEuro.printAttributes();
//        datasetEuro.printRecords();

//        datasetBitcoin.printAttributes();
//        datasetBitcoin.printRecords();

        List<String> inputStringPrimitiveList = new ArrayList<>();
        inputStringPrimitiveList.add("P1:1.USD>1.13");
        inputStringPrimitiveList.add("P2:1.JPY>124");
        inputStringPrimitiveList.add("P3:2.value>=412");


        List<Primitive> primitiveList = new ArrayList<>();
        for (int i = 0; i<inputStringPrimitiveList.size(); i++) {
            Primitive primitive = Primitive.convertStringToPrimitive(inputStringPrimitiveList.get(i));
            primitive.findRecords(getDatasetById(primitive.getDatasetId()));
            primitive.toString();
            primitiveList.add(primitive);
        }

        if (Primitive.duplicatesExist(primitiveList)) {
            System.out.println("ERROR - duplicates detected");
        }

        String inputEvent = "E1:(P1 || P2) && P3";
        Event event = Event.convertStringToEvent(inputEvent);
        event.toString();

//        System.out.println(Event.getOpearionList("P5 && (P1 || (!P2 && P3) && (P7 || P10)) && P4 || (P11 && P12)"));
        System.out.println(Event.getOperionList("(P1 || !P2) && P3"));
    }

    private static Dataset getDatasetById(int id) {
        for (Dataset dataset: datasets) {
            if (dataset.getId() == id) {
                return dataset;
            }
        }
        return null;
    }
}
