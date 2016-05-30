package mk.datasets.app;

import java.time.LocalDate;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Pattern {

	private LocalDate startDate;
	private LocalDate endDate;

	public enum Name {
		ABSENCE,
		INVARIANCE,
		EXISTENCE,
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

	public String absence(Event event) {
		boolean patternDetected = true;
		for (LocalDate date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.ABSENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.ABSENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String invariance(Event event) {
		boolean patternDetected = true;
		LocalDate startd = LocalDate.from(startDate);
		while (startd.isBefore(endDate)) {
			startd = startd.plusDays(1);
			if (!event.getDates().contains(startd)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.INVARIANCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.INVARIANCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String existence(Event event) {
		boolean patternDetected = false;
		for (LocalDate date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = true;
				break;
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.EXISTENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.EXISTENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String response(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		LocalDate firstDate = firstEvent.getDates().get(0);
		for (LocalDate secondDate: secondEvent.getDates()) {
			if ((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) && firstDate.isBefore(endDate) && (secondDate.isBefore(endDate) || secondDate.isEqual(endDate)) &&
					firstDate.isBefore(secondDate)) {
				patternDetected = true;
				break;
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.RESPONSE.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.RESPONSE.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String obligation(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		for (LocalDate firstDate: firstEvent.getDates()) {
			for (LocalDate secondDate: secondEvent.getDates()) {
//				if ((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) && firstDate.isBefore(endDate) && (secondDate.isBefore(endDate) || secondDate.isEqual(endDate)) &&
//						firstDate.isBefore(secondDate)) {
//					patternDetected = true;
//					break;
//				} else if ((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) {
//
//				}
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.OBLIGATION.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.OBLIGATION.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String responsively(Event event) {
		boolean patternDetected = true;
//		for (LocalDate date: event.getDates()) {
//			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
//				LocalDate helpDate = LocalDate.from(date);
//				while (helpDate.isBefore(endDate) || helpDate.isEqual(endDate)) {
//					helpDate = helpDate.plusDays(1);
//					if (!event.getDates().contains(helpDate)) {
//						patternDetected = false;
//					}
//
//				}
//			}
//		}
		if (patternDetected) {
			return "\nWzorzec " + Name.RESPONSIVELY.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.RESPONSIVELY.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String persistence(Event event) {
		boolean patternDetected = true;
		for (LocalDate date: event.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				LocalDate helpDate = LocalDate.from(date);
				while (helpDate.isBefore(endDate)) {
					helpDate = helpDate.plusDays(1);
					if (!event.getDates().contains(helpDate)) {
						patternDetected = false;
						break;
					}
				}
				if (!patternDetected) {
					break;
				}
			}
		}
		if (patternDetected) {
			return "\nWzorzec " + Name.PERSISTENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.PERSISTENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String reactivity(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;

		if (patternDetected) {
			return "\nWzorzec " + Name.REACTIVITY.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		} else {
			return "\nWzorzec " + Name.REACTIVITY.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		}
	}
}
