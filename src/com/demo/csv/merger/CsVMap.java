package com.demo.csv.merger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CsVMap {

	private Map<String, String> csvMap;

	public CsVMap(String id) {
		csvMap = new LinkedHashMap<>();
	}

	public void put(String key, String val) {
		csvMap.put(key, val);
	}

	public String get(String key) {
		return csvMap.get(key);
	}

}
