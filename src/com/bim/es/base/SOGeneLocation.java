package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOGeneLocation extends SOBase {
	private static ArrayList<SOGeneLocation> list;

	public SOGeneLocation(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOGeneLocation> l = getList();
		for (SOGeneLocation t : l) {
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
		List<SOGeneLocation> l = getList();
		int cnt = 0;
		for (SOGeneLocation t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOGeneLocation> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOGeneLocation> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOGeneLocation t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOGeneLocation> getList() {
		if ( list != null ) {
			return list;
		}
		list = new ArrayList<SOGeneLocation>();
		list.add(new SOGeneLocation("", "Any"));
		list
				.add(new SOGeneLocation("gene_in_genomic[PROP]",
						"Genomic DNA/RNA"));
		list.add(new SOGeneLocation("gene_in_mitochondrion[PROP]",
				"Mitochondrion"));
		list.add(new SOGeneLocation("gene_in_plastid_chloroplast[PROP]",
				"Chloroplast"));
		return list;
	}
}
