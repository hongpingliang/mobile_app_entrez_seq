package com.bim.es.base;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.os.Parcel;
import android.os.Parcelable;

import com.bim.core.EParceble;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.ncbi.EDatabase;

public class ESequenceInfo extends EParceble {
	private String db;
	private int id;
	private String caption;
	private String title;
	private int gi;
	private String createDate;
	private String updateDate;
	private int flag;
	private int taxId;
	private int length;
	private String status;
	private String comment;
	
	private boolean checked;

	public ESequenceInfo() {
	}

	public String getFlagLabel() {
		if ( flag == 256 ) {
			return "circular";
		} else {
			return "linear";
		}
		
	}
	public boolean isProtein() {
		return EDatabase.Protein.equals(getDb());
	}
	public boolean isNucleotide() {
		return EDatabase.Nucleotide.equals(getDb());
	}
	
	public String toJsonString() {
		JSONStringer json = new JSONStringer();
		try {
			json.object();
			json.key("db").value(getDb());
			json.key("id").value(getId());
			set(json, "caption", getCaption());
			set(json, "title", getTitle());		
			set(json, "gi", getGi());
			set(json, "createDate", getCreateDate());
			set(json, "updateDate", getUpdateDate());
			set(json, "flag", getFlag());
			set(json, "taxId", getTaxId());
			set(json, "length", getLength());	
			set(json, "status", getStatus());
			set(json, "comment", getComment());
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
			JSONObject json = new JSONObject(jsonStr);
			db = json.getString("db");
			id = json.getInt("id");
			caption = getString(json, "caption");
			title = getString(json, "title");
			gi = getInt(json, "gi");
			createDate = getString(json, "createDate");
			updateDate = getString(json, "updateDate");
			flag = getInt(json, "flag");
			taxId = getInt(json, "taxId");
			length = getInt(json, "length");
			status = getString(json, "status");
			comment = getString(json, "comment");

		} catch (JSONException e) {
			Log.d(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private ESequenceInfo(Parcel in) {
		db = in.readString();
		id = in.readInt();
		caption = in.readString();
		title = in.readString();
		gi = in.readInt();
		createDate = in.readString();
		updateDate = in.readString();
		flag = in.readInt();
		taxId = in.readInt();
		length = in.readInt();
		status = in.readString();
		comment = in.readString();
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(db);
		out.writeInt(id);
		out.writeString(caption);		
		out.writeString(title);
		out.writeInt(gi);
		out.writeString(createDate);
		out.writeString(updateDate);
		out.writeInt(flag);
		out.writeInt(taxId);
		out.writeInt(length);
		out.writeString(status);
		out.writeString(comment);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ESequenceInfo createFromParcel(Parcel in) {
			return new ESequenceInfo(in);
		}

		public ESequenceInfo[] newArray(int size) {
			return new ESequenceInfo[size];
		}
	};

	public boolean isDataOkay() {
		if (getId() <= 1) {
			return false;
		}
		return true;
	}

	public String toString() {
		return getId() + " " + getTitle();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getGi() {
		return gi;
	}

	public void setGi(int gi) {
		this.gi = gi;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getTaxId() {
		return taxId;
	}

	public void setTaxId(int taxId) {
		this.taxId = taxId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

}