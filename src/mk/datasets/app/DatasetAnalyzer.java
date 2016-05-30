package mk.datasets.app;

import java.io.File;
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

	private static int counter = 0;

	public DatasetAnalyzer() {}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public void testAnalyzer() {
//		testAddDatasets();
//		testAddPrimitives();
//		testAddEvents();

//		testPattern();

	}

	public void testPattern() {
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
			case EXISTENCE:
				pattern.existence(event);
				break;
			case RESPONSE:
				pattern.response(event, secondEvent);
				break;
			case OBLIGATION:
				pattern.obligation(event, secondEvent);
				break;
			case RESPONSIVELY:
				pattern.responsively(event);
				break;
			case PERSISTENCE:
				pattern.responsively(event);
				break;
			case REACTIVITY:
				pattern.reactivity(event, secondEvent);
		}
	}

	public String addDataset(File file) {
		int lastBackslash = file.getAbsolutePath().lastIndexOf('\\');
		String datasetName = file.getAbsolutePath().substring(lastBackslash+1);
		if (getDatasetByName(datasetName)==null) {
			counter++;
			Dataset dataset = new Dataset(counter, datasetName, file.getAbsolutePath());
			datasets.add(dataset);
		} else {
			return "Zbiór danych został załadowany już wcześniej!";
		}

		//Sort datasets - first contains the oldest data
		Collections.sort(datasets, (dataset1, dataset2) -> dataset1.getOldestDate().compareTo(dataset2.getOldestDate()));

		assignTimeIdToRecords(0);
		return "Zbiór danych został wczytany poprawnie.";
	}

	public String addPrimitives(String inputPrimitives) {
		if (!inputPrimitives.isEmpty()) {
			String[] primitivesTable = inputPrimitives.split("\\n");

			//Covnert primitives
			List<Primitive> primitiveList = new ArrayList<>();
			for (int i = 0; i < primitivesTable.length; i++) {
				Primitive primitive = Primitive.convertStringToPrimitive(primitivesTable[i]);
				primitive.findRecords(getDatasetById(primitive.getDatasetId()));
				primitive.toString();
				primitiveList.add(primitive);
			}

			//Find duplicates
			if (Primitive.duplicatesExist(primitiveList)) {
				return "ERROR - wykryto duplikaty(Prymitywy). Usuń je i spróbuj ponownie.";
			}
			primitives.addAll(primitiveList);
			return "Prymitywy zostały wczytane poprawnie.";
		}
		return "ERROR - zdefiniuj prymitywy!";
	}

	public String addEvents(String inputEvents) {
		if (!inputEvents.isEmpty()) {
			String[] eventsTable = inputEvents.split("\\n");

			//Covnert events
			List<Event> eventList = new ArrayList<>();
			for (int i = 0; i < eventsTable.length; i++) {
				Event event = Event.convertStringToEvent(eventsTable[i]);
				event.findDates(datasets, primitives);
				eventList.add(event);
			}

			//Find duplicates
			if (Event.duplicatesExist(eventList)) {
				return "ERROR - wykryto duplikaty(Eventy). Usuń je i spróbuj ponownie.";
			}
			events.addAll(eventList);
			return "Eventy zostały wczytane poprawnie.";
		}
		return "ERROR - zdefiniuj eventy!";
	}

	public void testAddDatasets() {
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

	public void testAddPrimitives() {
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

	public void testAddEvents() {
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

	public Dataset getDatasetByName(String name) {
		for (Dataset dataset : datasets) {
			if (dataset.getName().equals(name)) {
				return dataset;
			}
		}
		return null;
	}
}
