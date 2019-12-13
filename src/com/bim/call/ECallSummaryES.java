package com.bim.call;

import org.xml.sax.SAXException;

import com.bim.core.Util;
import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSummary;

public class ECallSummaryES extends ECallSummary {

	private static final String TAG_Caption = "Caption";
	private static final String TAG_Title = "Title";
	private static final String TAG_Gi = "Gi";
	private static final String TAG_CreateDate = "CreateDate";
	private static final String TAG_UpdateDate = "UpdateDate";
	private static final String TAG_Flags = "Flags";
	private static final String TAG_TaxId = "TaxId";
	private static final String TAG_Length = "Length";
	private static final String TAG_Status = "Status";
	private static final String TAG_Comment = "Comment";

	private EResponseSummaryES response;

	private ESequenceInfo seqInfo;

	private boolean isCatchValue = false;
	private String value;

	private boolean isIdStarted;
	private boolean isCaptionStarted;
	private boolean isTitleStarted;
	private boolean isGiStarted;
	private boolean isCreateDateStarted;
	private boolean isUpdateDateStarted;
	private boolean isFlagsStarted;
	private boolean isTaxIdStarted;
	private boolean isLengthStarted;
	private boolean isStatusStarted;
	private boolean isCommentStarted;
	
	private static final int LEVEL_DocSum = 2;
	private static final int LEVEL_MAIN = 3;
	private int level = 0;

	public ECallSummaryES(ActivityPub activityBase) {
		super(activityBase);
		response = new EResponseSummaryES();
	}

	public void startElement(String uri, String localName, String name,
			org.xml.sax.Attributes attributes) throws SAXException {
		level++;

//		Log.d("begin " + localName + " " + level);

		isCatchValue = false;
		value = null;
		if (level == LEVEL_DocSum && TAG_DocSum.equals(localName)) {
			seqInfo = new ESequenceInfo();
			seqInfo.setDb(getRequest().getDb());
			return;
		}

		if (level == LEVEL_MAIN) {
			if (TAG_Id.equals(localName)) {
				isCatchValue = true;
				isIdStarted = true;
				return;
			}

			if (attributes == null) {
				return;
			}
			if (!TAG_Item.equals(localName)) {
				return;
			}

			String attValue = attributes.getValue(TAG_ATT_NAME);
			if ( Util.isNull(attValue)) {
				return;
			}
			if (TAG_Caption.equals(attValue)) {
				isCaptionStarted = true;
				isCatchValue = true;
			} else if (TAG_Title.equals(attValue)) {
				isTitleStarted = true;
				isCatchValue = true;
			} else if (TAG_Gi.equals(attValue)) {
				isGiStarted = true;
				isCatchValue = true;
			} else if (TAG_CreateDate.equals(attValue)) {
				isCreateDateStarted = true;
				isCatchValue = true;
			} else if (TAG_UpdateDate.equals(attValue)) {
				isUpdateDateStarted = true;
				isCatchValue = true;
			} else if (TAG_Flags.equals(attValue)) {
				isFlagsStarted = true;
				isCatchValue = true;
			} else if (TAG_TaxId.equals(attValue)) {
				isTaxIdStarted = true;
				isCatchValue = true;
			} else if (TAG_Length.equals(attValue)) {
				isLengthStarted = true;
				isCatchValue = true;
			} else if (TAG_Status.equals(attValue)) {
				isStatusStarted = true;
				isCatchValue = true;
			} else if (TAG_Comment.equals(attValue)) {
				isCommentStarted = true;
				isCatchValue = true;
			}
			return;
		}
	}

	public void characters(char ch[], int start, int length) {
		if (seqInfo == null || !isCatchValue) {
			return;
		}
		value = getString(ch, start, length);
	}

	public void endElement(String namespaceURI, String localName, String qName) {
		doEndElement(namespaceURI, localName, qName);
		level--;
	}

	private void doEndElement(String namespaceURI, String localName,
			String qName) {
//		Log.d("end " + localName + " " + level);
		isCatchValue = false;
		if (seqInfo == null) {
			return;
		}

		if (level == LEVEL_DocSum && TAG_DocSum.equals(localName)) {
			if (seqInfo.isDataOkay()) {
				response.getSeqInfoList().add(seqInfo);
			}
			seqInfo = null;
			return;
		}

		if (level == LEVEL_MAIN) {
			if (isIdStarted) {
				seqInfo.setId(Util.toInt(value));
				isIdStarted = false;
				return;
			}
			if (isCaptionStarted) {
				seqInfo.setCaption(value);
				
				isCaptionStarted = false;
			} else if (isTitleStarted) {
				seqInfo.setTitle(Util.trim(value));
				isTitleStarted = false;
			} else if (isGiStarted) {
				seqInfo.setGi(Util.toInt(value));
				isGiStarted = false;
			} else if (isCreateDateStarted) {
				seqInfo.setCreateDate(value);
				isCreateDateStarted = false;
			} else if (isUpdateDateStarted) {
				seqInfo.setUpdateDate(value);
				isUpdateDateStarted = false;
			} else if (isFlagsStarted) {
				seqInfo.setFlag(Util.toInt(value));
				isFlagsStarted = false;
			} else if (isTaxIdStarted) {
				seqInfo.setTaxId(Util.toInt(value));
				isTaxIdStarted = false;
			} else if (isLengthStarted) {
				seqInfo.setLength(Util.toInt(value));
				isLengthStarted = false;
			} else if (isStatusStarted) {
				seqInfo.setStatus(value);
				isStatusStarted = false;
			} else if (isCommentStarted) {
				seqInfo.setComment(value);
				isCommentStarted = false;
			}
			return;
		}
	}

	public EResponseSummaryES getResponse() {
		return response;
	}
}
