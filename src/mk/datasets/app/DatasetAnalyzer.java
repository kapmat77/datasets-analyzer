package mk.datasets.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class DatasetAnalyzer {

	private static List<Dataset> datasets = new ArrayList<>();

	public DatasetAnalyzer() {

	}

	public void testAnalyzer() {
		testAddDatasets();
		testAddPrimitives();
		testAddEvents();
	}

	private void testAddDatasets() {
		//Add datasets
		String path1 = "src/resources/eurofxref-hist.csv";
		String path2 = "src/resources/various-bitcoin-currency-statist.csv";
		Dataset datasetEuro = new Dataset(1, "Euro", path1);
		Dataset datasetBitcoin = new Dataset(2, "Bitcoin", path2);

		datasets.add(datasetEuro);
		datasets.add(datasetBitcoin);
	}

	private void testAddPrimitives() {
		//Add primitives
		List<String> inputStringPrimitiveList = new ArrayList<>();
		inputStringPrimitiveList.add("P1:1.USD>1.13");
		inputStringPrimitiveList.add("P2:1.JPY>124");
		inputStringPrimitiveList.add("P3:2.value>=412");

		//Covnert primitives
		List<Primitive> primitiveList = new ArrayList<>();
		for (int i = 0; i < inputStringPrimitiveList.size(); i++) {
			Primitive primitive = Primitive.convertStringToPrimitive(inputStringPrimitiveList.get(i));
			primitive.findRecords(getDatasetById(primitive.getDatasetId()));
			primitive.toString();
			primitiveList.add(primitive);
		}

		//Find duplicates
		if (Primitive.duplicatesExist(primitiveList)) {
			System.out.println("ERROR - duplicates detected");
		}
	}

	private void testAddEvents() {
		//Add events
		List<String> inputStringEventList = new ArrayList<>();
		inputStringEventList.add("E1:(P1 || P2) && P3");
		inputStringEventList.add("E2:P1 && P3");
		inputStringEventList.add("E3:P1 || P2");

		//Covnert events
		List<Event> eventList = new ArrayList<>();
		for (int i = 0; i < inputStringEventList.size(); i++) {
			Event event = Event.convertStringToEvent(inputStringEventList.get(i));
//            event.findRecords();
			System.out.println(event.toString());
			eventList.add(event);
		}
		//Find duplicates
		if (Event.duplicatesExist(eventList)) {
			System.out.println("ERROR - duplicates detected");
		}

		String inputEvent = "E1:(P1 || P2) && P3";
		Event event = Event.convertStringToEvent(inputEvent);
		System.out.println(event.toString());

//        System.out.println(Event.getOpearionList("P5 && (P1 || (!P2 && P3) && (P7 || P10)) && P4 || (P11 && P12)"));
//        System.out.println(Event.getOperionList("(P1 || !P2) && P3"));
	}

	private Dataset getDatasetById(int id) {
		for (Dataset dataset : datasets) {
			if (dataset.getId() == id) {
				return dataset;
			}
		}
		return null;
	}
}
