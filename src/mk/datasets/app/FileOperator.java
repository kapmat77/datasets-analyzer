package mk.datasets.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by Kapmat on 2016-05-28.
 */

public class FileOperator {

	private List<Record> records = new ArrayList<>();
	private List<String> attributes = new ArrayList<>();
	private Dataset dataset;
	private DateFormat dateFormat;

	enum DateFormat {
		DATE,
		TIME,
		DATE_AND_TIME,
		FULL_DATE_ONE_COLUMN
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		switch (dateFormat) {
			case DATE:
				return "Day";
			case TIME:
				return "Hour";
			case DATE_AND_TIME:
				return "Hour";
			case FULL_DATE_ONE_COLUMN:
				return "Hour";
			default:
				return "Day";
		}
	}

	public void readDataFromFile(String file, Dataset newDataset) {
		dataset = newDataset;
		try {
			if (file.endsWith(".csv")) {
				readFromCsv(file);
			} else if (file.endsWith(".txt")) {
				readFromTxt(file);
			} else if (file.endsWith(".json")) {
				readFromJson(file);
			} else if (file.endsWith(".xlsx")) {
				readFromXlsx(file);
			}
		} catch (IOException e) {
			System.out.println("Plik nie zostal wczytany poprawnie!");
			e.printStackTrace();
			System.exit(-1);
		}
		if (attributes.size()>0 && records.size()>0) {
			dataset.setAttributes(attributes);
			dataset.setRecords(records);
		} else {
			System.out.println("Plik nie zostal wczytany poprawnie!");
			System.exit(-1);
		}
	}

	private void readFromCsv(String file) throws IOException {
		Path path = Paths.get(file);
		Scanner in = new Scanner(path);
		String line;
		String[] parts;
		line = in.nextLine();
		boolean commaFile = false;
		if (line.contains(";")) {
			parts = line.replace("\"","").split(";");
			commaFile = false;
		} else {
			parts = line.replace("\"","").split(",");
			commaFile = true;
		}
		attributes = new ArrayList<>();
		for (String part : parts) {
			attributes.add(part);
		}
		int index = 0;
		//Get data format
		dateFormat = getDataFormat();
		while (in.hasNextLine()) {
			index++;
			Record record = new Record(index);
			line = in.nextLine();
			if (!line.isEmpty()) {
				if (commaFile) {
					parts = line.split(",");
				} else {
					parts = line.replace(",", ".").split(";");
				}
				for (int i = 0; i<parts.length; i++) {
					//Check if there is hidden time in "date" column
					String checkSize = parts[i].replace("-","").replace(":","").replace(" ","").replace("\"","");
					if (checkSize.length() > 8) {
						setDateFormat(DateFormat.FULL_DATE_ONE_COLUMN);
					}
					switch (dateFormat) {
						case DATE:
							if (attributes.get(i).equalsIgnoreCase("date")) {
								String[] dateParts = parts[i].replace("\"","").split("-");
								record.setLocalDateTime(LocalDate.of(Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2])), LocalTime.of(0,0,0));
							} else {
								record.addParameter(attributes.get(i), parts[i]);
							}
							break;
						case TIME:
							if (attributes.get(i).equalsIgnoreCase("time")) {
								String[] timeParts;
								if (parts[i].contains(":")) {
									timeParts = parts[i].replace("\"","").split(":");

								} else {
									timeParts = parts[i].replace("\"","").split("-");
								}
								record.setLocalDateTime(LocalDate.of(1,1,1), LocalTime.of(Integer.valueOf(timeParts[0]),Integer.valueOf(timeParts[1]), Integer.valueOf(timeParts[2])));
							} else {
								record.addParameter(attributes.get(i), parts[i]);
							}
							break;
						case DATE_AND_TIME:
							if (attributes.get(i).equalsIgnoreCase("date") && attributes.get(i+1).equalsIgnoreCase("time")) {
								String[] dateParts = parts[i].replace("\"","").split("-");
								String[] timeParts = parts[i+1].replace("\"","").split(":");
								record.setLocalDateTime(LocalDate.of(Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2])),
										LocalTime.of(Integer.valueOf(timeParts[0]), Integer.valueOf(timeParts[1]), Integer.valueOf(timeParts[2])));
							} else {
								record.addParameter(attributes.get(i), parts[i]);
							}
							break;
						case FULL_DATE_ONE_COLUMN:
							if (attributes.get(i).equalsIgnoreCase("date") && attributes.get(i+1).equalsIgnoreCase("time")) {
								String date = parts[i].replace("\"","").replace("-", "").replace(":", "");
								String[] dateParts = date.split(" ");
								if (dateParts.length == 5) {
									record.setLocalDateTime(LocalDate.of(Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2])),
											LocalTime.of(Integer.valueOf(dateParts[3]), Integer.valueOf(dateParts[4]), 0));
								} else {
									record.setLocalDateTime(LocalDate.of(Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[2])),
											LocalTime.of(Integer.valueOf(dateParts[3]), Integer.valueOf(dateParts[4]), Integer.valueOf(dateParts[5])));
								}
							} else {
								record.addParameter(attributes.get(i), parts[i]);
							}
							break;
					}

				}
				records.add(record);
			} else {
				break;
			}
		}
		//Sort record - from oldest data to newest data
		Collections.sort(records, (record1, record2) -> record1.getLocalDateTime().compareTo(record2.getLocalDateTime()));
	}

	private DateFormat getDataFormat() {
		boolean isDate = false;
		boolean isTime = false;
		for (int i = 0; i<attributes.size(); i++) {
			if (attributes.get(i).equalsIgnoreCase("date")) {
				isDate = true;
			} else if (attributes.get(i).equalsIgnoreCase("time")) {
				isTime = true;
			}
		}
		if (isDate && isTime) {
			return DateFormat.DATE_AND_TIME;
		} else if (isDate) {
			return DateFormat.DATE;
		} else {
			return DateFormat.TIME;
		}
	}

	private void readFromTxt(String file) {
		String line;
		String[] parts;

	}

	private void readFromXlsx(String file) {
//		String line;
//		String[] parts;
//		try {
//			FileInputStream fileStream = new FileInputStream(new File(file));
//			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			Iterator<Row> rowIterator = sheet.iterator();
//			while (rowIterator.hasNext())
//			{
//				Row row = rowIterator.next();
//				Iterator<Cell> cellIterator = row.cellIterator();
//
//				while (cellIterator.hasNext())
//				{
//					Cell cell = cellIterator.next();
//					switch (cell.getCellType())
//					{
//						case Cell.CELL_TYPE_NUMERIC:
//							System.out.print(cell.getNumericCellValue() + "t");
//							break;
//						case Cell.CELL_TYPE_STRING:
//							System.out.print(cell.getStringCellValue() + "t");
//							break;
//					}
//				}
//				System.out.println("");
//			}
//			fileStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void readFromJson(String file) {
		String line;
		String[] parts;

	}
}