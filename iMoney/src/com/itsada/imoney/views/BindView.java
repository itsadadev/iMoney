package com.itsada.imoney.views;

import android.app.Activity;
import android.widget.LinearLayout;

public interface BindView {
	
	void onCreateView();
	double onBindView(final Activity activity, LinearLayout root,
			String group);
}
