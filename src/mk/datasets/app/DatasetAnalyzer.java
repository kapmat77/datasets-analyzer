package mk.datasets.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class DatasetAnalyzer {

	private static List<Dataset> datasets = new ArrayList<>();
	private static List<Primitive> primitives = new ArrayList<>();
	private static List<Event> events = new ArrayList<>();

	public DatasetAnalyzer() {}

	public void testAnalyzer() {
		testAddDatasets();
		testAddPrimitives();
		testAddEvents();

		testPattern();

	}

	private void testPattern() {
		Event event = events.get(0);
		Event secondEvent = events.get(1);
		Pattern.Name patternName = Pattern.Name.ABSENCE;
		Pattern pattern = new Pattern();
		pattern.setStartDate(LocalDate.of(1970,1,1));
		pattern.setEndDate(LocalDate.of(2016,12,31));
		switch (patternName) {
			case ABSENCE:
				pattern.absence(event);
				break;
			case INVARIANCE:
				pattern.invariance(event);
				break;
			case EXISTANCE:
				pattern.existance(event);
				break;
			case RESPONSE:
				pattern.response(event, event);
				break;
		}
	}

	private void testAddDatasets() {
		//Add datasets
		String path1 = "src/resources/eurofxref-hist.csv";
		String path2 = "src/resources/various-bitcoin-currency-statist.csv";
		Dataset datasetEuro = new Dataset(1, "Euro", path1);
		Dataset datasetBitcoin = new Dataset(2, "Bitcoin", path2);

		datasets.add(datasetEuro);
		datasets.add(datasetBitcoin);

		//Sort datasets - first contains the oldest data
		Collections.sort(datasets, (dataset1, dataset2) -> dataset1.getOldestDate().compareTo(dataset2.getOldestDate()));

		assignTimeIdToRecords(0);
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

		primitives.addAll(primitiveList);
	}

	private void testAddEvents() {
		//Add events
		List<String> inputStringEventList = new ArrayList<>();
		inputStringEventList.add("E1:P1 && P2");
		inputStringEventList.add("E2:P1 || P2");
//		inputStringEventList.add("E3:P1 || !P2");
//		inputStringEventList.add("E4:(P1 || !P2) && P3");

		//Covnert events
		List<Event> eventList = new ArrayList<>();
		for (int i = 0; i < inputStringEventList.size(); i++) {
			Event event = Event.convertStringToEvent(inputStringEventList.get(i));
			event.findDates(datasets, primitives);
			eventList.add(event);
		}
		//Find duplicates
		if (Event.duplicatesExist(eventList)) {
			System.out.println("ERROR - duplicates detected");
		}

		events.addAll(eventList);

//        System.out.println(Event.getOpearionList("P5 && (P1 || (!P2 && P3) && (P7 || P10)) && P4 || (P11 && P12)"));
//        System.out.println(Event.getOperionList("(P1 || !P2) && P3"));
	}

	private void assignTimeIdToRecords(int daysDisplacement) {
		int timeId = 0;
		//TODO zmienić daty występowania w przypadku przesunięcia !!!!!
		Dataset firstDataset = datasets.get(0);
		for (Dataset secondDataset: datasets) {
			if (!firstDataset.equals(secondDataset)) {
				for (Record firstRecord: firstDataset.getRecords()) {
					for (Record secondRecord: secondDataset.getRecords()) {
						if (firstRecord.getLocalDate().equals(secondRecord.getLocalDate()) &&
								firstRecord.getTimeId()==0 && secondRecord.getTimeId()==0) {
							timeId++;
							firstRecord.setTimeId(timeId);
							secondRecord.setTimeId(timeId);
						}
					}
				}
			}
		}
	}

	public static Dataset getDatasetById(int id) {
		for (Dataset dataset : datasets) {
			if (dataset.getId() == id) {
				return dataset;
			}
		}
		return null;
	}
}
