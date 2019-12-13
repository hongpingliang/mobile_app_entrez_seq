package com.bim.es;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bim.es.base.ESequenceInfo;

public class ListMySequenceAdapter extends ListAdapter {
	private ActivityMySequence activity;
	protected LayoutInflater inflater;
	private List<ESequenceInfo> list;

	public ListMySequenceAdapter(ActivityMySequence activity) {
		this.activity = activity;
		this.list = new ArrayList<ESequenceInfo>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		if ( list == null ) {
			return 0;
		}
		return list.size();
	}

	public ESequenceInfo getSequence(int position) {
		if (position < list.size()) {
			return list.get(position);
		} else {
			return null;
		}
	}

	public Object getItem(int position) {
		return getSequence(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_sequence_row, parent,
					false);
		}

		final ESequenceInfo seqInfo = getSequence(position);

		updateView(activity, convertView, seqInfo, position);
		return convertView;
	}

	public List<ESequenceInfo> getList() {
		return list;
	}

	public void setList(List<ESequenceInfo> list) {
		if (list == null) {
			this.list = new ArrayList<ESequenceInfo>();
		} else {
			this.list = list;
		}
	}

}
