package com.bim.call;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.EResponse;

public class EResponseSummaryES extends EResponse {

	private ArrayList<ESequenceInfo> seqInfoList;

	public EResponseSummaryES() {
		seqInfoList = new ArrayList<ESequenceInfo>();
	}

	private EResponseSummaryES(Parcel in) {
	}

	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public EResponseSummaryES createFromParcel(Parcel in) {
			return new EResponseSummaryES(in);
		}

		public EResponseSummaryES[] newArray(int size) {
			return new EResponseSummaryES[size];
		}
	};

	public ArrayList<ESequenceInfo> getSeqInfoList() {
		return seqInfoList;
	}

}
