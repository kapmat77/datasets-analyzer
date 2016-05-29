package mk.datasets.app;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Dataset {

	private int id;
	private String name;
	private List<String> attributes = new ArrayList<>();
	private List<Record> records = new ArrayList<>();

	public Dataset() {
		this.id = 0;
	}

	public Dataset(int id, String name, String path) {
		this.id = id;
		this.name = name;
		readData(path);
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

	private void readData(String path) {
		FileOperator fileOperator = new FileOperator();
		fileOperator.readDataFromFile(path, this);
	}

	public LocalDate getOldestDate() {
		return Finder.findMinInArray(records).getLocalDate();
	}

	public LocalDate getNewestDate() {
		return Finder.findMaxInArray(records).getLocalDate();
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


}
