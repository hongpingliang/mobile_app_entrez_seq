package com.bim.es;

import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bim.core.Util;
import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.ActivityPub;

public abstract class ListAdapter extends BaseAdapter {

	protected void startDetailActivity(final ActivityPub activityBase,
			ESequenceInfo seqInfo) {
		if (activityBase instanceof ActivityListSequence) {
			ActivityListSequence activity = (ActivityListSequence) activityBase;
			activity.startShowDetailActivity(seqInfo);
			return;
		}
		if (activityBase instanceof ActivityMySequence) {
			ActivityMySequence activity = (ActivityMySequence) activityBase;
			activity.startShowDetailActivity(seqInfo);
			return;
		}
	}

	protected void updateView(final ActivityPub activityBase, View rowView,
			final ESequenceInfo seqInfo, int position) {
		CheckBox mCheckbox = (CheckBox) rowView
				.findViewById(R.id.list_row_checkbox);
		mCheckbox.setOnCheckedChangeListener(null);
		mCheckbox.setChecked(seqInfo.isChecked());
		mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				seqInfo.setChecked(isChecked);
			}
		});

		TextView mTitle = (TextView) rowView.findViewById(R.id.list_row_title);
		mTitle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startDetailActivity(activityBase, seqInfo);
			}
		});
		String num = (position + 1) + ".  ";
		String title = num  + seqInfo.getTitle();
		mTitle.setText(title, TextView.BufferType.SPANNABLE);

		Spannable str = (Spannable) mTitle.getText();
		// new StyleSpan(android.graphics.Typeface.ITALIC);
		str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, num.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new RelativeSizeSpan(0.75f), 0, num.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new UnderlineSpan(), num.length(), title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		String len = Util.formatInt(seqInfo.getLength());
		if ( seqInfo.isNucleotide() ) {
			len += " bp " + seqInfo.getFlagLabel() + " DNA";
		} else if ( seqInfo.isProtein() ) {
			len += " aa protein";
		}
		setOnly(rowView, R.id.list_row_seq_length_info, len);
		setOnly(rowView, R.id.list_row_seq_caption, seqInfo.getCaption());
		setOnly(rowView, R.id.list_row_seq_gi, seqInfo.getGi() + "", "GI: ");

		String date = "Create " + seqInfo.getCreateDate() + "    Update "
				+ seqInfo.getUpdateDate();
		setOnly(rowView, R.id.list_row_seq_created_date, date);
	}

	public static void set(View view, int resId, String value) {
		set(view, resId, value, "");
	}

	public static void set(View view, int resId, String value, String name) {
		TextView mText = (TextView) view.findViewById(resId);
		if (Util.isNull(value)) {
			mText.setVisibility(View.GONE);
		} else {
			mText.setText(name + value);
			mText.setVisibility(View.VISIBLE);
		}
	}

	public static void setOnly(View view, int resId, String value) {
		setOnly(view, resId, value, "");
	}

	public static void setOnly(View view, int resId, String value, String name) {
		TextView mText = (TextView) view.findViewById(resId);
		mText.setText(name + value);
	}
}
