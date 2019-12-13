package com.bim.es;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bim.core.Util;
import com.bim.es.base.ESearchSequence;

public class ListMySearchAdapter extends BaseAdapter {
	private ActivityMySearch activity;
	protected LayoutInflater inflater;
	private List<ESearchSequence> searchList;

	public ListMySearchAdapter(ActivityMySearch activity) {
		this.activity = activity;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		if (searchList == null) {
			return 0;
		}
		return searchList.size();
	}

	public ESearchSequence getSearch(int position) {
		return searchList.get(position);
	}

	public Object getItem(int position) {
		return getSearch(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View rowView, ViewGroup parent) {
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.my_search_row, parent, false);
		}

		final ESearchSequence search = getSearch(position);

		CheckBox mCheckbox = (CheckBox) rowView
				.findViewById(R.id.my_search_row_checkbox);
		mCheckbox.setOnCheckedChangeListener(null);
		mCheckbox.setChecked(search.isChecked());
		mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				search.setChecked(isChecked);
			}
		});

		ListAdapter.set(rowView, R.id.my_search_row_db, search.getDBLabel());

		TextView mTerm = (TextView) rowView
				.findViewById(R.id.my_search_row_term);
		mTerm.setText(search.getTerm());

		ListAdapter.set(rowView, R.id.my_search_row_exclude, search
				.getExcludeLabel());
		ListAdapter.set(rowView, R.id.my_search_row_molecule, search
				.getMoleculeLabel());
		ListAdapter.set(rowView, R.id.my_search_row_gene_loc, search
				.getGeneLocationLabel());
		ListAdapter.set(rowView, R.id.my_search_row_seg_seq, search
				.getSegSeqLabel());

		String source = null;
		if (search.isNucleotide()) {
			source = search.getSeqSourceDNALabel();
		} else if (search.isProtein()) {
			source = search.getSeqSourceProteinLabel();
		}
		ListAdapter.set(rowView, R.id.my_search_row_seq_source, source);
		ListAdapter.set(rowView, R.id.my_search_row_pubdate, search
				.getPubDateLabel(), "Create: ");
		ListAdapter.set(rowView, R.id.my_search_row_moddate, search
				.getModDateLabel(), "Update: ");

		TextView mTime = (TextView) rowView
				.findViewById(R.id.my_search_row_time);
		mTime.setText(search.getTime());

		TextView mResult = (TextView) rowView
				.findViewById(R.id.my_search_row_result);
		mResult.setText(Util.getResourceString(activity,
				R.string.list_row_result)
				+ ": " + search.getResult());

		ImageButton runButton = (ImageButton) rowView
				.findViewById(R.id.my_search_row_button_run);
		runButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				activity.runSearch(search);
			}
		});

		return rowView;
	}

	public List<ESearchSequence> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<ESearchSequence> searchList) {
		if (searchList == null) {
			searchList = new ArrayList<ESearchSequence>();
		} else {
			this.searchList = searchList;
		}
	}
}
