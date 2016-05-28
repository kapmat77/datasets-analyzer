package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Condition {

	private int id;
	private String name;
	private int datasetId;
	private String attribute;
	private Mark mark;
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
		this.name = null;
		this.datasetId = 0;
		this.attribute = null;
		this.mark = null;
		this.value = null;
	}

	public Condition(int id, String name, int databaseId, String attribute, Mark mark, Double value) {
		this.id = id;
		this.name = name;
		this.datasetId = databaseId;
		this.attribute = attribute;
		this.mark = mark;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(int datasetId) {
		this.datasetId = datasetId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public static Condition convertStringToCondition(String inputCondition) {

		//Delete all spaces
		inputCondition = inputCondition.replace(" ","");

		int datasetId;
		String name = "";
		String attribute;
		Mark mark = Mark.NONE;
		Double value;

		//Get name
		int colonIndex = inputCondition.indexOf(":");
		for (int i = 0; i<colonIndex; i++) {
			name = name + inputCondition.charAt(i);
		}

		//Get datasetId
		String datasetIdText = "";
		int dotIndex = inputCondition.indexOf(".");
		for (int i = colonIndex+1; i<dotIndex; i++) {
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

		return new Condition(1, name, datasetId, attribute, mark, value);
	}

	public String toString() {
		return "ID: " + this.id +
				"\nName: " + this.name +
				"\nDatasetID: " + this.datasetId +
				"\nAttribute: " + this.attribute +
				"\n Mark: " + this.mark.name() +
				"\n Value: " + this.value;
	}
}
