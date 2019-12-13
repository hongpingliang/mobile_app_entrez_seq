package com.bim.es;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bim.es.base.ESequenceInfo;

public class ListSequenceAdapter extends ListAdapter {
	final private ActivityListSequence activity;
	protected LayoutInflater inflater;
	private List<ESequenceInfo> list;

	public ListSequenceAdapter(ActivityListSequence activity) {
		this.activity = activity;
		this.list = new ArrayList<ESequenceInfo>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addList(List<ESequenceInfo> l) {
		if (l != null) {
			list.addAll(l);
		}
	}

	public int getCount() {
		if (list.size() >= activity.getAvailabeCount()) {
			return list.size();
		} else {
			return list.size() + 1;
		}
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

		View loadingView = convertView.findViewById(R.id.list_row_loading_view);
		View dataView = convertView.findViewById(R.id.list_row_data_view);
		final ESequenceInfo seqInfo = getSequence(position);
		if (seqInfo == null) {
			loadingView.setVisibility(View.VISIBLE);
			dataView.setVisibility(View.GONE);
			activity.chekAndLoadMore();
			return convertView;
		}

		loadingView.setVisibility(View.GONE);
		dataView.setVisibility(View.VISIBLE);

		updateView(activity, convertView, seqInfo, position);
		return convertView;
	}

	public List<ESequenceInfo> getList() {
		return list;
	}

	public void setList(List<ESequenceInfo> list) {
		this.list = list;
	}
}
