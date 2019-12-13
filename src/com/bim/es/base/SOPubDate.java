package com.bim.es.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bim.core.EDate;
import com.bim.core.Util;

public class SOPubDate extends SOBase {
	private static ArrayList<SOPubDate> list;

	public SOPubDate(String id, String name) {
		super(id, name);
	}
	
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOPubDate> l = getList();
		for (SOPubDate t : l) {
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
		List<SOPubDate> l = getList();
		int cnt = 0;
		for (SOPubDate t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOPubDate> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOPubDate> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOPubDate t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOPubDate> getList() {
		if ( list != null ) {
			return list;
		}
		list = new ArrayList<SOPubDate>();
		list.add(new SOPubDate("", "Any Date"));
		list.add(new SOPubDate("30", "30 days"));
		list.add(new SOPubDate("60", "60 days"));
		list.add(new SOPubDate("90", "90 days"));
		list.add(new SOPubDate("180", "180 days"));
		list.add(new SOPubDate("1", "1 year"));
		list.add(new SOPubDate("2", "2 year"));
		list.add(new SOPubDate("3", "3 year"));
		list.add(new SOPubDate("5", "5 year"));
		list.add(new SOPubDate("10", "10 year"));
		return list;
	}
	
	public static String toSearchString(String idStr, String suffix) {
		int id = Util.toInt(idStr);
		if ( id <= 0 ) {
			return null;
		}

		int field;
		if ( id <= 10 ) {
			field = Calendar.YEAR;
		} else {
			field = Calendar.DATE;
		}
		
		EDate fromDate = new EDate();
		fromDate.setCurrentFrom(field, id * -1);
		
		EDate toDate = new EDate();
		toDate.setToToday();
	
		String t = "";
		
		t += "(";
		t += fromDate.getLabel();
		t += suffix;
		t += ":";
		t += toDate.getLabel();
		t += suffix;
		t += ")";
		return t;
	}
}
