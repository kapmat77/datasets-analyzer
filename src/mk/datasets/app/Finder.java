package mk.datasets.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Finder {

	public static List<LocalDate> getRecordsDates(Primitive firstPrimitive, Event.Mark mark, Primitive secondPrimitive) {
		List<LocalDate> dates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (Record firstRecord: firstPrimitive.getRecords()) {
					for (Record secondRecord: secondPrimitive.getRecords()) {
						if (firstRecord.getLocalDate().equals(secondRecord.getLocalDate())) {
							dates.add(firstRecord.getLocalDate());
						}
					}
				}
				break;
			case OR:
				for (Record record: firstPrimitive.getRecords()) {
					if (!dates.contains(record.getLocalDate())) {
						dates.add(record.getLocalDate());
					}
				}
				for (Record record: secondPrimitive.getRecords()) {
					if (!dates.contains(record.getLocalDate())) {
						dates.add(record.getLocalDate());
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(dates, LocalDate::compareTo);
		return dates;
	}

	public static List<LocalDate> getRecordsDates(List<LocalDate> dates, Event.Mark mark, Primitive primitive) {
		List<LocalDate> newDates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (LocalDate date: dates) {
					for (Record record: primitive.getRecords()) {
						if (date.equals(record.getLocalDate())) {
							newDates.add(date);
						}
					}
				}
				break;
			case OR:
				for (LocalDate date: dates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				for (Record record: primitive.getRecords()) {
					if (!newDates.contains(record.getLocalDate())) {
						newDates.add(record.getLocalDate());
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(newDates, LocalDate::compareTo);
		return newDates;
	}

	public static List<LocalDate> getRecordsDates(List<LocalDate> firstDates, Event.Mark mark, List<LocalDate> secondDates) {
		List<LocalDate> newDates = new ArrayList<>();
		switch (mark) {
			case AND:
				for (LocalDate firstDate: firstDates) {
					for (LocalDate secondDate: secondDates) {
						if (firstDate.equals(secondDate)) {
							newDates.add(firstDate);
						}
					}
				}
				break;
			case OR:
				for (LocalDate date: firstDates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				for (LocalDate date: secondDates) {
					if (!newDates.contains(date)) {
						newDates.add(date);
					}
				}
				break;
		}
		//Sort dates - from oldest data to newest data
		Collections.sort(newDates, LocalDate::compareTo);
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
}
