package mk.datasets.app;

import java.time.LocalDateTime;

/**
 * Created by Kapmat on 2016-05-29.
 */
public class Pattern {

	private LocalDateTime startDate;
	private LocalDateTime endDate;

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

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String absence(Event event) {
		boolean patternDetected = true;
		for (LocalDateTime date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.ABSENCE.name() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.ABSENCE.name() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO zmienić wszystkie patterny plusDay
	public String invariance(Event event) {
		boolean patternDetected = true;
		LocalDateTime startd = LocalDateTime.from(startDate);
		while (startd.isBefore(endDate)) {
			startd = startd.plusDays(1);
			if (!event.getDates().contains(startd)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.INVARIANCE.name() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.INVARIANCE.name() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String existence(Event event) {
		boolean patternDetected = false;
		for (LocalDateTime date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = true;
				break;
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.EXISTENCE.name() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.EXISTENCE.name() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String response(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		if (!firstEvent.getDates().isEmpty()) {
			LocalDateTime firstDate = firstEvent.getDates().get(0);
			for (LocalDateTime secondDate: secondEvent.getDates()) {
				if ((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) && firstDate.isBefore(endDate) && (secondDate.isBefore(endDate) || secondDate.isEqual(endDate)) &&
						firstDate.isBefore(secondDate)) {
					patternDetected = true;
					break;
				}
			}
		} else {
			patternDetected = false;
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.RESPONSE.name() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.RESPONSE.name() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO przeanalizować jeszcze
	public String obligation(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		for (LocalDateTime firstDate: firstEvent.getDates()) {
			for (LocalDateTime secondDate: secondEvent.getDates()) {
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
			return "TRUE - Wzorzec " + Name.OBLIGATION.name() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.OBLIGATION.name() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO zmienić wszystkie patterny plusDay
	//TODO zaimplementować
	public String responsively(Event event) {
		boolean patternDetected = true;
//		for (LocalDateTime date: event.getDates()) {
//			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
//				LocalDateTime helpDate = LocalDateTime.from(date);
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
//			return "TRUE - Wzorzec " + Name.RESPONSIVELY.name() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
//		} else {
//			return "FALSE - Wzorzec " + Name.RESPONSIVELY.name() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
//		}
		return "Wzorzec nie zaimplementowany !";
	}

	//TODO zmienić wszystkie patterny plusDay
	public String persistence(Event event) {
		boolean patternDetected = true;
		for (LocalDateTime date: event.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				LocalDateTime helpDate = LocalDateTime.from(date);
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
			return "TRUE - Wzorzec " + Name.PERSISTENCE.name() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.PERSISTENCE.name() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO przetestować
	//TODO zmienić wszystkie patterny plusDay
	public String reactivity(Event firstEvent, Event secondEvent) {
		boolean firstPersistance = false;
		boolean patternDetected = true;
		//first event
		LocalDateTime startPersistanceDate = LocalDateTime.now();
		for (LocalDateTime date: firstEvent.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				startPersistanceDate = LocalDateTime.from(date);
				LocalDateTime helpDate = LocalDateTime.from(date);
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
			for (LocalDateTime date: secondEvent.getDates()) {
				if ((date.isAfter(startPersistanceDate) || date.isEqual(startPersistanceDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
					LocalDateTime helpDate = LocalDateTime.from(date);
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
			return "TRUE - Wzorzec " + Name.REACTIVITY.name() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.REACTIVITY.name() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}
}
