package com.bim.es;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bim.ncbi.ActivityPub;

public class ActivitySort extends ActivityPub implements View.OnClickListener {
	private RadioGroup mRadioGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.sort);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		mRadioGroup = (RadioGroup) findViewById(R.id.sort_radio_group);

		Intent intent = getIntent();
		if (intent != null) {
			int mapMode = intent.getIntExtra("sortBy", 0);
			if (mapMode > 0) {
				mRadioGroup.check(mapMode);
			}
		}
		RadioButton mRadio = (RadioButton) findViewById(R.id.sort_by_title);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_gi);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_length);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_locus);
		mRadio.setOnClickListener(this);
		
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Intent intent = new Intent();
		intent.putExtra("sortBy", checkedId);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra("sortBy", mRadioGroup.getCheckedRadioButtonId());
		setResult(RESULT_OK, intent);
		finish();
	}
}