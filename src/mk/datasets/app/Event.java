package mk.datasets.app;

import mk.datasets.interfaces.InputText;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Event implements InputText{

	private static int counter = 1;
	private static final String TEMPORARY = "TEMPORARY";

	private final int id;
	private String name;
	private String expression;
	private List<LocalDateTime> dates = new ArrayList<>();

	enum Mark {
		NOT,
		OR,
		AND;
	}

	public Event() {
		this.id = counter++;
		this.name = "null";
		this.expression = "null";
	}

	public Event(String name) {
		this.id = counter++;
		this.name = name;
		this.expression = "null";
	}

	public Event(String name, String expression) {
		this.id = counter++;
		this.name = name;
		this.expression = expression;
	}

	public int getId() {
		return id;
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

	public List<LocalDateTime> getDates() {
		return dates;
	}

	public void setDates(List<LocalDateTime> dates) {
		this.dates = dates;
	}

	public static Event convertStringToEvent(String inputEvent) {
		inputEvent = inputEvent.replace(" ","");
		String name = "";

		//Get name
		int colonIndex = inputEvent.indexOf(":");
		for (int i = 0; i<colonIndex; i++) {
			name = name + inputEvent.charAt(i);
		}

		//Get expression
		String expression;
		expression = inputEvent.substring(colonIndex+1);

		return new Event(name, expression);
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

		if (!startIndexes.isEmpty()) {
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
		} else {
			order.put(-1,exp.length());
		}

		return order;
	}

	public static boolean duplicatesExist(List<Event> events) {
		for (Event event : events) {
			for (Event secondEvent : events) {
				if (!event.equals(secondEvent)) {
					if (event.getName().equals(secondEvent.getName())) {
						return true;
					} else if (event.getExpression().equals(secondEvent.getExpression())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void findDates(List<Primitive> primitives) {
		String nakedExpressionWithNegation = expression.replace("&"," ").replace("|", " ").
				replace("   ","  ").replace("  "," ").replace("(","").replace(")","");
		String[] primitiveNames = nakedExpressionWithNegation.split(" ");

		List<Primitive> activePrimitives = new ArrayList<>();
		for (Primitive primitive: primitives) {
			for (int i = 0; i<primitiveNames.length; i++) {
				if (primitiveNames[i].contains(primitive.getName()) && primitiveNames[i].contains("!")) {
					Primitive notPrimitive = Primitive.createNegation(primitive);
					if (!activePrimitives.contains(notPrimitive)) {
						activePrimitives.add(notPrimitive);
					}
				} else if (primitiveNames[i].contains(primitive.getName()) && !primitiveNames[i].contains("!") && !activePrimitives.contains(primitive)) {
					activePrimitives.add(primitive);
				}
			}
		}

		List<String> operationList;
		if (expression.substring(1,expression.length()).contains("(")) {
			operationList = getOperionList(expression);
		} else {
			operationList = getOperionList(expression.replace("(","").replace(")",""));
		}

		List<Event> temporaryEventsList = new ArrayList<>();
		for (int i = 0; i<operationList.size(); i++) {
			Event temporaryEvent = new Event(TEMPORARY + i, operationList.get(i));
			temporaryEventsList.add(temporaryEvent);
			for (int j = 0; j<temporaryEventsList.size(); j++) {
				if (operationList.get(i).contains(temporaryEventsList.get(j).getExpression()) && operationList.get(i).length()!=temporaryEventsList.get(j).getExpression().length()) {
					if (operationList.get(i).indexOf(temporaryEventsList.get(j).getExpression()) == 0) {
						operationList.add(i, temporaryEvent.name);
						operationList.remove(i+1);
					} else {
						char beforeExp = operationList.get(i).charAt(operationList.get(i).indexOf(temporaryEventsList.get(j).getExpression())-1);
						if (beforeExp=='(') {
							String newOperation = operationList.get(i).replace("(" + temporaryEventsList.get(j).getExpression(), "(" + temporaryEventsList.get(j).name);
							if (newOperation.indexOf(temporaryEventsList.get(j).name) != 0 && newOperation.charAt(newOperation.indexOf(temporaryEventsList.get(j).name) + temporaryEventsList.get(j).name.length()) == ')') {
								newOperation = operationList.get(i).replace("(" + temporaryEventsList.get(j).getExpression() + ")", temporaryEventsList.get(j).name);
							}
							operationList.remove(i);
							operationList.add(i, newOperation);
							Event secondEvent = new Event(TEMPORARY + i+1, newOperation);
							temporaryEventsList.remove(i);
							temporaryEventsList.add(i, secondEvent);
						}
					}
				}
			}
		}

		for (int i = 0; i<temporaryEventsList.size(); i++) {
				this.dates = Finder.getRecordsDates(temporaryEventsList.get(i).getExpression(), activePrimitives);
				activePrimitives.add(new Primitive(temporaryEventsList.get(i).getName(),this.dates));
		}
		if (primitiveNames.length==1) {
			this.dates = gerPrimitiveByName(temporaryEventsList.get(0).getExpression(), activePrimitives).getDates();
		}
	}

	public Primitive gerPrimitiveByName(String name, List<Primitive> primitiveList) {
		for (Primitive primitive: primitiveList) {
			if (primitive.getName().equalsIgnoreCase(name)) {
				return primitive;
			}
		}
		return null;
	}

	public static void resetCounter() {
		counter=1;
	}

	public String showDates() {
		String output = "\n\tDate:";
		for (LocalDateTime date: this.getDates()) {
			output = output + "\n\t" + date.toString();
		}
		return output + "\n";
	}

	public String toString() {
		return "\nID: " + this.id +
				"\nName: " + this.name +
				"\nExpression: " + this.expression;
	}
}
