package com.itsada.imoney.views;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.itsada.imoney.R;

public class LanguageDialog implements OnClickListener {

//	private Activity activity;
	private Dialog dialog;
	public Callback callback;

	private TextView tvContent;
	private Button btCancel, btOk;
	private RadioButton radioTh, radioEng;

	public Locale locale = Locale.ENGLISH;

	public LanguageDialog(Activity activity) {
//		this.activity = activity;

		dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);

		dialog.setContentView(R.layout.custom_dialog_language);

		tvContent = (TextView) dialog.findViewById(R.id.tvContent);
		radioTh = (RadioButton) dialog.findViewById(R.id.radioTh);
		radioEng = (RadioButton) dialog.findViewById(R.id.radioEng);
		btCancel = (Button) dialog.findViewById(R.id.btCancel);
		btOk = (Button) dialog.findViewById(R.id.btOk);

		radioTh.setOnClickListener(this);
		radioEng.setOnClickListener(this);
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
	
	public void show(Locale locale) {

		this.locale = locale;
		if(locale.equals(Locale.ENGLISH)){
			radioEng.setChecked(true);
			radioTh.setChecked(false);
		}
		else{
			radioTh.setChecked(true);
			radioEng.setChecked(false);
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

		case R.id.radioTh:
			radioEng.setChecked(false);
			locale = new Locale("th");
			
			Log.d("", locale.getLanguage());
			break;
		case R.id.radioEng:
			radioTh.setChecked(false);
			locale = Locale.ENGLISH;
			Log.d("", locale.getLanguage());
			break;
		}

	}
	
	public void setVisibleBtCancel(int visible){
		btCancel.setVisibility(visible);
	}

	public interface Callback {
		void callback();
	}
}
