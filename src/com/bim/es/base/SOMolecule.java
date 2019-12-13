package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOMolecule extends SOBase {
	private static ArrayList<SOMolecule> list;

	public SOMolecule(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOMolecule> l = getList();
		for (SOMolecule t : l) {
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
		List<SOMolecule> l = getList();
		int cnt = 0;
		for (SOMolecule t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOMolecule> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOMolecule> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOMolecule t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOMolecule> getList() {
		if ( list != null ) {
			return list;
		}
		list = new ArrayList<SOMolecule>();
		list.add(new SOMolecule("", "Any"));
		list.add(new SOMolecule("biomol_genomic[PROP]", "Genomic DNA/RNA"));
		list.add(new SOMolecule("biomol_mRNA[PROP]", "mRNA"));
		list.add(new SOMolecule("biomol_rRNA[PROP]", "rRNA"));
		list.add(new SOMolecule("biomol_cRNA[PROP]", "cRNA"));
		return list;
	}
}
