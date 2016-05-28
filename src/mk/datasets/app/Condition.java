package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Condition {

	private int id;
	private int databaseId;
	private String attribute;
	private Mark type;
	private Double value;

	enum Mark {
		EQUAL,
		NOT_EQUAL,
		MORE_THAN,
		LESS_THEN
	}

	public Condition() {
		this.id = 0;
		this.databaseId = 0;
		this.attribute = null;
		this.type = null;
		this.value = null;
	}

	public Condition(int id, int databaseId, String attribute, Mark type, Double value) {
		this.id = id;
		this.databaseId = databaseId;
		this.attribute = attribute;
		this.type = type;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Mark getMark() {
		return type;
	}

	public void setMark(Mark type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
