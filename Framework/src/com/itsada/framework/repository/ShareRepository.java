package com.itsada.framework.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareRepository {

	private SharedPreferences shared;
	private Context context;
	private String appName;

	public ShareRepository(Context context, String appName) {
		this.context = context;
		this.appName = appName;
	}
	
	public void clearData() {
		shared = context.getSharedPreferences(this.appName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.clear();
		editor.commit();
	}

	public void saveData(String key, String data) {
		shared = context.getSharedPreferences(this.appName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(key, data);
		editor.commit();
	}

	public String getStringData(String key) {
		shared = context.getSharedPreferences(this.appName,
				Context.MODE_PRIVATE);
		return shared.getString(key, "");
	}

}
