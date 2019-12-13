package com.bim.es.base;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

public class SOSeqSourceProtein extends SOBase {
	private static ArrayList<SOSeqSourceProtein> list;

	public SOSeqSourceProtein(String id, String name) {
		super(id, name);
		
		
	}
	public static String findLabelById(String id) {
		if (Util.isNull(id)) {
			return null;
		}
		List<SOSeqSourceProtein> l = getList();
		for (SOSeqSourceProtein t : l) {
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
		List<SOSeqSourceProtein> l = getList();
		int cnt = 0;
		for (SOSeqSourceProtein t : l) {
			if (t.getId().equals(id)) {
				return cnt;
			}
			cnt++;
		}
		return -1;
	}

	public static String findId(int position) {
		List<SOSeqSourceProtein> list = getList();
		if (position < 0 || position >= list.size()) {
			return null;
		}
		return list.get(position).getId();
	}

	public static String[] getLabels() {
		List<SOSeqSourceProtein> l = getList();
		String[] array = new String[l.size()];
		int cnt = 0;
		for (SOSeqSourceProtein t : l) {
			array[cnt] = t.getLabel();
			cnt++;
		}

		return array;
	}

	private static ArrayList<SOSeqSourceProtein> getList() {
		if ( list != null ) {
			return list;
		}
		
		list = new ArrayList<SOSeqSourceProtein>();
		list.add(new SOSeqSourceProtein("", "Any"));
		list.add(new SOSeqSourceProtein("srcdb_refseq[PROP]", "RefSeq"));
		list.add(new SOSeqSourceProtein("srcdb_genbank[PROP]", "GenBank"));
		list.add(new SOSeqSourceProtein("srcdb_embl[PROP]", "EMBL"));
		list.add(new SOSeqSourceProtein("srcdb_ddbj[PROP]", "DDBJ"));
		list.add(new SOSeqSourceProtein("srcdb_pdb[PROP]", "PDB"));
		list.add(new SOSeqSourceProtein("srcdb_swiss-prot[PROP]", "SWISS-PROT"));
		list.add(new SOSeqSourceProtein("srcdb_pir[PROP]", "PIR"));
		list.add(new SOSeqSourceProtein("srcdb_prf[PROP]", "PRF"));
		return list;
	}
}