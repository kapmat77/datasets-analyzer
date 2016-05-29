package mk.datasets.app;

import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Event {

	private int id;
	private String name;
	private String expression;
//	private static List<String> operations = new ArrayList<>();
//	private static List<Integer> startIndexes = new ArrayList<>();
//	private static List<Integer> endIndexes = new ArrayList<>();

	enum Mark {
		NOT,
		OR,
		AND;
	}

	public Event() {
		this.id = 0;
		this.name = "null";
		this.expression = "null";
	}

	public Event(int id, String name) {
		this.id = id;
		this.name = name;
		this.expression = "null";
	}

	public Event(int id, String name, String expression, List<String> operations) {
		this.id = id;
		this.name = name;
		this.expression = expression;
//		this.operations = operations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Event convertStringToEvent(String inputEvent) {
//		"E1:(P1 || !P2) && P3";
		inputEvent = inputEvent.replace(" ","");

		String name = "";
		List<String> operations = new ArrayList<>();

		//Get name
		int colonIndex = inputEvent.indexOf(":");
		for (int i = 0; i<colonIndex; i++) {
			name = name + inputEvent.charAt(i);
		}

		//Get operations in appropriate order
		for (int i = colonIndex+1; i<inputEvent.length(); i++) {

		}

		return new Event(1, name, inputEvent, operations);
	}

	public static List<String> getOperionList(String exp){
		List<String> operations = new ArrayList<>();
		exp = exp.replace(" ","");

		Map<Integer,Integer> order;
		order = findOrder(exp);

		String singleExp = "";
		for (Map.Entry<Integer, Integer> entry: order.entrySet()) {
			singleExp = exp.substring(entry.getKey()+1, entry.getValue());
			operations.add(singleExp);
		}
		operations.add(exp);

		return operations;
	}

	private static Map<Integer, Integer> findOrder(String exp) {
		List<Integer> startIndexes = new ArrayList<>();
		Map<Integer,Integer> endIndexes = new HashMap<>();
		Map<Integer,Integer> order = new LinkedHashMap <>();

		for (int i = 0; i<exp.length(); i++) {
			if (exp.charAt(i) == '(') {
				startIndexes.add(i);
				break;
			}
		}

		for (int i = startIndexes.get(0)+1; i<exp.length(); i++) {
			if (exp.charAt(i)=='(') {
				startIndexes.add(i);
			}
			if (exp.charAt(i)==')') {
				for (int k = startIndexes.size()-1; k>=0; k--) {
					if (!endIndexes.containsKey(startIndexes.get(k))) {
						endIndexes.put(startIndexes.get(k),i);
						break;
					}
				}
			}
		}

		for (int k = 0; k<startIndexes.size(); k++) {
			int currentKey = startIndexes.get(k);
			int currentValue = endIndexes.get(currentKey);
			boolean findAnother = false;
			for (Map.Entry<Integer, Integer> entry: endIndexes.entrySet()) {
				if (entry.getValue() < currentValue && !order.containsKey(entry.getKey())) {
					findAnother = true;
					break;
				}
			}
			if (!findAnother) {
				order.put(currentKey, endIndexes.get(currentKey));
			}
			if ((k+1) == startIndexes.size() && order.size() != startIndexes.size()) {
				k = -1;
			}
		}
		return order;
	}

	public String toString() {
		return "ID: " + this.id +
				"\nName: " + this.name +
				"\nExpression: " + this.expression;
	}
}
