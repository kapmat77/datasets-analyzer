package mk.datasets.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Record implements Comparable{

	private int id;
	private int timeId;
	private Map<String, String> parameters = new HashMap<>();
	private LocalDateTime localDateTime;

	public Record() {
		this.id = 0;
		this.timeId = 0;
		this.localDateTime = LocalDateTime.of(1,1,1,0,0);
	}

	public Record(int id) {
		this.id = id;
		this.timeId = 0;
		this.localDateTime = LocalDateTime.of(1,1,1,0,0);
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

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public void setLocalDateTime(LocalDate localDate, LocalTime localTime) {
		this.localDateTime = LocalDateTime.of(localDate, localTime);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String attribute, String parameter) {
		this.parameters.put(attribute, parameter);
	}

	//Compare dates
	@Override
	public int compareTo(Object o) {
		if (this.localDateTime.isAfter(((Record)o).getLocalDateTime())) {
			return 1;
		} else if (this.localDateTime.isEqual(((Record)o).getLocalDateTime())) {
			return 0;
		} else {
			return -1;
		}
	}
}
