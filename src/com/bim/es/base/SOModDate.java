package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOModDate extends SOBase {
	private static ArrayList<SOModDate> list;

	public SOModDate(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOModDate> l = getList();
		for (SOModDate t : l) {
			if (t.getId().equals(id)) {
				return t.getLabel();
			}
		}
		return null;
	}

	public static int findPositionById(String id) {
		if (Util.isNull(id)) {
			return -1;
		}
		List<SOModDate> l = getList();
		int cnt = 0;
		for (SOModDate t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOModDate> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOModDate> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOModDate t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOModDate> getList() {
		if ( list != null ) {
			return list;
		}
		list = new ArrayList<SOModDate>();
		list.add(new SOModDate("", "Any Date"));
		list.add(new SOModDate("30", "30 days"));
		list.add(new SOModDate("60", "60 days"));
		list.add(new SOModDate("90", "90 days"));
		list.add(new SOModDate("180", "180 days"));
		list.add(new SOModDate("1", "1 year"));
		list.add(new SOModDate("2", "2 year"));
		list.add(new SOModDate("3", "3 year"));
		list.add(new SOModDate("5", "5 year"));
		list.add(new SOModDate("10", "10 year"));
		return list;
	}
}