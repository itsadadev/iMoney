package com.itsada.imoney.views;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;

public class DateFormatDialog implements OnClickListener {

	// private Activity activity;
	private Dialog dialog;
	public DateFormatCallback callback;

	private TextView tvContent;
	private Button btCancel, btOk;
	private RadioButton radioDDMMYYYYEn, radioMMDDYYYYEn, radioYYYDDMMEn,
			radioDDMMYYYYTh, radioMMDDYYYYTh, radioYYYDDMMTh;
	public String format;

	public Locale locale;

	public DateFormatDialog(Activity activity) {
		// this.activity = activity;

		dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);

		dialog.setContentView(R.layout.custom_dialog_date_format);

		tvContent = (TextView) dialog.findViewById(R.id.tvContent);
		radioDDMMYYYYEn = (RadioButton) dialog
				.findViewById(R.id.radioDDMMYYYYEn);
		radioMMDDYYYYEn = (RadioButton) dialog
				.findViewById(R.id.radioMMDDYYYYEn);
		radioYYYDDMMEn = (RadioButton) dialog.findViewById(R.id.radioYYYDDMMEn);

		radioDDMMYYYYTh = (RadioButton) dialog
				.findViewById(R.id.radioDDMMYYYYTh);
		radioMMDDYYYYTh = (RadioButton) dialog
				.findViewById(R.id.radioMMDDYYYYTh);
		radioYYYDDMMTh = (RadioButton) dialog.findViewById(R.id.radioYYYDDMMTh);

		btCancel = (Button) dialog.findViewById(R.id.btCancel);
		btOk = (Button) dialog.findViewById(R.id.btOk);

		radioDDMMYYYYEn.setOnClickListener(this);
		radioMMDDYYYYEn.setOnClickListener(this);
		radioYYYDDMMEn.setOnClickListener(this);

		radioDDMMYYYYTh.setOnClickListener(this);
		radioMMDDYYYYTh.setOnClickListener(this);
		radioYYYDDMMTh.setOnClickListener(this);

		btCancel.setOnClickListener(this);
		btOk.setOnClickListener(this);
	}

	public void show() {

		dialog.show();
	}

	public void show(String title) {

		tvContent.setText(title);
		dialog.show();
	}

	public void setFormat(String format) {

		radioDDMMYYYYEn.setChecked(false);
		radioMMDDYYYYEn.setChecked(false);
		radioYYYDDMMEn.setChecked(false);

		radioDDMMYYYYTh.setChecked(false);
		radioMMDDYYYYTh.setChecked(false);
		radioYYYDDMMTh.setChecked(false);
		
		if (format.equals(BaseActivity.ddMMyyyyEnFormat)) {
			radioDDMMYYYYEn.setChecked(true);
		} else if (format.equals(BaseActivity.MMddyyyyEnFormat)) {
			radioMMDDYYYYEn.setChecked(true);
		} else if (format.equals(BaseActivity.yyyyddMMEnFormat)) {
			radioYYYDDMMEn.setChecked(true);
		} else if (format.equals(BaseActivity.ddMMyyyyThFormat)) {
			radioDDMMYYYYTh.setChecked(true);
		} else if (format.equals(BaseActivity.MMddyyyyThFormat)) {
			radioMMDDYYYYTh.setChecked(true);
		} else if (format.equals(BaseActivity.yyyyddMMThFormat)) {
			radioYYYDDMMTh.setChecked(true);
		}
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btCancel:
			dialog.dismiss();
			break;

		case R.id.btOk:
			if (callback != null)
				callback.callback();
			dialog.dismiss();
			break;

		case R.id.radioDDMMYYYYEn:
		case R.id.radioMMDDYYYYEn:
		case R.id.radioYYYDDMMEn:

		case R.id.radioDDMMYYYYTh:
		case R.id.radioMMDDYYYYTh:
		case R.id.radioYYYDDMMTh:

			radioDDMMYYYYEn.setChecked(false);
			radioMMDDYYYYEn.setChecked(false);
			radioYYYDDMMEn.setChecked(false);

			radioDDMMYYYYTh.setChecked(false);
			radioMMDDYYYYTh.setChecked(false);
			radioYYYDDMMTh.setChecked(false);

			setCheck(v);

			break;

		}
	}

	private void setCheck(View v) {
		switch (v.getId()) {
		case R.id.radioDDMMYYYYEn:
			radioDDMMYYYYEn.setChecked(true);
			format = BaseActivity.ddMMyyyyEnFormat;
			break;			
		case R.id.radioMMDDYYYYEn:
			radioMMDDYYYYEn.setChecked(true);
			format = BaseActivity.MMddyyyyEnFormat;
			break;
		case R.id.radioYYYDDMMEn:
			radioYYYDDMMEn.setChecked(true);
			format = BaseActivity.yyyyddMMEnFormat;
			break;
		case R.id.radioDDMMYYYYTh:
			radioDDMMYYYYTh.setChecked(true);
			format = BaseActivity.ddMMyyyyThFormat;
			break;
		case R.id.radioMMDDYYYYTh:
			radioMMDDYYYYTh.setChecked(true);
			format = BaseActivity.MMddyyyyThFormat;
			break;
		case R.id.radioYYYDDMMTh:
			radioYYYDDMMTh.setChecked(true);
			format = BaseActivity.yyyyddMMThFormat;
			break;
		}
	}

	public void setVisibleBtCancel(int visible) {
		btCancel.setVisibility(visible);
	}

	public interface DateFormatCallback {
		void callback();
	}
}
