package com.bim.es.base;

import java.util.Comparator;

import com.bim.core.Util;
import com.bim.es.R;
public class SequenceComparator implements Comparator {

	private int byResId;
	private boolean isDescOrder;

	public SequenceComparator(int byResId, boolean isDescOrder) {
		this.byResId = byResId;
		this.isDescOrder = isDescOrder;
	}

	public int compare(Object object1, Object object2) {
		ESequenceInfo a = (ESequenceInfo) object1;
		ESequenceInfo b = (ESequenceInfo) object2;

		String aStr = null;
		String bStr = null;
		switch (byResId) {
		case R.id.sort_by_title:
			aStr = a.getTitle();
			bStr = b.getTitle();
			break;
		case R.id.sort_by_gi:
			if (isDescOrder) {
				return b.getGi() - a.getGi();
			} else {
				return a.getGi() - b.getGi();
			}
		case R.id.sort_by_locus:
			aStr = a.getCaption();
			bStr = b.getCaption();
			break;
		case R.id.sort_by_length:
			if (isDescOrder) {
				return b.getLength() - a.getLength();
			} else {
				return a.getLength() - b.getLength();
			}	
		default:
			break;
		}

		if (isDescOrder) {
			String t = aStr;
			aStr = bStr;
			bStr = t;
		}

		return byName(aStr, bStr);
	}

	private int byName(String a, String b) {
		if (Util.isNull(a) && Util.isNull(b)) {
			return 0;
		} else if (Util.isNull(a)) {
			return -1;
		} else if (Util.isNull(b)) {
			return 1;
		} else {
			return a.compareToIgnoreCase(b);
		}
	}
}
