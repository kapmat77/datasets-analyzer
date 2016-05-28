package mk.datasets.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Record {

	private int id;
	private int timeId;
	private List<String> parameters = new ArrayList<>();

	public Record() {
		this.id = 0;
	}

	public Record(int id, List<String> parameters) {
		this.id = id;
		this.parameters = parameters;
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

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String parameter) {
		this.parameters.add(parameter);
	}
}
