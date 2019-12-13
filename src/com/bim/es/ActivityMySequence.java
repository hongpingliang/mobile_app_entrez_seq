package com.bim.es;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.bim.call.DeviceES;
import com.bim.core.Device;
import com.bim.core.Util;
import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.ActivityPub;

public class ActivityMySequence extends ActivityPub {
	private ListMySequenceAdapter mListAdapter;
	public static final String MY_SEQUENCE_FILE_NAME = "entrez_seq_my_sequence.txt";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.my_sequence);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		mListAdapter = new ListMySequenceAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.my_sequence_list);
		mListView.setAdapter(mListAdapter);

		refreshDisplay();

		if (mListAdapter.getCount() < 1) {
			displayError("No saved sequence");
		}
		logDevice();
	}

	private void logDevice() {
		if (isFirstCreated) {
			DeviceES.save(this, Device.ACTION_MY_ARTICLE, mListAdapter
					.getCount()
					+ "");
		}
		isFirstCreated = false;
	}

	private void refreshDisplay() {
		List<ESequenceInfo> list = getSequenceList();
		mListAdapter.setList(list);
		mListAdapter.notifyDataSetChanged();
	}

	public static void save(Activity activity, List<ESequenceInfo> list,
			int mode) {
		try {
			FileOutputStream fOut = activity.openFileOutput(
					MY_SEQUENCE_FILE_NAME, mode);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			for (ESequenceInfo a : list) {
				// Log.d(a.toJsonString());
				osw.write(a.toJsonString() + "\n");
			}
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ESequenceInfo> getSequenceList() {
		ArrayList<ESequenceInfo> list = new ArrayList<ESequenceInfo>();
		try {
			FileInputStream fIn = openFileInput(MY_SEQUENCE_FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(fIn));
			String line;
			while ((line = br.readLine()) != null) {
				if (Util.isNull(line)) {
					continue;
				}
				// Log.d(line);
				ESequenceInfo a = new ESequenceInfo();
				if (a.parse(line)) {
					list.add(a);
				}
			}
			fIn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.my_sequence, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_my_sequence_select_all:
			selectAll();
			return true;
		case R.id.menu_my_sequence_email_sequence:
			email();
			return true;
		case R.id.menu_my_sequence_delete:
			delete();
			return true;
		case R.id.menu_my_sequence_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}

		return true;
	}

	private void selectAll() {
		for (ESequenceInfo a : mListAdapter.getList()) {
			a.setChecked(true);
		}
		mListAdapter.notifyDataSetChanged();
	}

	private void email() {
		List<ESequenceInfo> list = getSelected();
		if (list.size() < 1) {
			displayError("Please check a sequence");
			return;
		}
		ActivityListSequence.email(this, list);
	}

	private List<ESequenceInfo> getSelected() {
		ArrayList<ESequenceInfo> list = new ArrayList<ESequenceInfo>();
		for (ESequenceInfo a : mListAdapter.getList()) {
			if (a.isChecked()) {
				list.add(a);
			}
		}
		return list;
	}

	private void delete() {
		ArrayList<ESequenceInfo> list = new ArrayList<ESequenceInfo>();
		ArrayList<ESequenceInfo> saveList = new ArrayList<ESequenceInfo>();
		for (ESequenceInfo a : mListAdapter.getList()) {
			if (a.isChecked()) {
				list.add(a);
			} else {
				saveList.add(a);
			}
		}
		if (list.size() < 1) {
			displayError("Please check a sequence");
			return;
		}
		save(this, saveList, MODE_PRIVATE);
		refreshDisplay();
	}

	public void startShowDetailActivity(ESequenceInfo seqInfo) {
		ActivitySequenceDetail.isFirstCreated = true;
		Intent intent = new Intent(this, ActivitySequenceDetail.class);
		intent.putExtra("seqInfo", seqInfo);
		this.startActivityForResult(intent, 1);
	}

}
