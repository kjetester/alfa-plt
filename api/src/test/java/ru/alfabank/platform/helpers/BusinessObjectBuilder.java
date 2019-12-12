package ru.alfabank.platform.helpers;

import java.util.*;

public class BusinessObjectBuilder {

	public static List<String> swapOutersInArray(List<String> initList) {
		if (initList.size() > 1) {
			Collections.swap(initList, 0, initList.size() - 1);
			return initList;
		} else {
			return initList;
		}
	}
}