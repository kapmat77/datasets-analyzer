package mk.datasets.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */

public class FileOperator {

	private List<Record> records = new ArrayList<>();
	private List<String> attributes = new ArrayList<>();
	private Dataset dataset;

	public void readDataFromFile(String file, Dataset newDataset) {
		dataset = newDataset;
		try {
			if (file.endsWith(".csv")) {
				readFromCsv(file);
			} else if (file.endsWith(".txt")) {
				readFromTxt(file);
			} else if (file.endsWith(".json")) {
				readFromJson(file);
			} else if (file.endsWith(".xls")) {
				readFromXls(file);
			}
		} catch (IOException e) {
			System.out.println("Plik nie zostal wczytany poprawnie!");
			e.printStackTrace();
			System.exit(-1);
		}
		if (attributes.size()>0 && records.size()>0) {
			dataset.setAttributes(attributes);
			dataset.setRecords(records);
		} else {
			System.out.println("Plik nie zostal wczytany poprawnie!");
			System.exit(-1);
		}
	}

	private void readFromCsv(String file) throws IOException {
		Path path = Paths.get(file);
		Scanner in = new Scanner(path);
		String line;
		String[] parts;
		line = in.nextLine();
		parts = line.replace("\"","").split(",");
		attributes = new ArrayList<>();
		for (String part : parts) {
			attributes.add(part);
		}
		int index = 0;
		while (in.hasNextLine()) {
			index++;
			Record record = new Record(index);
			line = in.nextLine();
			if (!line.isEmpty()) {
				parts = line.split(",");
				for (int i = 0; i<parts.length; i++) {
					if (attributes.get(i).equalsIgnoreCase("date")) {
						String[] dateParts = parts[i].replace("\"","").split("-");
						record.setLocalDateTime(LocalDate.of(Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2])), LocalTime.of(0,0));
					} else {
						record.addParameter(attributes.get(i), parts[i]);
					}
				}
				records.add(record);
			} else {
				break;
			}
		}
		//Sort record - from oldest data to newest data
		Collections.sort(records, (record1, record2) -> record1.getLocalDateTime().compareTo(record2.getLocalDateTime()));
	}

	private void readFromTxt(String file) {
		String line;
		String[] parts;

	}

	private void readFromXls(String file) {
		String line;
		String[] parts;

	}

	private void readFromJson(String file) {
		String line;
		String[] parts;

	}
}