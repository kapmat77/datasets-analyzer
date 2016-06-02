package mk.datasets.app;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class DatasetAnalyzer {

	private static List<Dataset> datasets = new ArrayList<>();
	private static List<Primitive> primitives = new ArrayList<>();
	private static List<Event> events = new ArrayList<>();
	private static LocalDateTime oldestDate;
	private static LocalDateTime newestDate;

	private static int counter = 0;

	public DatasetAnalyzer() {}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public void resetLists() {
		primitives.clear();
		events.clear();
	}

	public String activePattern(Pattern.Name patternName, String inputE, LocalDateTime startDate, LocalDateTime endDate) {
		String[] inputEvents = inputE.replace(" ","").split(",");

		Event event, secondEvent;
		Pattern pattern = new Pattern();
		pattern.setStartDate(startDate);
		pattern.setEndDate(endDate);

		String outputMsg;
		outputMsg = "\nPoszukiwanie wzorca " + patternName.name() +" dla eventów: ";
		for (int i = 0; i<inputEvents.length; i++) {
			outputMsg = outputMsg + inputEvents[i];
			if (i==(inputEvents.length-1)) {
				outputMsg = outputMsg + ".";
			} else {
				outputMsg = outputMsg + ", ";
			}
		}
		for (int i = 0; i<inputEvents.length; i++) {
			if (inputEvents[i].contains("->")) {
				String[] dubleEvent = inputEvents[i].split("->");
				event = getEventByName(dubleEvent[0]);
				secondEvent = getEventByName(dubleEvent[1]);
			} else {
				event = getEventByName(inputEvents[i]);
				secondEvent = null;
			}
			switch (patternName) {
				case ABSENCE:
					outputMsg = outputMsg + "\n\t" + pattern.absence(event);
					break;
				case INVARIANCE:
					outputMsg = outputMsg + "\n\t" + pattern.invariance(event);
					break;
				case EXISTENCE:
					outputMsg = outputMsg + "\n\t" + pattern.existence(event);
					break;
				case RESPONSE:
					outputMsg = outputMsg + "\n\t" + pattern.response(event, secondEvent);
					break;
				case OBLIGATION:
					outputMsg = outputMsg + "\n\t" + pattern.obligation(event, secondEvent);
					break;
				case RESPONSIVELY:
					outputMsg = outputMsg + "\n\t" + pattern.responsively(event);
					break;
				case PERSISTENCE:
					outputMsg = outputMsg + "\n\t" + pattern.persistence(event);
					break;
				case REACTIVITY:
					outputMsg = outputMsg + "\n\t" + pattern.reactivity(event, secondEvent);
					break;
				default:
					return "ERROR - zły wzorzec";
			}
		}
		return outputMsg;
	}

	public String addDataset(File file) {
		int lastBackslash = file.getAbsolutePath().lastIndexOf('\\');
		String datasetName = file.getAbsolutePath().substring(lastBackslash+1);
		if (getDatasetByName(datasetName)==null) {
			counter++;
			Dataset dataset = new Dataset(counter, datasetName, file.getAbsolutePath());
			datasets.add(dataset);
			setOldestAndNewestDate();
		} else {
			return "Zbiór danych został załadowany już wcześniej!";
		}

		//Sort datasets - first contains the oldest data
		Collections.sort(datasets, (dataset1, dataset2) -> dataset1.getOldestDate().compareTo(dataset2.getOldestDate()));
		assignTimeIdToRecords(0);

		//Convert data if datasets have different date format
		convertDatasetsToOneDateFormat();

		return "Zbiór danych został wczytany poprawnie.";
	}

	private void convertDatasetsToOneDateFormat() {
		FileOperator.DateSmallestPart smallestPart = smallestDateFormat();

		//INTERPOLACJA
		for (Dataset dataset: datasets) {
			if (!dataset.getMeasurement().equalsIgnoreCase(smallestPart.name())) {
				int multiplier = getMultiplier(dataset.getMeasurement(), smallestPart);
				List<Record> oldRecords = dataset.getRecords();
				List<Record> newRecords = new ArrayList<>();

				for (int j = 0; j<oldRecords.size(); j++) {
					for (int i = 0; i<multiplier; i++) {
//						Map<String, String> newParam = new HashMap<>();
						Record newRecord = new Record(j * multiplier + i + 1);
						if (i !=0) {
							for (Map.Entry<String, String> entry: oldRecords.get(j).getParameters().entrySet()) {
								if (!(entry.getKey().equalsIgnoreCase("data") || entry.getKey().equalsIgnoreCase("time"))) {
									if ((j+1)<oldRecords.size()) {
										long secondTimeDiff = differenceLocalDateTime(oldRecords.get(j).getLocalDateTime(),oldRecords.get(j+1).getLocalDateTime());
										double valueDiff = 0;
										boolean appropriateData = true;
										try {
											valueDiff = Double.valueOf(Double.valueOf(oldRecords.get(j+1).getParameters().get(entry.getKey())) - Double.valueOf(entry.getValue()));
										} catch (Exception e) {
											//TODO w przypadku braku danych !!
											valueDiff = 0;
											appropriateData = false;
										}
										double partValue = valueDiff/multiplier;

										if (appropriateData) {
											newRecord.addParameter(entry.getKey(), String.valueOf(Double.valueOf(entry.getValue())+partValue*i));
										} else {
											newRecord.addParameter(entry.getKey(), "N/A");
										}

										//TODO poprawić, dział tylko dla przejścia z DNI na mniejsze jednostki
										switch (smallestPart) {
											case SECOND:
												if (newRecord.getLocalDateTime().isEqual(LocalDateTime.of(1,1,1,0,0))) {
													newRecord.setLocalDateTime(oldRecords.get(j).getLocalDateTime().plusSeconds(i*(secondTimeDiff/(24*3600))));
													newRecords.add(newRecord);
												}
												break;
											case MINUTE:
												if (newRecord.getLocalDateTime().isEqual(LocalDateTime.of(1,1,1,0,0))) {
													newRecord.setLocalDateTime(oldRecords.get(j).getLocalDateTime().plusSeconds(i * (secondTimeDiff / (24 * 60))));
													newRecords.add(newRecord);
												}
												break;
											case HOUR:
												if (newRecord.getLocalDateTime().isEqual(LocalDateTime.of(1,1,1,0,0))) {
													newRecord.setLocalDateTime(oldRecords.get(j).getLocalDateTime().plusSeconds(i*(secondTimeDiff/24)));
													newRecords.add(newRecord);
												}
												break;
										}
									}
								}
							}
						} else {
							newRecords.add(oldRecords.get(j));
						}
					}
				}
				dataset.setRecords(newRecords);
				dataset.setMeasurement(smallestDateFormat().name());
			}
		}

	}

	private long convertToSeconds(LocalDateTime timeDiff) {
		long hours = timeDiff.getHour();
		long minutes = timeDiff.getMinute();
		long seconds = timeDiff.getSecond();

		if (hours == 0 && minutes == 0 && seconds == 0) {

		}

		return (hours*3600 + minutes*60 + seconds);
	}

	private long differenceLocalDateTime(LocalDateTime firstDate, LocalDateTime secondDate) {
		LocalDateTime tempDateTime = LocalDateTime.from(firstDate);

		long years = tempDateTime.until(secondDate, ChronoUnit.YEARS);
		tempDateTime = tempDateTime.plusYears(years);

		long months = tempDateTime.until(secondDate, ChronoUnit.MONTHS);
		tempDateTime = tempDateTime.plusMonths(months);

		long days = tempDateTime.until(secondDate, ChronoUnit.DAYS);
		tempDateTime = tempDateTime.plusDays(days);

		long hours = tempDateTime.until(secondDate, ChronoUnit.HOURS);
		tempDateTime = tempDateTime.plusHours(hours);

		long minutes = tempDateTime.until(secondDate, ChronoUnit.MINUTES);
		tempDateTime = tempDateTime.plusMinutes(minutes);

		long seconds = tempDateTime.until(secondDate, ChronoUnit.SECONDS);

		return (days*3600*24 + hours*3600 + minutes*60 + seconds);
	}

	private FileOperator.DateSmallestPart smallestDateFormat() {
		boolean start = true;
		FileOperator.DateSmallestPart dateSmallestPart = FileOperator.DateSmallestPart.DAY;
		FileOperator.DateSmallestPart dateFormat;
		for (Dataset dataset: datasets) {
			switch (dataset.getMeasurement()) {
				case "SECOND":
					dateFormat = FileOperator.DateSmallestPart.SECOND;
					break;
				case "MINUTE":
					dateFormat = FileOperator.DateSmallestPart.MINUTE;
					break;
				case "HOUR":
					dateFormat = FileOperator.DateSmallestPart.HOUR;
					break;
				case "DAY":
					dateFormat = FileOperator.DateSmallestPart.DAY;
					break;
				default:
					dateFormat = FileOperator.DateSmallestPart.DAY;
					break;
			}
			if (start) {
				dateSmallestPart = dateFormat;
				start = false;
			} else if (dateSmallestPart.isBiggerThan(dateFormat)) {
				dateSmallestPart = dateFormat;
			}
		}
		return dateSmallestPart;
	}

	private int getMultiplier(String currentUnit, FileOperator.DateSmallestPart smallUnit) {
		int multiplier = 0;
		switch (smallUnit) {
			case SECOND:
				switch (currentUnit) {
					case "MINUTE":
						multiplier = 60;
						break;
					case "HOUR":
						multiplier = 3600;
						break;
					case "DAY":
						multiplier = 86400;
						break;
				}
				break;
			case MINUTE:
				switch (currentUnit) {
					case "HOUR":
						multiplier = 60;
						break;
					case "DAY":
						multiplier = 1440;
						break;
				}
				break;
			case HOUR:
				switch (currentUnit) {
					case "DAY":
						multiplier = 24;
						break;
				}
				break;
			case DAY:
				break;
			default:
				break;
		}
		return multiplier;
	}

	public String addPrimitives(String inputPrimitives) {
		Primitive.resetCounter();
		try {
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
		} catch (Exception e) {
			return "ERROR - prymitywy zostały nieprawidłowo zdefiniowane!";
		}
	}

	public String addEvents(String inputEvents) {
		Event.resetCounter();
		try {
			if (!inputEvents.isEmpty()) {
				String[] eventsTable = inputEvents.split("\\n");

				//Covnert events
				List<Event> eventList = new ArrayList<>();
				for (int i = 0; i < eventsTable.length; i++) {
					Event event = Event.convertStringToEvent(eventsTable[i]);
					event.findDates(primitives);
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
		} catch (Exception e) {
			return "ERROR - eventy zostały nieprawidłowo zdefiniowane!";
		}
	}

	private void assignTimeIdToRecords(int daysDisplacement) {
		int timeId = 0;
		//TODO zmienić daty występowania w przypadku przesunięcia !!!!!
		Dataset firstDataset = datasets.get(0);
		for (Dataset secondDataset: datasets) {
			if (!firstDataset.equals(secondDataset)) {
				for (Record firstRecord: firstDataset.getRecords()) {
					for (Record secondRecord: secondDataset.getRecords()) {
						if (firstRecord.getLocalDateTime().equals(secondRecord.getLocalDateTime()) &&
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

	private void setOldestAndNewestDate() {
		boolean start = true;
		for (Dataset dataset: datasets) {
			if (start) {
				oldestDate = dataset.getOldestDate();
				newestDate = dataset.getNewestDate();
				start = false;
			} else {
				if (dataset.getNewestDate().isAfter(newestDate)) {
					newestDate = dataset.getNewestDate();
				}
				if (dataset.getOldestDate().isBefore(oldestDate)) {
					oldestDate = dataset.getOldestDate();
				}
			}
		}
	}

	public static LocalDateTime getOldestDate() {
		return oldestDate;
	}

	public static void setOldestDate(LocalDateTime oldestDate) {
		DatasetAnalyzer.oldestDate = oldestDate;
	}

	public static LocalDateTime getNewestDate() {
		return newestDate;
	}

	public static void setNewestDate(LocalDateTime newestDate) {
		DatasetAnalyzer.newestDate = newestDate;
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

	public Event getEventByName(String name) {
		for (Event event : events) {
			if (event.getName().equals(name)) {
				return event;
			}
		}
		return null;
	}

	public String showPrimitives() {
		String output = "\n";
		for (Primitive primitive: primitives) {
			output = output + primitive.toString();
			output = output + primitive.showDates();
		}
		return output;
	}

	public String showEvents() {
		String output = "\n";
		for (Event event: events) {
			output = output + event.toString();
			output = output + event.showDates();
		}
		return output;
	}
}
