package com.bim.es.base;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.os.Parcel;
import android.os.Parcelable;

import com.bim.core.Util;
import com.bim.ncbi.EDatabase;
import com.bim.ncbi.ESearch;

public class ESearchSequence extends ESearch {
	private String exclude;
	private String geneLocation;
	private String modDate;
	private String molecule;
	private String pubDate;
	private String segSeq;
	private String seqSource;	
	
	public ESearchSequence() {
		super();
	}
	
	public boolean isProtein() {
		return EDatabase.Protein.equals(getDb());
	}
	public boolean isNucleotide() {
		return EDatabase.Nucleotide.equals(getDb());
	}
	
	private ESearchSequence(Parcel in) {
		setDb(in.readString());
		setTime(in.readString());
		setTerm(in.readString());
		
		exclude = in.readString();
		geneLocation = in.readString();
		modDate = in.readString();
		molecule = in.readString();
		pubDate = in.readString();
		segSeq = in.readString();
		seqSource = in.readString();		

		setResult(in.readInt());
		setSort(in.readString());
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(getDb());
		out.writeString(getTime());
		out.writeString(getTerm());

		out.writeString(exclude);
		out.writeString(geneLocation);
		out.writeString(modDate);
		out.writeString(molecule);
		out.writeString(pubDate);
		out.writeString(segSeq);
		out.writeString(seqSource);
		
		out.writeInt(getResult());
		out.writeString(getSort());
	}
	
	public String toJsonString() {
		JSONStringer json = new JSONStringer();
		try {
			json.object();
			set(json, "db", getDb());
			json.key("time").value(getTime());
			set(json, "term", getTerm());

			set(json, "exclude", exclude);
			set(json, "geneLocation", geneLocation);
			set(json, "modDate", modDate);
			set(json, "molecule", molecule);
			set(json, "pubDate", pubDate);
			set(json, "segSeq", segSeq);
			set(json, "seqSource", seqSource);
			
			set(json, "result", getResult());
			set(json, "sort", getSort());

			json.endObject();
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean parse(String jsonStr) {
		if (Util.isNull(jsonStr)) {
			return false;
		}

		try {
			JSONObject json = new JSONObject(jsonStr.trim());
			setDb(json.getString("db"));
			setTime(json.getString("time"));
			setTerm(getString(json, "term"));

			exclude = getString(json, "exclude");
			geneLocation = getString(json, "geneLocation");
			modDate = getString(json, "modDate");
			molecule = getString(json, "molecule");
			pubDate = getString(json, "pubDate");
			segSeq = getString(json, "segSeq");
			seqSource = getString(json, "seqSource");
			
			setResult(getInt(json, "result"));
			setSort(getString(json, "sort"));

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
	public String getGeneLocationLabel() {
		if (Util.isNull(geneLocation)) {
			return null;
		}
		return SOGeneLocation.findLabelById(geneLocation);
	}
	public String getModDateLabel() {
		if (Util.isNull(modDate)) {
			return null;
		}
		return SOModDate.findLabelById(modDate);
	}
	public String getMoleculeLabel() {
		if (Util.isNull(molecule)) {
			return null;
		}
		return SOMolecule.findLabelById(molecule);
	}
	public String getPubDateLabel() {
		if (Util.isNull(pubDate)) {
			return null;
		}
		return SOPubDate.findLabelById(pubDate);
	}
	public String getSegSeqLabel() {
		if (Util.isNull(segSeq)) {
			return null;
		}
		return SOSegSequence.findLabelById(segSeq);
	}
	public String getSeqSourceDNALabel() {
		if (Util.isNull(seqSource)) {
			return null;
		}
		return SOSeqSourceDNA.findLabelById(seqSource);
	}	
	public String getSeqSourceProteinLabel() {
		if (Util.isNull(seqSource)) {
			return null;
		}
		return SOSeqSourceProtein.findLabelById(seqSource);
	}	

	
	public String getExcludeLabel() {
		if ( Util.isNull(exclude)) {
			return null;
		}
		
		String t = "";
		if (isExcludeExist("gbdiv_sts") ) {
			t += "STSs";
		}
		if (isExcludeExist("srcdb_tpa_ddbj/embl/genbank") ) {
			if ( !Util.isNull(t)) {
				t += ",  ";
			}			
			t += "TPA";
		}
		if (isExcludeExist("gbdiv_htg") ) {
			if ( !Util.isNull(t)) {
				t += ",  ";
			}			
			t += "working draft";
		}
		if (isExcludeExist("gbdiv_pat") ) {
			if ( !Util.isNull(t)) {
				t += ",  ";
			}			
			t += "patents";
		}
		return t;
	}
		
	private boolean isExcludeExist(String key) {
		if (exclude.indexOf(key) > -1) {
			return true;
		} else {
			return false;
		}
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ESearchSequence createFromParcel(Parcel in) {
			return new ESearchSequence(in);
		}

		public ESearchSequence[] newArray(int size) {
			return new ESearchSequence[size];
		}
	};


	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getGeneLocation() {
		return geneLocation;
	}

	public void setGeneLocation(String geneLocation) {
		this.geneLocation = geneLocation;
	}

	public String getModDate() {
		return modDate;
	}

	public void setModDate(String modDate) {
		this.modDate = modDate;
	}

	public String getMolecule() {
		return molecule;
	}

	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSegSeq() {
		return segSeq;
	}

	public void setSegSeq(String segSeq) {
		this.segSeq = segSeq;
	}

	public String getSeqSource() {
		return seqSource;
	}

	public void setSeqSource(String seqSource) {
		this.seqSource = seqSource;
	}	
}
