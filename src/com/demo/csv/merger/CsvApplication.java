package com.demo.csv.merger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CsvApplication {
	static String path = System.getProperty("user.dir") + "\\src\\com\\demo\\csv\\merger";
	static String folderPath = path + "//files";

	public static void main(String args[]) throws Exception {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		// List of list of headers and values
		List<List<String>> headersList = extractHeader(listOfFiles);
		List<List<CsVMap>> valueList = new ArrayList<>();
		for (int i = 0; i < headersList.size() && i < listOfFiles.length; ++i) {
			valueList.add(CSVUtils.getRows(listOfFiles[i], headersList.get(i)));
		}

		// Unique headers
		Collection<? extends List<String>> set = new HashSet(headersList);
		headersList.clear();
		headersList.addAll(set);

		// List of header and values
		List<String> headers = new ArrayList<>();
		List<CsVMap> values = new ArrayList<>();

		for (List<String> innerlist : headersList) {
			for (String i : innerlist) {
				headers.add(i);
			}
		}

		for (List<CsVMap> innerlist : valueList) {
			for (int i = 0; i < innerlist.size(); ++i) {
				values.add(innerlist.get(i));
			}
		}

		// Write and save file
		CSVUtils.writeAndSaveFile(new File(path + "\\trade1.csv"), headers, values);
	}

	private static List<List<String>> extractHeader(File[] listOfFiles) throws IOException {
		List<List<String>> headers = new ArrayList<>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				headers.add(CSVUtils.getHeaders(file));
			}
		}
		headers.add(List.of("MS_PC", "BreakStatus"));
		return headers;
	}

}