package com.bim.es;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.bim.call.DeviceES;
import com.bim.call.ECallSummaryES;
import com.bim.call.EResponseSummaryES;
import com.bim.core.Device;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.es.base.ESearchSequence;
import com.bim.es.base.ESequenceInfo;
import com.bim.es.base.SequenceComparator;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSummary;
import com.bim.ncbi.ERequest;
import com.bim.ncbi.EResponse;
import com.bim.ncbi.EResponseSearch;

public class ActivityListSequence extends ActivityPub {
	public final static int ACTIVITY_SORT = 2;
	private ListSequenceAdapter mListAdapter;
	private ERequest request;
	private ESearchSequence search;
	private EResponseSearch searchResponse;
	private int sortResId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.list_sequence);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		Intent intent = getIntent();
		if (intent == null) {
			Log.d("Error in ActivityListArticle:onCreate");
			return;
		}

		searchResponse = intent.getParcelableExtra("response");
		search = intent.getParcelableExtra("search");

		/*
		 * searchResponse = new EResponseSearch(); searchResponse.setCount(10);
		 * search = new ESearchSequence(); search.setDb(EDatabase.Nucleotide);
		 */

		if (searchResponse == null || searchResponse.getCount() < 1) {
			displayError("no search searchRequest/response");
			return;
		}

		ECallSummaryES eSummary = new ECallSummaryES(this);
		request = eSummary.getRequest();
		request.setDb(search.getDb());
		request.setRetstart(1);
		request.setRetmax(10);
		request.setQuery_key(searchResponse.getQueryKey());
		request.setWebEnv(searchResponse.getWebEnv());

		mListAdapter = new ListSequenceAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.list_seq_list);
		mListView.setAdapter(mListAdapter);

		setTitle(Util.getResourceString(this, R.string.list_sequence_result)
				+ ": " + getAvailabeCount());

		closeDialog();

		logDevice();

		chekAndLoadMore();
	}

	private void logDevice() {
		if (isFirstCreated) {
			if (search != null) {
				DeviceES.save(this, Device.ACTION_SEARCH, search
						.toJsonString());
			}
		}
		isFirstCreated = false;
	}

	public void onESummaryOkay(EResponse response, ECallSummary eSummary) {
		EResponseSummaryES responseChem = (EResponseSummaryES) response;

		List<ESequenceInfo> list = responseChem.getSeqInfoList();
		if (list != null && list.size() > 0) {
			// Log.d("load: " + list.size());
			mListAdapter.addList(list);
			mListAdapter.notifyDataSetChanged();
			int newStart = request.getRetstart() + request.getRetmax();
			request.setRetstart(newStart);
		}
	}

	public int getAvailabeCount() {
		return searchResponse.getCount();
	}

	public void startSortActivity() {
		Intent intent = new Intent(this, ActivitySort.class);
		intent.putExtra("sortBy", sortResId);
		this.startActivityForResult(intent, ACTIVITY_SORT);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null) {
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		if (requestCode == ACTIVITY_SORT) {
			sort(intent);
		}
	}

	private void sort(Intent intent) {
		sortResId = intent.getIntExtra("sortBy", 0);
		if (sortResId <= 0) {
			return;
		}

		if (mListAdapter.getList().size() < 2) {
			return;
		}

		SequenceComparator comparator = new SequenceComparator(sortResId, false);
		Collections.sort(mListAdapter.getList(), comparator);
		mListAdapter.notifyDataSetChanged();

	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.list_sequence, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_list_sequence_save_sequence:
			save();
			return true;
		case R.id.menu_list_sequence_email_sequence:
			email();
			return true;
		case R.id.menu_list_sequence_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		case R.id.menu_list_sequence_save_search:
			saveSearch();
			return true;
		case R.id.menu_list_sequence_sort:
			startSortActivity();
			return true;
		}

		return true;
	}

	private void saveSearch() {
		if (search == null) {
			return;
		}
		ArrayList<ESearchSequence> list = new ArrayList<ESearchSequence>();
		list.add(search);
		ActivityMySearch.saveSearch(this, list, MODE_APPEND);
	}

	private List<ESequenceInfo> getCheckedsequenceList() {
		List<ESequenceInfo> list = new ArrayList<ESequenceInfo>();
		for (ESequenceInfo a : mListAdapter.getList()) {
			if (a.isChecked()) {
				list.add(a);
			}
		}
		return list;
	}

	private void email() {
		List<ESequenceInfo> list = getCheckedsequenceList();
		if (list.size() < 1) {
			displayError("Please check a sequence");
			return;
		}
		email(this, list);
	}

	public static void email(Activity activity, List<ESequenceInfo> list) {

		String subject = "Entrez Sequence: ";
		String content = "";
		int cnt = 1;
		for (ESequenceInfo a : list) {
			content += cnt + ". " + a.getCaption() + "\n";
			content += "  " + a.getTitle() + "\n";
			content += "  " + ActivitySequenceDetail.getUrl(a);
			content += "\n\n";
			cnt++;
		}

		Util.doEmail(activity, subject, content);
	}

	public void chekAndLoadMore() {
		if (mListAdapter.getList().size() >= getAvailabeCount()) {
			return;
		}

		if (httpThread != null && httpThread.isAlive()) {
			return;
		}

		showLoadingDialog();
		ECallSummaryES eSummary = new ECallSummaryES(this);
		eSummary.setRequest(request);
		httpThread = new Thread(eSummary);
		httpThread.start();
	}

	private void save() {
		List<ESequenceInfo> list = getCheckedsequenceList();
		if (list.size() < 1) {
			displayError("Please check a sequence");
			return;
		}

		ActivityMySequence.save(this, list, MODE_APPEND);

		showMessage("sequence saved");
	}

	public void startShowDetailActivity(ESequenceInfo seqInfo) {

		ActivitySequenceDetail.isFirstCreated = true;
		Intent intent = new Intent(this, ActivitySequenceDetail.class);
		intent.putExtra("seqInfo", seqInfo);
		this.startActivityForResult(intent, 1);
	}

}
