package mk.datasets.app;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Event {

	private static int counter = 1;

	private final int id;
	private String name;
	private String expression;
	private List<LocalDate> dates = new ArrayList<>();

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

	public List<LocalDate> getDates() {
		return dates;
	}

	public void setDates(List<LocalDate> dates) {
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

	public void findDates(List<Dataset> datasets, List<Primitive> primitives) {
//		List<LocalDate> dates = new ArrayList<>();

		//TODO sprawdzić czy to faktycznie działa !!!
		String nakedExpressionWithNegation = expression.replace("&"," ").replace("|", " ").
				replace("   ","  ").replace("  "," ").replace("(","").replace(")","");
		String[] primitiveNames = nakedExpressionWithNegation.split(" ");

		//TODO coś dużo tych Prymitytów wrzuca do listy, występują duplikaty
		List<Primitive> activePrimitives = new ArrayList<>();
		for (Primitive primitive: primitives) {
			for (int i = 0; i<primitiveNames.length; i++) {
				if (primitiveNames[i].contains(primitive.getName()) && primitiveNames[i].contains("!")) {
					Primitive notPrimitive = Primitive.createNegation(primitive);
					activePrimitives.add(notPrimitive);
					break;
				} else if (primitiveNames[i].contains(primitive.getName()) && !primitiveNames[i].contains("!")) {
					activePrimitives.add(primitive);
					break;
				}
			}
		}

		//TODO Dokończyć metodę !!!
		//TODO TYMCZASOWO
		if (primitiveNames.length>1) {
			int secondPrimitiveIndex = expression.indexOf(activePrimitives.get(1).getName());
			switch (expression.substring(secondPrimitiveIndex-2, secondPrimitiveIndex)) {
				case "||":
					this.dates = Finder.getRecordsDates(activePrimitives.get(0), Event.Mark.OR, activePrimitives.get(1));
					break;
				case "&&":
					this.dates = Finder.getRecordsDates(activePrimitives.get(0), Event.Mark.AND, activePrimitives.get(1));
					break;
			}
		} else {
			this.dates = activePrimitives.get(0).getDates();
		}
	}

	public String toString() {
		return "\nID: " + this.id +
				"\nName: " + this.name +
				"\nExpression: " + this.expression;
	}
}
