package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOSegSequence extends SOBase {
	private static ArrayList<SOSegSequence> list;

	public SOSegSequence(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOSegSequence> l = getList();
		for (SOSegSequence t : l) {
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
		List<SOSegSequence> l = getList();
		int cnt = 0;
		for (SOSegSequence t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOSegSequence> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOSegSequence> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOSegSequence t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOSegSequence> getList() {
		if ( list != null ) {
			return list;
		}
		list = new ArrayList<SOSegSequence>();
		list.add(new SOSegSequence("", "Any"));
		list.add(new SOSegSequence("segmented_sequence[PROP]",
				"Show only master of set"));
		list.add(new SOSegSequence("segmented_part[PROP]",
				"Show only parts of set"));
		return list;
	}
}