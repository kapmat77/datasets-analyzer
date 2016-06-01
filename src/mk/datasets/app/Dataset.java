package mk.datasets.app;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Dataset {

	private int id;
	private String name;
	private String measurement;
	private List<String> attributes = new ArrayList<>();
	private List<Record> records = new ArrayList<>();

	public Dataset() {
		this.id = 0;
	}

	public Dataset(int id, String name, String path) {
		this.id = id;
		this.name = name;
		this.measurement = readData(path);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public void addAtrribute(String attribute) {
		this.attributes.add(attribute);
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public void addRecord(Record record) {
		this.records.add(record);
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	private String readData(String path) {
		FileOperator fileOperator = new FileOperator();
		fileOperator.readDataFromFile(path, this);
		return fileOperator.getDateFormat();
	}

	public LocalDateTime getOldestDate() {
		return Finder.findMinInArray(records).getLocalDateTime();
	}

	public LocalDateTime getNewestDate() {
		return Finder.findMaxInArray(records).getLocalDateTime();
	}

	public Record getRecordByTimeId(int timeId) {
		for (Record record: records) {
			if (record.getTimeId() == timeId) {
				return record;
			}
		}
		return null;
	}

	public void resetTimeId() {
		for (Record record: this.records) {
			record.setTimeId(0);
		}
	}

	public void printAttributes() {
		System.out.println("\n\"" + this.name + "\" attributes:");
		this.getAttributes().forEach(System.out::println);
	}

	public void printRecords() {
		System.out.println("\n\"" + this.name + "\" records:");
		for (Record record: this.getRecords()) {
			for (Map.Entry<String, String> entry: record.getParameters().entrySet()) {
				System.out.println(entry.getKey() + " - " + entry.getValue());
			}
		}
	}

	public String toString() {
		return "\nDATASET" +
				"\nId: " + this.id +
				"\nName: " + this.name +
				"\nFrequency: " + this.measurement +
				"\nAttributes: " + this.attributes.size() +
				"\nRecords: " +this.records.size();
	}

	public String toStringWithAttrubites() {
		String allAtributes = "";
		for (String attribute: attributes) {
			if (!attribute.equalsIgnoreCase("data")) {
				if (allAtributes.equals("")) {
					allAtributes = attribute;
				} else {
					allAtributes = allAtributes +  ", " + attribute;
				}
			}
		}
		return 	"\nId: " + this.id +
				"\nName: " + this.name +
				"\nDate: " + this.getOldestDate() + " - " + this.getNewestDate() +
				"\nMeasurement: " + this.measurement +
				"\nRecords: " +this.records.size() +
				"\nAttributes(" + this.attributes.size() +
				"): " + allAtributes;
	}
}
