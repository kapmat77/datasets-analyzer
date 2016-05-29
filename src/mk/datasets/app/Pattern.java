package mk.datasets.app;

import java.time.LocalDate;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Pattern {

	private LocalDate startDate;
	private LocalDate endDate;

	enum Name {
		ABSENCE,
		INVARIANCE,
		EXISTANCE,
		RESPONSE,
		OBLIGATION,
		RESPONSIVELY,
		PERSISTENCE,
		REACTIVITY
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void absence(Event event) {
		boolean patternDetected = true;
		for (LocalDate date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			System.out.println("Wzorzec " + Name.ABSENCE.name() + " jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		} else {
			System.out.println("Wzorzec " + Name.ABSENCE.name() + " nie jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		}
	}

	public void invariance(Event event) {
		boolean patternDetected = true;
		LocalDate currentDate = LocalDate.from(startDate);
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			currentDate = currentDate.plusDays(1);
			if (!event.getDates().contains(currentDate)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			System.out.println("Wzorzec " + Name.INVARIANCE.name() + " jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		} else {
			System.out.println("Wzorzec " + Name.INVARIANCE.name() + " nie jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		}
	}

	public void existance(Event event) {
		boolean patternDetected = false;
		for (LocalDate date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = true;
				break;
			}
		}
		if (patternDetected) {
			System.out.println("Wzorzec " + Name.ABSENCE.name() + " jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		} else {
			System.out.println("Wzorzec " + Name.ABSENCE.name() + " nie jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
		}
	}

	public void response(Event firstEvent, Event secondEvent) {
//		boolean patternDetected = false;
//		LocalDate firstDate = firstEvent.getDates().get(0);
//		for (LocalDate secondDate: secondEvent.getDates()) {
//			if (secondDate.isAfter(firstDate)) {
//				patternDetected = true;
//				break;
//			}
//		}
//		if (patternDetected) {
//			System.out.println("Wzorzec " + Name.ABSENCE.name() + " jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
//		} else {
//			System.out.println("Wzorzec " + Name.ABSENCE.name() + " nie jest spełniony w okresie: " + startDate.toString() + " - " + endDate.toString());
//		}
	}

	public void obligation(Event firstEvent, Event secondEvent) {

	}

	public void responsively(Event event) {

	}

	public void persistence(Event event) {

	}

	public void reactivity(Event firstEvent, Event secondEvent) {

	}
}
