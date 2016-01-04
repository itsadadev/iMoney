package com.itsada.imoney.fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity.ReturnView;
import com.itsada.imoney.BaseActivity.UpdateView;
import com.itsada.imoney.R;
import com.itsada.management.Format;

public abstract class BaseFragemnt extends Fragment {

	protected SimpleDateFormat dateFormat;
	protected SimpleDateFormat monthFormat;
	protected DecimalFormat moneyFormat;
	protected App app;
	protected UpdateView updateView;
	protected ReturnView returnView;
	/** Standard activity result: operation canceled. */
	protected static final int RESULT_CANCELED = 0;
	/** Standard activity result: operation succeeded. */
	protected static final int RESULT_OK = -1;
	
	protected Account currentAccount;
	protected abstract void onInit();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		app = ((App) getActivity().getApplication());
//		actionBar = getActivity().getActionBar();
		dateFormat = Format.getInstance(getActivity()).getDateFormat();
		monthFormat = Format.getInstance(getActivity()).getMonthFormat();
		moneyFormat = Format.getInstance(getActivity()).getMoneyFormat();
	}

	protected void showDialog(String title, String message) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		dialog.setContentView(R.layout.custom_dialog_message);

		// TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		Button btOK = (Button) dialog.findViewById(R.id.btOk);
		LinearLayout layoutOK = (LinearLayout) dialog
				.findViewById(R.id.layoutOk);

		// tvTitle.setText(title);
		tvMessage.setText(message);

		layoutOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (updateView != null)
					updateView.update();
				// if (returnView != null)
				// returnView.redirect();
				dialog.dismiss();
			}
		});

		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (updateView != null)
					updateView.update();
				// if (returnView != null)
				// returnView.redirect();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void setActionBar(){
		
	}
}
