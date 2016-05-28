package mk.datasets.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Dataset {

	private int id;
	private List<String> attributes = new ArrayList<>();
	private List<Record> records = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
