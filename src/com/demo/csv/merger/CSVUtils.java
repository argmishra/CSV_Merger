package com.demo.csv.merger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CSVUtils {

	public static List<String> getHeaders(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> headerList = new ArrayList<>();

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			headerList = new ArrayList<>(Arrays.asList(values));
			break;
		}
		br.close();

		return headerList;
	}

	public static List<CSVMap> getRows(File file, List<String> keys) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<CSVMap> recordList = new ArrayList<>();
		boolean checkHeader = true;

		String line = null;
		while ((line = br.readLine()) != null) {
			if (checkHeader) {
				checkHeader = false;
				continue;
			}

			CSVMap records = new CSVMap(file.getName());
			String[] value = line.split(",");
			for (int i = 0; i < value.length; i++) {
				if (keys.get(i).equals("MaturityDate")) {
					records.put(keys.get(i), getMaturityDate(value[i]));
				} else {
					records.put(keys.get(i), value[i]);
				}

				if (file.getName().equals("valuation.csv")) {
					Double val = Double.parseDouble(value[2]) - Double.parseDouble(value[1]);
					records.put("MS_PC", val.toString());
					records.put("BreakStatus", getRange(Math.abs(val)));
				}

			}
			recordList.add(records);
		}

		br.close();

		return recordList;
	}

	public static void writeAndSaveFile(File file, List<String> headers, List<CSVMap> records) throws IOException {
		FileWriter csvFile = new FileWriter(file);

		String seprator = "";
		String[] headerArr = headers.toArray(new String[headers.size()]);
		for (String header : headerArr) {
			csvFile.append(seprator);
			csvFile.append(header);
			seprator = ",";
		}

		csvFile.append("\n");

		for (CSVMap record : records) {
			seprator = "";
			for (int i = 0; i < headerArr.length; i++) {
				csvFile.append(seprator);
				if (record.get(headerArr[i]) == null) {
					csvFile.append("blank");
				} else {
					csvFile.append(record.get(headerArr[i]));
				}
				seprator = ",";

			}
			csvFile.append("\n");
		}

		csvFile.flush();
		csvFile.close();
	}

	private static String getMaturityDate(String val) {
		long days = getDayDifference(val);

		if (days < 0) {
			return "blank";
		}
		return getRange(days);
	}

	private static long getDayDifference(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());

		LocalDate ldate = LocalDate.parse(date, formatter);
		LocalDate cdate = LocalDate.parse(timeStamp, formatter);
		return ((cdate.toEpochDay() - ldate.toEpochDay()) / 12);
	}

	private static String getRange(double value) {
		String range = "100000+";
		if (checkRange((int) Math.round(value), 0, 99)) {
			range = "0-99";
		} else if (checkRange((int) Math.round(value), 100, 999)) {
			range = "100-999";
		} else if (checkRange((int) Math.round(value), 1000, 9999)) {
			range = "1000-9999";
		} else if (checkRange((int) Math.round(value), 10000, 99999)) {
			range = "10000-99999";
		}
		return range;
	}

	private static String getRange(long val) {
		String range = "50yr+";
		if (checkRange(val, 0, 1)) {
			range = "0m-1m";
		} else if (checkRange(val, 2, 6)) {
			range = "1m-6m";
		} else if (checkRange(val, 7, 12)) {
			range = "6m-1yr";
		} else if (checkRange(val, 13, 120)) {
			range = "1yr-10yr";
		} else if (checkRange(val, 121, 360)) {
			range = "10yr-30yr";
		} else if (checkRange(val, 361, 600)) {
			range = "30yr-50tr";
		}
		return range;
	}

	public static boolean checkRange(double x, double min, double max) {
		return x >= min && x <= max;
	}

}