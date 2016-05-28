package mk.datasets.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Event {

	private int id;
	private String name;
	private String expression;
	private List<String> operations = new ArrayList<>();

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
		this.operations = operations;
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

	public static String getExpressionBetweenBrackets(String exp, int index){
//		P5 && ((P1 || !P2) && P3) && P4;

		int startIndex = -1, endIndex = 0;
		int counter = 0;
		for (int i = index; i<exp.length(); i++) {
			if (exp.charAt(i)=='(') {
				startIndex = i;
			}
			if (startIndex!=-1) {
				if (exp.charAt(i)=='(' && startIndex!=i) {
					counter++;
				}
				if (exp.charAt(i)==')' && counter==0) {
					endIndex = i;
				} else if (exp.charAt(i)==')' && counter!=0) {
					counter--;
				}
			}
		}

		return exp.substring(startIndex, endIndex);
	}

	public String toString() {
		return "ID: " + this.id +
				"\nName: " + this.name +
				"\nExpression: " + this.expression;
	}
}
