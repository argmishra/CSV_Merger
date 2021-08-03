package com.demo.csv.merger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CSVMap {

	private Map<String, String> csvMap;

	public CSVMap(String id) {
		csvMap = new LinkedHashMap<>();
	}

	public void put(String key, String val) {
		csvMap.put(key, val);
	}

	public String get(String key) {
		return csvMap.get(key);
	}

}
