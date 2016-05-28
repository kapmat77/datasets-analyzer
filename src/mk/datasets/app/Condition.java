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
		LESS_THAN,
		EQUAL_OR_LESS_THAN,
		EQUAL_OR_MORE_THAN,
		NONE
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

	public static Condition convertStringToCondition(String inputCondition) {

		//Delete all spaces
		inputCondition = inputCondition.replace(" ","");

		//"1.USD<1.1343"
		int datasetId;
		String attribute;
		Condition.Mark mark = Mark.NONE;
		Double value;

		//Get datasetId
		String datasetIdText = "";
		int dotIndex = inputCondition.indexOf(".");
		for (int i = 0; i<dotIndex; i++) {
			datasetIdText = datasetIdText + String.valueOf(inputCondition.charAt(i));
		}
		datasetId = Integer.valueOf(datasetIdText);

		//Get mark
		int firstMarkIndex = 0, secondMarkIndex = 0;
		for (int i = dotIndex+1; i<inputCondition.length(); i++) {
			switch (inputCondition.charAt(i)) {
				case '=':
					firstMarkIndex = i;
					secondMarkIndex = i+1;
					mark = Mark.EQUAL;
					break;
				case '!':
					firstMarkIndex = i;
					secondMarkIndex = i+1;
					mark = Mark.NOT_EQUAL;
					break;
				case '<':
					firstMarkIndex = i;
					if (inputCondition.charAt(i+1)=='=') {
						mark = Mark.EQUAL_OR_LESS_THAN;
						secondMarkIndex = i+1;
					} else {
						mark = Mark.LESS_THAN;
						secondMarkIndex = 0;
					}
					break;
				case '>':
					firstMarkIndex = i;
					if (inputCondition.charAt(i+1)=='=') {
						mark = Mark.EQUAL_OR_MORE_THAN;
						secondMarkIndex = i+1;
					} else {
						mark = Mark.MORE_THAN;
						secondMarkIndex = 0;
					}
					break;
				default:
					break;
			}
			if (firstMarkIndex!=0) {
				break;
			}
		}

		//Get attribute
		attribute = inputCondition.substring(dotIndex+1, firstMarkIndex);

		//Get value
		if (secondMarkIndex==0) {
			value = Double.valueOf(inputCondition.substring(firstMarkIndex+1).replace(",","."));
		} else {
			value = Double.valueOf(inputCondition.substring(secondMarkIndex+1).replace(",","."));
		}

		return new Condition(1, datasetId, attribute, mark, value);
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
