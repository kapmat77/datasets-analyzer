package mk.datasets.app;

import mk.datasets.interfaces.InputText;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Finder {

	private static final String TEMPEVENT = "TEMPEVENT";
	private static int counter = 0;

	public static List<LocalDateTime> getRecordsDates(String expression, List<Primitive> activePrimitives) {
		List<LocalDateTime> dates = new ArrayList<>();

		String nakedExpressionWithNegation = expression.replace("&"," ").replace("|", " ").
				replace("   ","  ").replace("  "," ").replace("(","").replace(")","");
		String[] primitiveNames = nakedExpressionWithNegation.split(" ");
		List<String> primitivesList = new ArrayList<>();
		primitivesList.addAll(arrayToList(primitiveNames));

		List<Event> tempEventList = new ArrayList<>();
		for (int i = 0; i<primitivesList.size(); i++) {
			while (primitivesList.size() > 2) {
				String firstName = primitivesList.get(0);
				String secondName = primitivesList.get(1);
//				int secondPrimitiveIndex = expression.indexOf(activePrimitives.get(1).getName());
				int secondPrimitiveIndex = expression.indexOf(secondName);
				Event temporaryEvent = new Event(TEMPEVENT + counter,firstName+expression.substring(secondPrimitiveIndex-2, secondPrimitiveIndex)+secondName);
				counter++;
				tempEventList.add(temporaryEvent);
				switch (expression.substring(secondPrimitiveIndex-2, secondPrimitiveIndex)) {
					case "||":
						dates = getRecordsDates(getObjectDatesFromListByName(firstName, activePrimitives, tempEventList), Event.Mark.OR, getObjectDatesFromListByName(secondName, activePrimitives, tempEventList));
						break;
					case "&&":
						dates = getRecordsDates(getObjectDatesFromListByName(firstName, activePrimitives, tempEventList), Event.Mark.AND, getObjectDatesFromListByName(secondName, activePrimitives, tempEventList));
						break;
				}
				primitivesList.remove(1);
				primitivesList.remove(0);
				primitivesList.add(0, temporaryEvent.getName());
				temporaryEvent.setDates(dates);
			}
			if (primitivesList.size()==2) {
				String firstName = primitivesList.get(0);
				String secondName = primitivesList.get(1);
				int secondPrimitiveIndex = expression.indexOf(secondName);
				switch (expression.substring(secondPrimitiveIndex-2, secondPrimitiveIndex)) {
					case "||":
						dates = getRecordsDates(getObjectDatesFromListByName(firstName, activePrimitives, tempEventList), Event.Mark.OR, getObjectDatesFromListByName(secondName, activePrimitives, tempEventList));
						break;
					case "&&":
						dates = getRecordsDates(getObjectDatesFromListByName(firstName, activePrimitives, tempEventList), Event.Mark.AND, getObjectDatesFromListByName(secondName, activePrimitives, tempEventList));
						break;
				}
			}

		}
//		switch (mark) {
//			case AND:
//				for (Record firstRecord: firstPrimitive.getRecords()) {
//					for (Record secondRecord: secondPrimitive.getRecords()) {
//						if (firstRecord.getLocalDateTime().equals(secondRecord.getLocalDateTime())) {
//							dates.add(firstRecord.getLocalDateTime());
//						}
//					}
//				}
//				break;
//			case OR:
//				for (Record record: firstPrimitive.getRecords()) {
//					if (!dates.contains(record.getLocalDateTime())) {
//						dates.add(record.getLocalDateTime());
//					}
//				}
//				for (Record record: secondPrimitive.getRecords()) {
//					if (!dates.contains(record.getLocalDateTime())) {
//						dates.add(record.getLocalDateTime());
//					}
//				}
//				break;
//		}
//		//Sort dates - from oldest data to newest data
//		Collections.sort(dates, LocalDateTime::compareTo);
		return dates;
	}

	public static List<LocalDateTime> getRecordsDates(Primitive firstPrimitive, Event.Mark mark, Primitive secondPrimitive) {
		List<LocalDateTime> dates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (Record firstRecord: firstPrimitive.getRecords()) {
					for (Record secondRecord: secondPrimitive.getRecords()) {
						if (firstRecord.getLocalDateTime().equals(secondRecord.getLocalDateTime())) {
							dates.add(firstRecord.getLocalDateTime());
						}
					}
				}
				break;
			case OR:
				for (Record record: firstPrimitive.getRecords()) {
					if (!dates.contains(record.getLocalDateTime())) {
						dates.add(record.getLocalDateTime());
					}
				}
				for (Record record: secondPrimitive.getRecords()) {
					if (!dates.contains(record.getLocalDateTime())) {
						dates.add(record.getLocalDateTime());
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(dates, LocalDateTime::compareTo);
		return dates;
	}

	public static List<LocalDateTime> getRecordsDates(List<LocalDateTime> dates, Event.Mark mark, Primitive primitive) {
		List<LocalDateTime> newDates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (LocalDateTime date: dates) {
					for (Record record: primitive.getRecords()) {
						if (date.equals(record.getLocalDateTime())) {
							newDates.add(date);
						}
					}
				}
				break;
			case OR:
				for (LocalDateTime date: dates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				for (Record record: primitive.getRecords()) {
					if (!newDates.contains(record.getLocalDateTime())) {
						newDates.add(record.getLocalDateTime());
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(newDates, LocalDateTime::compareTo);
		return newDates;
	}

	public static List<LocalDateTime> getRecordsDates(List<LocalDateTime> firstDates, Event.Mark mark, List<LocalDateTime> secondDates) {
		List<LocalDateTime> newDates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (LocalDateTime firstDate: firstDates) {
					for (LocalDateTime secondDate: secondDates) {
						if (firstDate.equals(secondDate)) {
							newDates.add(firstDate);
						}
					}
				}
				break;
			case OR:
				for (LocalDateTime date: firstDates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				for (LocalDateTime date: secondDates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(newDates, LocalDateTime::compareTo);
		return newDates;
	}

	public static <T extends Comparable<T>> T findMinInArray(final List<T> inputList) {
		int minIndex;
		if (inputList.isEmpty()) {
			minIndex = -1;
		} else {
			final ListIterator<T> itr = inputList.listIterator();
			T min = itr.next();
			minIndex = itr.previousIndex();
			while (itr.hasNext()) {
				final T curr = itr.next();
				if (curr.compareTo(min) < 0) {
					min = curr;
					minIndex = itr.previousIndex();
				}
			}
		}
		return inputList.get(minIndex);
	}

	public static <T extends Comparable<T>> T findMaxInArray(final List<T> inputList) {
		int maxIndex;
		if (inputList.isEmpty()) {
			maxIndex = -1;
		} else {
			final ListIterator<T> itr = inputList.listIterator();
			T max = itr.next();
			maxIndex = itr.previousIndex();
			while (itr.hasNext()) {
				final T curr = itr.next();
				if (curr.compareTo(max) > 0) {
					max = curr;
					maxIndex = itr.previousIndex();
				}
			}
		}
		return inputList.get(maxIndex);
	}

	public static <T extends InputText> List<LocalDateTime> getObjectDatesFromListByName(String name, List<T> objectList) {
		for (T t: objectList) {
			if (t.getName().equals(name)) {
				return t.getDates();
			}
		}
		return null;
	}

	public static <T extends  InputText, K extends InputText> List<LocalDateTime> getObjectDatesFromListByName(String name, List<T> objectList, List<K> secondObjectList) {
		for (T t: objectList) {
			if (t.getName().equals(name)) {
				return t.getDates();
			}
		}
		for (K k: secondObjectList) {
			if (k.getName().equals(name)) {
				return k.getDates();
			}
		}
		return null;
	}

	private static <T> List<T> arrayToList(T[] array) {
		List<T> objectList = new ArrayList<>();
		for (int i = 0; i<array.length; i++) {
			objectList.add(array[i]);
		}
		return objectList;
	}
}
