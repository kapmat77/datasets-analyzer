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
			return "Wzorzec " + Name.ABSENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.ABSENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
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
			return "Wzorzec " + Name.INVARIANCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.INVARIANCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
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
			return "Wzorzec " + Name.EXISTENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.EXISTENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
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
			return "Wzorzec " + Name.RESPONSE.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.RESPONSE.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO przeanalizować jeszcze
	public String obligation(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		for (LocalDate firstDate: firstEvent.getDates()) {
			for (LocalDate secondDate: secondEvent.getDates()) {
				if ((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) && firstDate.isBefore(endDate) && (secondDate.isBefore(endDate) || secondDate.isEqual(endDate)) &&
						firstDate.isBefore(secondDate)) {
					patternDetected = true;
					break;
				} else if ((firstDate.isBefore(startDate) || firstDate.isAfter(endDate))) {
					patternDetected = true;
					break;
				}
			}
		}
		if (patternDetected) {
			return "Wzorzec " + Name.OBLIGATION.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.OBLIGATION.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO zaimplementować
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
//		if (patternDetected) {
//			return "Wzorzec " + Name.RESPONSIVELY.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
//		} else {
//			return "Wzorzec " + Name.RESPONSIVELY.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
//		}
		return "Wzorzec nie zaimplementowany !";
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
			return "Wzorzec " + Name.PERSISTENCE.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.PERSISTENCE.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO przetestować
	public String reactivity(Event firstEvent, Event secondEvent) {
		boolean firstPersistance = false;
		boolean patternDetected = true;
		//first event
		LocalDate startPersistanceDate = LocalDate.now();
		for (LocalDate date: firstEvent.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				startPersistanceDate = LocalDate.from(date);
				LocalDate helpDate = LocalDate.from(date);
				firstPersistance = true;
				while (helpDate.isBefore(endDate)) {
					helpDate = helpDate.plusDays(1);
					if (!firstEvent.getDates().contains(helpDate)) {
						firstPersistance = false;
						break;
					}
				}
			}
			if (!firstPersistance) {
				break;
			}
		}
		if (firstPersistance) {
			for (LocalDate date: secondEvent.getDates()) {
				if ((date.isAfter(startPersistanceDate) || date.isEqual(startPersistanceDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
					LocalDate helpDate = LocalDate.from(date);
					while (helpDate.isBefore(endDate)) {
						helpDate = helpDate.plusDays(1);
						if (!secondEvent.getDates().contains(helpDate)) {
							patternDetected = false;
							break;
						}
					}
				}
				if (!patternDetected) {
					break;
				}
			}
		} else {
			patternDetected = false;
		}
		if (patternDetected) {
			return "Wzorzec " + Name.REACTIVITY.name() + " występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		} else {
			return "Wzorzec " + Name.REACTIVITY.name() + " nie występuje dla eventów " + firstEvent.getName() + "->" + secondEvent.getName() + startDate.toString() + " - " + endDate.toString();
		}
	}
}
