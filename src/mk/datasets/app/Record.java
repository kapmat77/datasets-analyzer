package mk.datasets.app;

import sun.util.resources.LocaleData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Record {

	private int id;
	private int timeId;
//	private List<String> parameters = new ArrayList<>();
	private Map<String, String> parameters = new HashMap<>();
	private LocalDate localDate;

	public Record() {
		this.id = 0;
		this.timeId = 0;
		this.localDate = LocalDate.of(1,1,1);
	}

	public Record(int id) {
		this.id = id;
		this.timeId = 0;
		this.localDate = LocalDate.of(1,1,1);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeId() {
		return timeId;
	}

	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	//	public List<String> getParameters() {
//		return parameters;
//	}
//
//	public void setParameters(List<String> parameters) {
//		this.parameters = parameters;
//	}

	public void addParameter(String attribute, String parameter) {
		this.parameters.put(attribute, parameter);
	}
}
