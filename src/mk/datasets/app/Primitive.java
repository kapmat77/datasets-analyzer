package mk.datasets.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Primitive {

	private int id;
	private String name;
	private int datasetId;
	private String attribute;
	private Mark mark;
	private Double value;
	private List<Record> records = new ArrayList<>();

	enum Mark {
		EQUAL,
		NOT_EQUAL,
		MORE_THAN,
		LESS_THAN,
		EQUAL_OR_LESS_THAN,
		EQUAL_OR_MORE_THAN,
		NONE;
	}

	public Primitive() {
		this.id = 0;
		this.name = null;
		this.datasetId = 0;
		this.attribute = null;
		this.mark = null;
		this.value = null;
	}

	public Primitive(int id, String name, int databaseId, String attribute, Mark mark, Double value) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Primitive convertStringToPrimitive(String inputPrimitive) {

		//Delete all spaces
		inputPrimitive = inputPrimitive.replace(" ","");

		int datasetId;
		String name = "";
		String attribute;
		Mark mark = Mark.NONE;
		Double value;

		//Get name
		int colonIndex = inputPrimitive.indexOf(":");
		for (int i = 0; i<colonIndex; i++) {
			name = name + inputPrimitive.charAt(i);
		}

		//Get datasetId
		String datasetIdText = "";
		int dotIndex = inputPrimitive.indexOf(".");
		for (int i = colonIndex+1; i<dotIndex; i++) {
			datasetIdText = datasetIdText + String.valueOf(inputPrimitive.charAt(i));
		}
		datasetId = Integer.valueOf(datasetIdText);

		//Get mark
		int firstMarkIndex = 0, secondMarkIndex = 0;
		for (int i = dotIndex+1; i<inputPrimitive.length(); i++) {
			switch (inputPrimitive.charAt(i)) {
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
					if (inputPrimitive.charAt(i+1)=='=') {
						mark = Mark.EQUAL_OR_LESS_THAN;
						secondMarkIndex = i+1;
					} else {
						mark = Mark.LESS_THAN;
						secondMarkIndex = 0;
					}
					break;
				case '>':
					firstMarkIndex = i;
					if (inputPrimitive.charAt(i+1)=='=') {
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
		attribute = inputPrimitive.substring(dotIndex+1, firstMarkIndex);

		//Get value
		if (secondMarkIndex==0) {
			value = Double.valueOf(inputPrimitive.substring(firstMarkIndex+1).replace(",","."));
		} else {
			value = Double.valueOf(inputPrimitive.substring(secondMarkIndex+1).replace(",","."));
		}

		return new Primitive(1, name, datasetId, attribute, mark, value);
	}

	public void findRecords(Dataset dataset) {
		records = new ArrayList<>();
		for (Record record: dataset.getRecords()) {
			try {
				switch (this.mark) {
					case EQUAL:
						if ((record.getParameters().get(this.attribute)).equals(String.valueOf(this.value))) {
							records.add(record);
						}
						break;
					case NOT_EQUAL:
						if (!(record.getParameters().get(this.attribute)).equals(String.valueOf(this.value))) {
							records.add(record);
						}
						break;
					case LESS_THAN:
						if (Double.valueOf(record.getParameters().get(this.attribute)) < (this.value)) {
							records.add(record);
						}
						break;
					case MORE_THAN:
						if (Double.valueOf(record.getParameters().get(this.attribute)) > (this.value)) {
							records.add(record);
						}
						break;
					case EQUAL_OR_LESS_THAN:
						if (Double.valueOf(record.getParameters().get(this.attribute)) <= (this.value)) {
							records.add(record);
						}
						break;
					case EQUAL_OR_MORE_THAN:
						if (Double.valueOf(record.getParameters().get(this.attribute)) >= (this.value)) {
							records.add(record);
						}
						break;
					case NONE:
						break;
				}
			} catch (NullPointerException e) {
				//Tekst zamiast liczby (najczęściej N/A) a więc pomijamy wiersz
			}
		}
	}

	public static boolean duplicatesExist(List<Primitive> primitives) {
		for (Primitive primitive : primitives) {
			for (Primitive secondPrimitive : primitives) {
				if (!primitive.equals(secondPrimitive)) {
					if (primitive.getName().equals(secondPrimitive.getName())) {
						return true;
					} else if (primitive.getValue().equals(secondPrimitive.getValue()) &&
							primitive.getAttribute().equals(secondPrimitive.getAttribute()) &&
							primitive.getMark().equals(secondPrimitive.getMark()) &&
							primitive.getDatasetId() == secondPrimitive.getDatasetId()) {
						return true;
					}
				}
			}
		}
		return false;
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
