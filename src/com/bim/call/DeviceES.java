package com.bim.call;

import android.app.Activity;

import com.bim.core.Device;
import com.bim.core.Util;
import com.bim.es.R;

public class DeviceES {
	
	public static String getUrl(Activity activity, int pageId) {
		return activity.getResources().getString(R.string.url_root)
				+ activity.getResources().getString(pageId);
	}
	
	
	public static void save(Activity activity, int actionId, String data) {
		String url = getUrl(activity, R.string.url_pubchem_device);
		url += Util.add("dAct", actionId + "", "?");
		url += Util.add("dD", Util.nullToNone(data));
		url += Util.add("dT", "_entrez_sequence");
		url += Device.getDeviceInfo(activity);

		Device post = new Device(url);
		post.start();
	}

	
}
