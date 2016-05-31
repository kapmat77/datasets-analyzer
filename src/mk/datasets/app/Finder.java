package mk.datasets.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Finder {

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
}
