package com.itsada.imoney.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.itsada.imoney.R;

public class Troggle extends View implements OnClickListener {


	private Button btOn, btOff;

	public Troggle(Context context) {
		super(context);

		LinearLayout layout = (LinearLayout)LinearLayout.inflate(context, R.layout.troggle, null);

		btOn = (Button) layout.findViewById(R.id.btOn);
		btOff = (Button) layout.findViewById(R.id.btOff);

		btOn.setOnClickListener(this);
		btOff.setOnClickListener(this);
	}
	
	public Troggle(Activity activity) {
		super(activity.getApplicationContext());

		LinearLayout layout = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.troggle, null);

		btOn = (Button) layout.findViewById(R.id.btOn);
		btOff = (Button) layout.findViewById(R.id.btOff);

		btOn.setOnClickListener(this);
		btOff.setOnClickListener(this);
	}

	public Troggle(Context context, String on, String off) {
		super(context);

		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.troggle, null);

		btOn = (Button) layout.findViewById(R.id.btOn);
		btOff = (Button) layout.findViewById(R.id.btOff);
		btOn.setText(on);
		btOff.setText(off);

		btOn.setOnClickListener(this);
		btOff.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btOn:
			btOn.setBackgroundResource(R.drawable.selector_troggle_on_normal);
			btOff.setBackgroundResource(R.drawable.selector_troggle_off_active);
			break;

		case R.id.btOff:
			btOn.setBackgroundResource(R.drawable.selector_troggle_on_active);
			btOff.setBackgroundResource(R.drawable.selector_troggle_off_normal);
			break;
		}

	}

}
