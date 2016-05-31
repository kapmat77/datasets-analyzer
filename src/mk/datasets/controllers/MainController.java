package mk.datasets.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mk.datasets.app.Dataset;
import mk.datasets.app.DatasetAnalyzer;
import mk.datasets.app.Finder;
import mk.datasets.app.Pattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Kapmat on 2016-05-28.
 */

public class MainController implements Initializable {

	//#################### MENU ####################
	@FXML
	private MenuItem miLoadDataset;
	@FXML
	private MenuItem miExit;
	@FXML
	private MenuItem miAbout;

	//#################### TEXT_AREA ####################
	@FXML
	private TextArea taOutput;
	@FXML
	private TextArea taInputPrimitives;
	@FXML
	private TextArea taInputEvents;

	//#################### CHECK_BOX ####################
	@FXML
	private CheckBox chAbsence;
	@FXML
	private CheckBox chInvariance;
	@FXML
	private CheckBox chExistence;
	@FXML
	private CheckBox chResponse;
	@FXML
	private CheckBox chObligation;
	@FXML
	private CheckBox chResponsively;
	@FXML
	private CheckBox chPersistence;
	@FXML
	private CheckBox chReactivity;

	//#################### TEXT_FIELD ####################
	@FXML
	private TextField tfAbsence;
	@FXML
	private TextField tfInvariance;
	@FXML
	private TextField tfExistence;
	@FXML
	private TextField tfResponse;
	@FXML
	private TextField tfObligation;
	@FXML
	private TextField tfResponsively;
	@FXML
	private TextField tfPersistence;
	@FXML
	private TextField tfReactivity;

	//#################### BUTTON ####################
	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonMove;
	@FXML
	private Button buttonPrimitives;
	@FXML
	private Button buttonEvents;

	//#################### DATA_PICKER ####################
	@FXML
	private DatePicker dpStartDate;
	@FXML
	private DatePicker dpEndDate;

	//#################### STAGES ####################
	private Stage loadStage = new Stage();
	private Stage aboutStage = new Stage();

	//#################### MODEL ####################
	DatasetAnalyzer datasetAnalyzer = new DatasetAnalyzer();

	LocalTime startTime = LocalTime.of(0, 0, 0);
	LocalTime endTime = LocalTime.of(0, 0, 0);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeAboutWindow();
		initializeComponents();
		initializeTooltips();

		//Default text
		taInputPrimitives.appendText("P1: 1.USD>1.13\nP2: 1.JPY>124\nP3: 2.value>=412\nP4: 2.value<0");
		taInputEvents.appendText("E1: ((P1 && P2) || P3) && P4");
//		taInputEvents.appendText("E1: (P1 && P2) || P3\nE2: P1 || P2\nE3: P4");
	}

	@FXML
	public void startAction(ActionEvent event) {
		datasetAnalyzer.resetLists();
		if (datasetAnalyzer.getDatasets().isEmpty()) {
			taOutput.appendText("\n\n" + "Wczytaj zbiór danych!");
			return;
		} else if (dateIsSetCorrectly()) {
			taOutput.appendText("\n\n#################### START ####################");
			taOutput.appendText("\n" + "Wyszukiwanie wzorców dla okresu od " + dpStartDate.getValue() + " " + startTime.toString() + " do " + dpEndDate.getValue() + " " + endTime.toString());
			String primitivesComment = datasetAnalyzer.addPrimitives(taInputPrimitives.getText());
			taOutput.appendText("\n" + primitivesComment);
			if (!primitivesComment.contains("ERROR")) {
				String eventsComment = datasetAnalyzer.addEvents(taInputEvents.getText());
				taOutput.appendText("\n" + eventsComment);
				if (!eventsComment.contains("ERROR")) {
					checkAndActivePatterns();
				} else {
					taOutput.appendText("\n\n" + "ERROR");
					taOutput.appendText("\n\n##################### END #####################");
					return;
				}
			} else {
				taOutput.appendText("\n\n" + "ERROR");
				taOutput.appendText("\n\n##################### END #####################");
				return;
			}
		} else {
			taOutput.appendText("\n\n" + "ERROR - popraw daty!");
			taOutput.appendText("\n\n##################### END #####################");
			return;
		}
		taOutput.appendText("\n\n##################### END #####################");
	}

	private void checkAndActivePatterns() {
		if (!tfAbsence.isDisable() && !tfAbsence.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.ABSENCE, tfAbsence.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfAbsence.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chAbsence.getText() + "' jest puste");
		}
		if (!tfInvariance.isDisable() && !tfInvariance.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.INVARIANCE, tfInvariance.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfInvariance.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chInvariance.getText() + "' jest puste");
		}
		if (!tfExistence.isDisable() && !tfExistence.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.EXISTENCE, tfExistence.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfExistence.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chExistence.getText() + "' jest puste");
		}
		if (!tfResponse.isDisable() && !tfResponse.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.RESPONSE, tfResponse.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfResponse.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chResponse.getText() + "' jest puste");
		}
		if (!tfObligation.isDisable() && !tfObligation.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.OBLIGATION, tfObligation.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfObligation.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chObligation.getText() + "' jest puste");
		}
		if (!tfResponsively.isDisable() && !tfResponsively.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.RESPONSIVELY, tfResponsively.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfResponsively.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chResponsively.getText() + "' jest puste");
		}
		if (!tfPersistence.isDisable() && !tfPersistence.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.PERSISTENCE, tfPersistence.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfResponsively.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chResponsively.getText() + "' jest puste");
		}
		if (!tfReactivity.isDisable() && !tfReactivity.getText().isEmpty()) {
			taOutput.appendText("\n" + datasetAnalyzer.activePattern(Pattern.Name.REACTIVITY, tfReactivity.getText(), LocalDateTime.of(dpStartDate.getValue(), startTime), LocalDateTime.of(dpEndDate.getValue(), endTime)));
		} else if (!tfReactivity.isDisable()) {
			taOutput.appendText("\nERROR - pole '" + chReactivity.getText() + "' jest puste");
		}
	}

	private boolean dateIsSetCorrectly() {
		if (dpStartDate.getValue() == null || dpEndDate.getValue() == null) {
			return false;
		}
		return dpStartDate.getValue().isBefore(dpEndDate.getValue()) || dpStartDate.getValue().isEqual(dpEndDate.getValue());
	}

	private void initializeTooltips() {
		chAbsence.setTooltip(new Tooltip("Brak wystąpienia zdarzenia"));
		chInvariance.setTooltip(new Tooltip("Ciągłe występowanie zdarzenia"));
		chExistence.setTooltip(new Tooltip("Możliwość wystąpienia zdarzenia"));
		chResponse.setTooltip(new Tooltip("Możliwość wystąpienia pewnego zdarzenia po którym musi wystąpić inne zdarzenie"));
		chObligation.setTooltip(new Tooltip("Możliwość wystąpienia dwóch zdarzeń w ten sposób, że jeżeli dane zdarzenie " +
				"wystąpi, to wystąpi także drugie zdarzenie"));
		chResponsively.setTooltip(new Tooltip("Możliwość wystąpienia pewnego zdarzenia w ten sposób, że nawet jeśli teraz" +
				" nie wystepuje, to i tak wystąpi w przyszłości"));
		chPersistence.setTooltip(new Tooltip("Możliwość wystąpienia pewnego zdarzenia od pewnego momentu już w sposób " +
				"niezmienniczy"));
		chReactivity.setTooltip(new Tooltip("Możliwość wystąpienia dwóch zdarzeń w ten sposób, że jeśli jedno wystąpi " +
				"od pewnego momentu trwale, to drugie zdarzenie od pewnego być może innego momentu też wystapi trwale"));
	}

	private void initializeComponents() {
		disableAllPatternTextFields();
		buttonStart.setDisable(true);

		taOutput.setWrapText(true);
		taInputPrimitives.setWrapText(true);
		taInputPrimitives.setStyle("" + "-fx-font-size: 13px;");
		taInputEvents.setWrapText(true);
		taInputEvents.setStyle("" + "-fx-font-size: 13px;");

		//Initialize datePicker
		String pattern = "yyyy-MM-dd";
		dpStartDate.setPromptText(pattern.toLowerCase());
		dpEndDate.setPromptText(pattern.toLowerCase());

		dpStartDate.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		dpEndDate.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		//Set default values
		dpStartDate.setValue(LocalDate.of(1, 1, 1));
		dpEndDate.setValue(LocalDate.now());
	}

	private void disableAllPatternTextFields() {
		tfAbsence.setDisable(true);
		tfInvariance.setDisable(true);
		tfExistence.setDisable(true);
		tfResponse.setDisable(true);
		tfObligation.setDisable(true);
		tfResponsively.setDisable(true);
		tfPersistence.setDisable(true);
		tfReactivity.setDisable(true);
	}

	@FXML
	public void loadDatasetAction(ActionEvent event) throws FileNotFoundException {
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Load data");
		filechooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Dataset file", "*.txt", "*.xls", "*.csv", "*.xml", "*.json"));
		File file = filechooser.showOpenDialog(loadStage);

		String output = datasetAnalyzer.addDataset(file);
		taOutput.appendText("\n\n" + output);

		//Get dataset
		int lastBackslash = file.getAbsolutePath().lastIndexOf('\\');
		String datasetName = file.getAbsolutePath().substring(lastBackslash + 1);
		Dataset dataset = datasetAnalyzer.getDatasetByName(datasetName);

		//Show basic information about loaded dataset
		taOutput.appendText(dataset.toStringWithAttrubites());
	}

	private void initializeAboutWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mk/datasets/fxml/AboutWindow.fxml"));
		Region root = null;
		try {
			root = (Region) loader.load();
		} catch (IOException e) {
			System.out.println("ERROR while loading AboutWindow.fxml. " + e.getMessage());
		}
		Scene mScene = new Scene(root, 300, 175);
		aboutStage.setScene(mScene);
		aboutStage.setTitle("About window");
		aboutStage.setResizable(false);
	}

	@FXML
	public void checkBoxAction(ActionEvent event) {
		if (event.getTarget().equals(chAbsence)) {
			if (tfAbsence.isDisable()) {
				tfAbsence.setDisable(false);
			} else {
				tfAbsence.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chInvariance)) {
			if (tfInvariance.isDisable()) {
				tfInvariance.setDisable(false);
			} else {
				tfInvariance.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chExistence)) {
			if (tfExistence.isDisable()) {
				tfExistence.setDisable(false);
			} else {
				tfExistence.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chResponse)) {
			if (tfResponse.isDisable()) {
				tfResponse.setDisable(false);
			} else {
				tfResponse.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chObligation)) {
			if (tfObligation.isDisable()) {
				tfObligation.setDisable(false);
			} else {
				tfObligation.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chResponsively)) {
			if (tfResponsively.isDisable()) {
				tfResponsively.setDisable(false);
			} else {
				tfResponsively.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chPersistence)) {
			if (tfPersistence.isDisable()) {
				tfPersistence.setDisable(false);
			} else {
				tfPersistence.setDisable(true);
			}
			startButtonStatus();
		} else if (event.getTarget().equals(chReactivity)) {
			if (tfReactivity.isDisable()) {
				tfReactivity.setDisable(false);
			} else {
				tfReactivity.setDisable(true);
			}
			startButtonStatus();
		}
	}

	private boolean allPatternTextFieldAreDisable() {
		return tfAbsence.isDisable() && tfInvariance.isDisable() && tfExistence.isDisable() && tfResponse.isDisable() &&
				tfObligation.isDisable() && tfResponsively.isDisable() && tfPersistence.isDisable() && tfReactivity.isDisable();
	}

	private void startButtonStatus() {
		if (allPatternTextFieldAreDisable()) {
			buttonStart.setDisable(true);
		} else {
			buttonStart.setDisable(false);
		}
	}

	@FXML
	public void aboutAction(ActionEvent event) {
		aboutStage.show();
	}

	@FXML
	public void exitAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	public void moveAction(ActionEvent event) {
	}

	@FXML
	public void showDatasets(ActionEvent event) {
		datasetAnalyzer.resetLists();
		taOutput.appendText("\n\n#################### DATASETS ####################");
		for (Dataset dataset : datasetAnalyzer.getDatasets()) {
			taOutput.appendText(dataset.toStringWithAttrubites() + "\n");
		}
		taOutput.appendText("##################### END #####################");
	}

	@FXML
	public void showPrimitives(ActionEvent event) {
		datasetAnalyzer.resetLists();
		taOutput.appendText("\n\n#################### PRYMITYWY ####################");
		String primitivesComment = datasetAnalyzer.addPrimitives(taInputPrimitives.getText());
		taOutput.appendText("\n" + primitivesComment);
		taOutput.appendText(datasetAnalyzer.showPrimitives());
		taOutput.appendText("##################### END #####################");
	}

	@FXML
	public void showEvents(ActionEvent event) {
		datasetAnalyzer.resetLists();
		taOutput.appendText("\n\n#################### EVENTY ####################");
		String primitivesComment = datasetAnalyzer.addPrimitives(taInputPrimitives.getText());
		taOutput.appendText("\n" + primitivesComment);
		if (!primitivesComment.contains("ERROR")) {
			String eventsComment = datasetAnalyzer.addEvents(taInputEvents.getText());
			taOutput.appendText("\n" + eventsComment);
		}
		taOutput.appendText(datasetAnalyzer.showEvents());
		taOutput.appendText("##################### END #####################");
	}

	@FXML
	public void infoAction(ActionEvent event) {

	}
}
