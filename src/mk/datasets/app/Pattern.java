package mk.datasets.app;

import java.time.LocalDateTime;
import java.util.*;

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
		REACTIVITY;

		public String getName() {
			switch (this) {
				case ABSENCE:
					return "ABSENCJA";
				case INVARIANCE:
					return "NIEZMIENNICZOŚĆ";
				case EXISTENCE:
					return "MOŻLIWOŚĆ";
				case RESPONSE:
					return "RESPONSYWNOŚĆ ZDARZEŃ";
				case OBLIGATION:
					return "OBLIGACJA";
				case RESPONSIVELY:
					return "RESPONSYWNOŚĆ (AUTO)";
				case PERSISTENCE:
					return "PERSYSTANCJA";
				case REACTIVITY:
					return "REAKTYWNOŚĆ";
				default:
					return "NONE";
			}
		}
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
			return "TRUE - Wzorzec " + Name.ABSENCE.getName() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.ABSENCE.getName() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String invariance(Event event) {
		boolean patternDetected = true;
		LocalDateTime startd = LocalDateTime.from(startDate);
		while (startd.isBefore(endDate)) {
			startd = nextDate(startd, DatasetAnalyzer.smallestDateFormat());
			if (!event.getDates().contains(startd)) {
				patternDetected = false;
				break;
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.INVARIANCE.getName() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		} else {
			return "FALSE - Wzorzec " + Name.INVARIANCE.getName() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String existence(Event event) {
		List<LocalDateTime> dateList = new ArrayList<>();
		boolean patternDetected = false;
		for (LocalDateTime date: event.getDates()) {
			if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.isEqual(startDate) || date.isEqual(endDate)) {
				patternDetected = true;
				dateList.add(date);
//				break;
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.EXISTENCE.getName() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateList);
		} else {
			return "FALSE - Wzorzec " + Name.EXISTENCE.getName() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	public String response(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		Map<LocalDateTime, LocalDateTime> dateMap = new LinkedHashMap<>();
		List<LocalDateTime> usedDates = new ArrayList<>();
		if (!firstEvent.getDates().isEmpty()) {
			for (LocalDateTime firstDate: firstEvent.getDates()) {
				for (LocalDateTime secondDate: secondEvent.getDates()) {
					if (((firstDate.isAfter(startDate) || firstDate.isEqual(startDate)) && firstDate.isBefore(endDate) && (secondDate.isBefore(endDate) || secondDate.isEqual(endDate)) &&
							firstDate.isBefore(secondDate)) && !usedDates.contains(secondDate)) {
						dateMap.put(firstDate, secondDate);
						usedDates.add(secondDate);
						patternDetected = true;
						break;
					}
				}
			}
		} else {
			patternDetected = false;
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.RESPONSE.getName() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateMap);
		} else {
			return "FALSE - Wzorzec " + Name.RESPONSE.getName() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}


	}

	//TODO przeanalizować jeszcze
	public String obligation(Event firstEvent, Event secondEvent) {
		boolean patternDetected = false;
		Map<LocalDateTime, LocalDateTime> dateMap = new LinkedHashMap<>();
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
			return "TRUE - Wzorzec " + Name.OBLIGATION.getName() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateMap);
		} else {
			return "FALSE - Wzorzec " + Name.OBLIGATION.getName() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO zaimplementować
	public String responsively(Event event) {
		boolean patternDetected = true;
		List<LocalDateTime> dateList = new ArrayList<>();
//		for (LocalDateTime date: event.getDates()) {
//			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
//				LocalDateTime helpDate = LocalDateTime.from(date);
//				while (helpDate.isBefore(endDate) || helpDate.isEqual(endDate)) {
//					helpDate = nextDate(helpDate, DatasetAnalyzer.smallestDateFormat());
//					if (!event.getDates().contains(helpDate)) {
//						patternDetected = false;
//					}
//
//				}
//			}
//		}
//		if (patternDetected) {
//			return "TRUE - Wzorzec " + Name.RESPONSIVELY.getName() + " występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateList);
//		} else {
//			return "FALSE - Wzorzec " + Name.RESPONSIVELY.getName() + " nie występuje dla eventu " + event.getName() + " w okresie: " + startDate.toString() + " - " + endDate.toString();
//		}
		return "Wzorzec nie zaimplementowany !";
	}

	public String persistence(Event event) {
		boolean patternDetected = true;
		List<LocalDateTime> dateList = new ArrayList<>();
		for (LocalDateTime date: event.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				LocalDateTime helpDate = LocalDateTime.from(date);
				while (helpDate.isBefore(endDate)) {
					helpDate = nextDate(helpDate, DatasetAnalyzer.smallestDateFormat());
					patternDetected = true;
					if (!event.getDates().contains(helpDate)) {
						patternDetected = false;
					}
				}
				if (!patternDetected) {
					break;
				}
			}
		}
		if (patternDetected) {
			return "TRUE - Wzorzec " + Name.PERSISTENCE.getName() + " występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateList);
		} else {
			return "FALSE - Wzorzec " + Name.PERSISTENCE.getName() + " nie występuje dla eventu '" + event.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	//TODO przetestować
	public String reactivity(Event firstEvent, Event secondEvent) {
		boolean firstPersistance = false;
		boolean patternDetected = true;
		Map<LocalDateTime, LocalDateTime> dateMap = new LinkedHashMap<>();
		//first event
		LocalDateTime startPersistanceDate = LocalDateTime.now();
		for (LocalDateTime date: firstEvent.getDates()) {
			if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate))) {
				startPersistanceDate = LocalDateTime.from(date);
				LocalDateTime helpDate = LocalDateTime.from(date);
				firstPersistance = true;
				while (helpDate.isBefore(endDate)) {
					helpDate = nextDate(helpDate, DatasetAnalyzer.smallestDateFormat());
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
			return "TRUE - Wzorzec " + Name.REACTIVITY.getName() + " występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString() + showDates(dateMap);
		} else {
			return "FALSE - Wzorzec " + Name.REACTIVITY.getName() + " nie występuje dla eventów '" + firstEvent.getName() + "->" + secondEvent.getName() + "' w okresie: " + startDate.toString() + " - " + endDate.toString();
		}
	}

	private LocalDateTime nextDate(LocalDateTime date, FileOperator.DateSmallestPart smallestPart) {
		switch (smallestPart) {
			case DAY:
				return date.plusDays(1);
			case HOUR:
				return date.plusHours(1);
			case MINUTE:
				return date.plusMinutes(1);
			case SECOND:
				return date.plusSeconds(1);
			default:
				return LocalDateTime.MAX;
		}
	}

	private String showDates(List<LocalDateTime> dateList) {
		String output = "\n\tDate:";
		for (LocalDateTime date: dateList) {
			output = output + "\n\t" + date.toString();
		}
		return output + "\n";
	}

	private String showDates(Map<LocalDateTime, LocalDateTime> dateMap) {
		String output = "\n\tDate:";
		for (Map.Entry<LocalDateTime, LocalDateTime> entry: dateMap.entrySet()) {

			output = output + "\n\t" + entry.getKey().toString() + " -> " + entry.getValue().toString();
		}
		return output + "\n";
	}
}
