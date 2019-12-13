package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOSeqSourceDNA extends SOBase {
	private static ArrayList<SOSeqSourceDNA> list;

	public SOSeqSourceDNA(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOSeqSourceDNA> l = getList();
		for (SOSeqSourceDNA t : l) {
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
		List<SOSeqSourceDNA> l = getList();
		int cnt = 0;
		for (SOSeqSourceDNA t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOSeqSourceDNA> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOSeqSourceDNA> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOSeqSourceDNA t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOSeqSourceDNA> getList() {
		if ( list != null ) {
			return list;
		}
		
		list = new ArrayList<SOSeqSourceDNA>();
		list.add(new SOSeqSourceDNA("", "Any"));
		list.add(new SOSeqSourceDNA("srcdb_refseq[PROP]", "RefSeq"));
		list.add(new SOSeqSourceDNA("srcdb_genbank[PROP]", "GenBank"));
		list.add(new SOSeqSourceDNA("srcdb_embl[PROP]", "EMBL"));
		list.add(new SOSeqSourceDNA("srcdb_ddbj[PROP]", "DDBJ"));
		list.add(new SOSeqSourceDNA("srcdb_pdb[PROP]", "PDB"));
		return list;
	}
}