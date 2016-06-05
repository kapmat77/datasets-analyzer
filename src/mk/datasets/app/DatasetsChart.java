package mk.datasets.app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DatasetsChart extends Application {

	private List<Dataset> datasets = new ArrayList<>();

	@Override public void start(Stage stage) {
		stage.setTitle("Datasets chart");
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(0,5,1);
		final ScatterChart<String, Number> sc =
				new ScatterChart<>(xAxis,yAxis);
		xAxis.setLabel("Date");
		yAxis.setLabel("Dataset ID");
//		yAxis.setTickUnit(1);

		List<XYChart.Series> seriesList = new ArrayList<>();

		XYChart.Series timeSeries = new XYChart.Series();
		timeSeries.setName("Time");
		LocalDateTime oldest =  DatasetAnalyzer.getOldestDate();
		LocalDateTime newest =  DatasetAnalyzer.getNewestDate();
		LocalDateTime helpDate =  oldest;

		while (!helpDate.isAfter(newest)) {
			timeSeries.getData().add(new XYChart.Data(helpDate.toString(), 0));
			switch (DatasetAnalyzer.smallestDateFormat()) {
				case DAY:
					helpDate = helpDate.plusDays(1);
					break;
				case HOUR:
					helpDate = helpDate.plusHours(1);
					break;
				case MINUTE:
					helpDate = helpDate.plusMinutes(1);
					break;
				case SECOND:
					helpDate = helpDate.plusSeconds(1);
					break;
				default:
					helpDate = LocalDateTime.MIN;
			}
		}

		seriesList.add(timeSeries);

		for (int i = 0; i<datasets.size(); i++) {
			XYChart.Series series = new XYChart.Series();
			series.setName(datasets.get(i).getName());
			for (Record record: datasets.get(i).getRecords()) {
				series.getData().add(new XYChart.Data(record.getLocalDateTime().toString(), datasets.get(i).getId()));
			}
			seriesList.add(series);
		}



		sc.setPrefSize(1300, 600);
		for (XYChart.Series series: seriesList) {
			sc.getData().add(series);
		}
		Scene scene  = new Scene(new Group());
		final VBox vbox = new VBox();
//		final HBox hbox = new HBox();

//		final Button add = new Button("Add Series");
//		final Button remove = new Button("Remove Series");

//		hbox.setSpacing(1);
//		hbox.getChildren().addAll(add, remove);

		vbox.getChildren().addAll(sc);
//		hbox.setPadding(new Insets(1, 1, 1, 1));

		((Group)scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}
}