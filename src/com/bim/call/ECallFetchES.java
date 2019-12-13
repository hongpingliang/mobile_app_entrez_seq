package com.bim.call;

import com.bim.es.base.ESequenceInfo;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallFetch;

public class ECallFetchES extends ECallFetch {

	private EResponseFetchES response;

	public ECallFetchES(ActivityPub activityBase, ESequenceInfo seqInfo) {
		super(activityBase);
		response = new EResponseFetchES();
		
		isLoadText = true;
//		loadFileName = ActivitySequenceDetail.DATA_FILE_NAME;
		getRequest().setId(seqInfo.getId()+"");
	}
	
	public EResponseFetchES getResponse() {
		return response;
	}

	
	protected void onLoadTextReady(String content) {
		response.setContent(content);
	}
}
