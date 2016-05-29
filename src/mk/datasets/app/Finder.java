package mk.datasets.app;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Finder {

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
