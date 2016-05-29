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

		List<LocalDate> datesAnd = Finder.getRecordsDates(primitives.get(0), Event.Mark.AND, primitives.get(1));
		List<LocalDate> datesOr = Finder.getRecordsDates(primitives.get(0), Event.Mark.OR, primitives.get(1));

		List<LocalDate> datesSecondAnd = Finder.getRecordsDates(datesAnd, Event.Mark.AND, datesOr);
		List<LocalDate> datesSecondOr = Finder.getRecordsDates(datesAnd, Event.Mark.OR, datesOr);

		List<LocalDate> datesThirdAnd = Finder.getRecordsDates(datesAnd, Event.Mark.AND, primitives.get(1));
		List<LocalDate> datesThirdOr = Finder.getRecordsDates(datesAnd, Event.Mark.OR, primitives.get(1));
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
		inputStringEventList.add("E1:!P1 && !P3");
		inputStringEventList.add("E2:P1 || P2");
		inputStringEventList.add("E3:P1 || !P2");
		inputStringEventList.add("E4:(P1 || !P2) && P3");

		//Covnert events
		List<Event> eventList = new ArrayList<>();
		for (int i = 0; i < inputStringEventList.size(); i++) {
			Event event = Event.convertStringToEvent(inputStringEventList.get(i));
			event.findRecords(datasets, primitives);

//            event.findRecords();
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
		//TODO poprawić na coś szybszego :v
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
