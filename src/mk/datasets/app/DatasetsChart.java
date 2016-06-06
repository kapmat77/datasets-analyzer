package mk.datasets.app;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatasetsChart extends Application {

	private List<Dataset> datasets = new ArrayList<>();
	private String[] days;

	@Override
	public void start(Stage stage) {

		List<XYChart.Series> seriesList = new ArrayList<>();
		XYChart.Series timeSeries = new XYChart.Series();
		timeSeries.setName("Time");

		List<LocalDateTime> dateList = new ArrayList<>();
		LocalDateTime oldest = DatasetAnalyzer.getOldestDate();
		LocalDateTime newest = DatasetAnalyzer.getNewestDate();
		LocalDateTime helpDate = oldest;

		while (!helpDate.isAfter(newest)) {
			dateList.add(helpDate);
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

		ScatterChart chart = chart(dateList);
		NumberAxis xAxis = (NumberAxis) chart.getXAxis();

		Slider slider1 = new Slider();
		slider1.setMin(xAxis.getLowerBound());
		slider1.setMax(xAxis.getUpperBound());
		slider1.setValue(slider1.getMin());
		slider1.setMajorTickUnit(1);
		slider1.setMinorTickCount(0);
		slider1.setShowTickMarks(true);
		slider1.setSnapToTicks(true);
		Slider slider2 = new Slider();
		slider2.setMin(xAxis.getLowerBound());
		slider2.setMax(xAxis.getUpperBound());
		slider2.setValue(slider1.getMax());
		slider2.setShowTickMarks(true);
		slider2.setSnapToTicks(true);
		slider2.setMajorTickUnit(1);
		slider2.setMinorTickCount(0);

		Label low = new Label(DatasetAnalyzer.getOldestDate().toString());
		Label hight = new Label(DatasetAnalyzer.getNewestDate().toString());
		Label space = new Label("   -   ");

		ChangeListener<Number> valueChangeListner = (ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			int lowerBound = (int) Math.min(slider1.getValue(), slider2.getValue());
			int upperBound = (int) Math.max(slider1.getValue(), slider2.getValue());
			xAxis.setLowerBound(lowerBound);
			xAxis.setUpperBound(upperBound);
			low.setText(days[lowerBound]);
			hight.setText(days[upperBound]);
		};
		slider1.valueProperty().addListener(valueChangeListner);
		slider2.valueProperty().addListener(valueChangeListner);
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 6; -fx-spacing: 6;");

		HBox hbox = new HBox();
		hbox.setCenterShape(true);
		hbox.setStyle("-fx-alignment: center; -fx-font-size: 20px; -fx-font-weight: bold");
		hbox.getChildren().addAll(low, space, hight);
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(chart);
		vbox.getChildren().addAll(hbox, slider1, slider2);
		borderPane.setBottom(vbox);

		Scene scene = new Scene(borderPane, 1200, 600);
		stage.setScene(scene);
		stage.show();
	}

	private ScatterChart<Number, Number> chart(List<LocalDateTime> dateList) {
		days = new String[dateList.size()];
		for (int i = 0; i < dateList.size(); i++) {
			days[i] = dateList.get(i).toString();
		}

		NumberAxis xAxis = new NumberAxis(0, days.length - 1, 1);
		xAxis.setAutoRanging(false);
		xAxis.setTickLabelFormatter(new StringConverter<Number>() {

			@Override
			public String toString(Number t) {
				int index = t.intValue();
				return (index >= 0 && index < days.length) ? days[index] : null;
			}

			@Override
			public Number fromString(String string) {
				throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
			}
		});
		xAxis.setTickLabelRotation(90);

		final NumberAxis yAxis = new NumberAxis(0, datasets.size() + 1, 1);
		xAxis.setLabel("Date");
		yAxis.setLabel("Dataset ID");
		final ScatterChart<Number, Number> sc =
				new ScatterChart<>(xAxis, yAxis);
		sc.setPrefSize(1200, 210);

		List<XYChart.Series> seriesList = new ArrayList<>();

		for (int i = 0; i < datasets.size(); i++) {
			XYChart.Series series = new XYChart.Series();
			series.setName(datasets.get(i).getName());
			for (Record record : datasets.get(i).getRecords()) {
				series.getData().add(new XYChart.Data(getIndex(record.getLocalDateTime(), days), datasets.get(i).getId()));
			}
			seriesList.add(series);
		}
		for (XYChart.Series series : seriesList) {
			sc.getData().add(series);
		}
		return sc;
	}

	private int getIndex(LocalDateTime date, String[] days) {
		for (int i = 0; i < days.length; i++) {
			if (date.toString().equals(days[i])) {
				return i;
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}
}