package com.bim.es;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bim.call.DeviceES;
import com.bim.core.Device;
import com.bim.core.PubSetting;
import com.bim.core.Util;
import com.bim.es.base.ESearchSequence;
import com.bim.es.base.SOGeneLocation;
import com.bim.es.base.SOModDate;
import com.bim.es.base.SOMolecule;
import com.bim.es.base.SOPubDate;
import com.bim.es.base.SOSegSequence;
import com.bim.es.base.SOSeqSourceDNA;
import com.bim.es.base.SOSeqSourceProtein;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSearch;
import com.bim.ncbi.EDatabase;
import com.bim.ncbi.ERequest;
import com.bim.ncbi.EResponseSearch;
import com.bim.ncbi.ESearch;

public class ActivityMain extends ActivityPub implements
		RadioGroup.OnCheckedChangeListener {
	public static final int ACTIVITY_MY_SEARCH = 1;

	private EditText mSearchTerm;

	private String geneLocation;
	private String modDate;
	private String molecule;
	private String pubDate;
	private String segSeq;
	private String seqSource;

	private int selectedDbId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ERequest.NCBI_TOOL = "EntrezSequenceMobile";
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.main);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		init();

		PubSetting setting = getSetting(this);

		boolean isFirst = setting.isFirstTime();
		if (isFirst) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Intent intent = new Intent(ActivityMain.this,
							ActivityWelcome.class);
					ActivityMain.this.startActivityForResult(intent, 0);
				}
			}, 3000);
			setting.setFirstTime(false);
		}

		closeDialog();

		if (isFirstCreated) {
			DeviceES.save(this, Device.ACTION_START, isFirst + "");
		}
		isFirstCreated = false;
		
	}

	private void init() {

		mSearchTerm = (EditText) findViewById(R.id.main_search_term_text);

		Button searchButton = (Button) findViewById(R.id.main_search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doSearch();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.main_clear_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				clear();
			}
		});

		initSpinner();

		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.main_db_radiog);
		mRadioGroup.setOnCheckedChangeListener(this);
		mRadioGroup.check(R.id.main_db_dna);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (selectedDbId == checkedId) {
			return;
		}
		selectedDbId = checkedId;

		LinearLayout mExcludeSTS = (LinearLayout) findViewById(R.id.main_exclude_sts_layout);
		LinearLayout mMolecule = (LinearLayout) findViewById(R.id.main_molecule_layout);
		Spinner mDNA = (Spinner) findViewById(R.id.main_seq_source_dna_spinner);
		Spinner mProtein = (Spinner) findViewById(R.id.main_seq_source_protein_spinner);
		if (isDbProtein()) {
			mExcludeSTS.setVisibility(View.GONE);
			mMolecule.setVisibility(View.GONE);
			mDNA.setVisibility(View.GONE);
			mProtein.setVisibility(View.VISIBLE);
		} else if (isDbDNA()) {
			mExcludeSTS.setVisibility(View.VISIBLE);
			mMolecule.setVisibility(View.VISIBLE);
			mDNA.setVisibility(View.VISIBLE);
			mProtein.setVisibility(View.GONE);
		}
	}

	private boolean isDbDNA() {
		return selectedDbId == R.id.main_db_dna;
	}

	private boolean isDbProtein() {
		return selectedDbId == R.id.main_db_protein;
	}

	private String getDB() {
		switch (selectedDbId) {
		case R.id.main_db_dna:
			return EDatabase.Nucleotide;
		case R.id.main_db_protein:
			return EDatabase.Protein;
		}
		return null;
	}

	private void doSearch() {
		String op = "AND";
		String field = null;

		field = ESearch.join(field, getExclude(), op);
		field = ESearch.join(field, geneLocation, op);

		if (isDbDNA()) {
			field = ESearch.join(field, molecule, op);
		}
		field = ESearch.join(field, segSeq, op);
		field = ESearch.join(field, seqSource, op);

		if (!Util.isNull(pubDate)) {
			field = ESearch.join(field, SOPubDate.toSearchString(pubDate,
					"[PDAT]"), op);
		}
		if (!Util.isNull(modDate)) {
			field = ESearch.join(field, SOPubDate.toSearchString(modDate,
					"[MDAT]"), op);
		}

		String term = mSearchTerm.getText().toString();
		if (!Util.isNull(term)) {
			term = ESearch.value("All Fields", term);
		}
		if (Util.isNull(term) && Util.isNull(field)) {
			displayError("Please enter a term or tap a field");
			return;
		}

		String search = ESearch.join(term, field, op);
		search = search.replaceAll(" ", "+");

		showLoadingDialog();
		ECallSearch eSearch = new ECallSearch(this);
		eSearch.getRequest().setDb(getDB());
		eSearch.getRequest().setTerm(search);
		eSearch.getRequest().setRetmax(10);

		httpThread = new Thread(eSearch);
		httpThread.start();
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_main_my_search:
			startMySearchActivity();
			return true;
		case R.id.menu_main_my_sequence:
			startMySequenceActivity();
			return true;
		}

		return true;
	}

	private void startMySearchActivity() {
		ActivityMySearch.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityMySearch.class);
		this.startActivityForResult(intent, ACTIVITY_MY_SEARCH);
	}

	private void startMySequenceActivity() {
		ActivityMySequence.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityMySequence.class);
		this.startActivityForResult(intent, 2);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null) {
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		if (requestCode == ACTIVITY_MY_SEARCH) {
			onMySearchReturn(intent);
		}
	}

	private void onMySearchReturn(Intent intent) {

		ESearchSequence search = intent.getParcelableExtra("search");
		if (search == null) {
			return;
		}

		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.main_db_radiog);
		if (search.isNucleotide()) {
			mRadioGroup.check(R.id.main_db_dna);
		} else if (search.isProtein()) {
			mRadioGroup.check(R.id.main_db_protein);
		}
		mSearchTerm.setText(search.getTerm());
		updateExclude(search.getExclude());

		molecule = search.getMolecule();
		resetSpinner(R.id.main_molecule_spinner, SOMolecule
				.findPositionById(molecule));
		geneLocation = search.getGeneLocation();
		resetSpinner(R.id.main_gene_loc_spinner, SOGeneLocation
				.findPositionById(geneLocation));
		segSeq = search.getSegSeq();
		resetSpinner(R.id.main_seg_seq_spinner, SOSegSequence
				.findPositionById(segSeq));
		seqSource = search.getSeqSource();
		resetSpinner(R.id.main_seq_source_dna_spinner, SOSeqSourceDNA
				.findPositionById(seqSource));
		resetSpinner(R.id.main_seq_source_protein_spinner, SOSeqSourceProtein
				.findPositionById(seqSource));
		pubDate = search.getPubDate();
		resetSpinner(R.id.main_pub_date_spinner, SOPubDate
				.findPositionById(pubDate));
		modDate = search.getModDate();
		resetSpinner(R.id.main_mod_date_spinner, SOModDate
				.findPositionById(modDate));

		doSearch();
	}

	private void resetSpinner(int resId, int selectedIn) {
		int selected;
		if (selectedIn < 0) {
			selected = 0;
		} else {
			selected = selectedIn;
		}
		Util.selectSpinner(this, resId, selected);
	}

	public void onESearchOkay(EResponseSearch response, ECallSearch eSearch) {
		if (response == null) {
			return;
		}
		if (response.getIdList().size() < 1) {
			displayError("No result found");
			return;
		}

		if (response.getQueryKey() <= 0 || Util.isNull(response.getWebEnv())) {
			displayError("Failed to get query key and webenv");
			return;
		}

		ESearchSequence search = new ESearchSequence();
		search.setDb(eSearch.getRequest().getDb());
		search.setTerm(mSearchTerm.getText().toString());
		search.setExclude(getExclude());
		search.setGeneLocation(geneLocation);
		search.setModDate(modDate);
		search.setMolecule(molecule);
		search.setPubDate(pubDate);
		search.setSegSeq(segSeq);
		search.setSeqSource(seqSource);

		search.setSort(eSearch.getRequest().getSort());
		search.setResult(response.getCount());

		ActivityListSequence.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityListSequence.class);
		intent.putExtra("search", search);
		intent.putExtra("response", response);
		this.startActivityForResult(intent, 1);
	}

	private String getExclude() {
		boolean isSTS = getCheckBoxValue(R.id.main_exclude_sts_checkbox);
		boolean isTPA = getCheckBoxValue(R.id.main_exclude_tpa_checkbox);
		boolean isWD = getCheckBoxValue(R.id.main_exclude_wd_checkbox);
		boolean isPatent = getCheckBoxValue(R.id.main_exclude_patent_checkbox);

		if (!(isSTS || isTPA || isWD || isPatent)) {
			return null;
		}
		String t = "(all[filter]";
		if (isTPA) {
			t += " NOT srcdb_tpa_ddbj/embl/genbank[PROP]";
		}
		if (isPatent) {
			t += " NOT gbdiv_pat[PROP]";
		}

		if (isDbDNA()) {
			if (isSTS) {
				t += " NOT gbdiv_sts[PROP]";
			}
			if (isWD) {
				t += " NOT gbdiv_htg[PROP]";
			}
		}

		t += ")";
		return t;
	}

	private void updateExclude(String str) {
		updateExclude(str, "gbdiv_sts", R.id.main_exclude_sts_checkbox);
		updateExclude(str, "srcdb_tpa_ddbj/embl/genbank",
				R.id.main_exclude_tpa_checkbox);
		updateExclude(str, "gbdiv_htg", R.id.main_exclude_wd_checkbox);
		updateExclude(str, "gbdiv_pat", R.id.main_exclude_patent_checkbox);
	}

	private void updateExclude(String str, String key, int resId) {
		boolean val;
		if (str != null && str.indexOf(key) > -1) {
			val = true;
		} else {
			val = false;
		}
		CheckBox mCheck = (CheckBox) findViewById(resId);
		mCheck.setChecked(val);
	}

	private void clear() {
		clear(R.id.main_search_term_text);

		clearCheckbox(R.id.main_exclude_sts_checkbox);
		clearCheckbox(R.id.main_exclude_wd_checkbox);
		clearCheckbox(R.id.main_exclude_tpa_checkbox);
		clearCheckbox(R.id.main_exclude_patent_checkbox);

		clearSpinner(R.id.main_molecule_spinner);
		clearSpinner(R.id.main_gene_loc_spinner);
		clearSpinner(R.id.main_seg_seq_spinner);
		clearSpinner(R.id.main_seq_source_dna_spinner);
		clearSpinner(R.id.main_pub_date_spinner);
		clearSpinner(R.id.main_mod_date_spinner);
	}

	private void initSpinner() {

		initSpinnerOne(R.id.main_molecule_spinner, SOMolecule.getLabels());
		initSpinnerOne(R.id.main_gene_loc_spinner, SOGeneLocation.getLabels());
		initSpinnerOne(R.id.main_seg_seq_spinner, SOSegSequence.getLabels());
		initSpinnerOne(R.id.main_seq_source_dna_spinner, SOSeqSourceDNA
				.getLabels());
		initSpinnerOne(R.id.main_seq_source_protein_spinner, SOSeqSourceProtein
				.getLabels());
		initSpinnerOne(R.id.main_pub_date_spinner, SOPubDate.getLabels());
		initSpinnerOne(R.id.main_mod_date_spinner, SOModDate.getLabels());
	}

	private void initSpinnerOne(final int resId, String[] labels) {

		ArrayAdapter adapter;
		int aSpinItem = android.R.layout.simple_spinner_item;
		int androidDropItem = android.R.layout.simple_spinner_dropdown_item;

		Spinner mSteChirSpinner = (Spinner) findViewById(resId);
		adapter = new ArrayAdapter(this, aSpinItem, labels);
		adapter.setDropDownViewResource(androidDropItem);
		mSteChirSpinner.setAdapter(adapter);
		mSteChirSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {

				switch (resId) {
				case R.id.main_molecule_spinner:
					molecule = SOMolecule.findId(position);
					break;
				case R.id.main_gene_loc_spinner:
					geneLocation = SOGeneLocation.findId(position);					
					break;
				case R.id.main_seg_seq_spinner:
					segSeq = SOSegSequence.findId(position);
					break;
				case R.id.main_seq_source_dna_spinner:
					seqSource = SOSeqSourceDNA.findId(position);
					break;
				case R.id.main_seq_source_protein_spinner:
					seqSource = SOSeqSourceProtein.findId(position);
					break;
				case R.id.main_pub_date_spinner:
					pubDate = SOPubDate.findId(position);
					break;
				case R.id.main_mod_date_spinner:
					modDate = SOModDate.findId(position);
					break;
				}
			}

			public void onNothingSelected(AdapterView arg0) {
			}
		});
	}

	private static PubSetting _appSetting;

	public static PubSetting getSetting(Activity activity) {
		if (_appSetting != null) {
			return _appSetting;
		}
		_appSetting = new PubSetting();
		_appSetting.load(activity);
		if (_appSetting.isFirstTime()) {
			_appSetting.save(activity);
		}

		return _appSetting;
	}
}