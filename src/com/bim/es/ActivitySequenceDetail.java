package com.bim.es;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.bim.call.DeviceES;
import com.bim.call.ECallFetchES;
import com.bim.call.EResponseFetchES;
import com.bim.core.Device;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallFetch;
import com.bim.ncbi.ERequest;
import com.bim.ncbi.EResponseFetch;

public class ActivitySequenceDetail extends ActivityPub {
	
	private int MAX = 15000;
	public static final String DATA_FILE_NAME = "entrez_seq_sequence_detail.txt";
	
	public static final String URL_DNA = "https://www.ncbi.nlm.nih.gov/nuccore/";
	public static final String URL_Protein = "https://www.ncbi.nlm.nih.gov/protein/";

	private ERequest request;
	private ESequenceInfo seqInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.sequence_detail);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		Intent intent = getIntent();
		if (intent == null) {
			Log.d("Error in ActivitySequenceDetail:onCreate");
			return;
		}
		seqInfo = intent.getParcelableExtra("seqInfo");

		/*
		seqInfo = new ESequenceInfo();
		seqInfo.setId(194132090);
		seqInfo.setCaption("EU849490");
		seqInfo.setDb(EDatabase.Nucleotide);
*/
		
		if (seqInfo == null) {
			displayError("No sequence");
			return;
		}

		setTitle(seqInfo.getCaption() + "   GI: " + seqInfo.getGi());

		ECallFetchES eFetch = new ECallFetchES(this, seqInfo);
		request = eFetch.getRequest();
		request.setRetmode("html");
		request.setRettype("gb");
		request.setDb(seqInfo.getDb());
		
		
		if ( seqInfo.getLength() > MAX ) {
			request.setSeq_stop(MAX);
		}
		

		showWebView(eFetch);

		if (isFirstCreated) {
			DeviceES.save(this, Device.ACTION_SHOW_ABSTRACT, seqInfo
					.getCaption()
					+ "");
		}
		isFirstCreated = false;
	}

	
	private void showWebView(ECallFetchES eFetch) {
		
		WebView mWebView = (WebView) findViewById(R.id.sequence_detail_webview);
		mWebView.setVisibility(View.VISIBLE);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		FrameLayout mContentView = (FrameLayout) getWindow().getDecorView()
				.findViewById(android.R.id.content);
		final View zoom = mWebView.getZoomControls();
		mContentView.addView(zoom, ZOOM_PARAMS);
		zoom.setVisibility(View.GONE);

		mWebView.setWebViewClient(new WebViewClient() {

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setProgressBarIndeterminateVisibility(true);
			}

			public void onPageFinished(WebView view, String url) {
				setProgressBarIndeterminateVisibility(false);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				setProgressBarIndeterminateVisibility(false);
			}
		});
	
		httpThread = new Thread(eFetch);
		httpThread.start();
	}

	
	public void onEFetchOkay(EResponseFetch responseIn, ECallFetch eFetch) {
		EResponseFetchES response = (EResponseFetchES) responseIn;
		String content = "";
		
		content += "<html>";
		content += "<head>";
		content += "<base href=\"https://www.ncbi.nlm.nih.gov/\" />";
		content += "</head>";
		content += "<body style=\"color:#333333;padding-top:10px;\">";
		content += response.getContent();
		
		if ( seqInfo.getLength() > MAX ) {
			content += "<br/>Note: not full sequence is displayed.<br/><br/>";
		}
		content += "</body></html>";

		WebView mWebView = (WebView) findViewById(R.id.sequence_detail_webview);		
		mWebView.loadData(content,  "text/html", "utf-8");		
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.sequence_detail, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_sequence_detail_save_sequence:
			save();
			return true;
		case R.id.menu_sequence_detail_email_sequence:
			email();
			return true;
		case R.id.menu_sequence_detail_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}

		return true;
	}

	private void email() {
		String subject = "Entrez Sequence: " + seqInfo.getCaption();
		String content = "\n";
		content += "  " + seqInfo.getCaption() + "\n";
		content += "  " + seqInfo.getTitle() + "\n\n";
		content += "  " + getUrl(seqInfo);
		content += "\n\n";
		Util.doEmail(this, subject, content);
	}
	
	public static String getUrl(ESequenceInfo seqInfo) {
		if ( seqInfo.isNucleotide() ) {
			return URL_DNA + seqInfo.getId();
		} else if ( seqInfo.isProtein() ) {
			return URL_Protein + seqInfo.getId();
		}
		return null;
	}

	public void save() {
		ArrayList<ESequenceInfo> list = new ArrayList<ESequenceInfo>();
		list.add(seqInfo);
		ActivityMySequence.save(this, list, MODE_APPEND);
		showMessage("Sequence saved");
	}

	private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
}