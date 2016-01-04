package com.itsada.imoney.views;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itsada.imoney.R;

public class ConfirmDialog implements OnClickListener {

//	private Activity activity;
	private Dialog dialog;
	public Callback callback;

	private TextView tvMessage;
	private Button btCancel, btOk;

	public ConfirmDialog(Activity activity) {
//		this.activity = activity;

		dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);

		dialog.setContentView(R.layout.custom_dialog_confirm);

		tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		btCancel = (Button) dialog.findViewById(R.id.btCancel);
		btOk = (Button) dialog.findViewById(R.id.btOk);

		btCancel.setOnClickListener(this);
		btOk.setOnClickListener(this);
	}

	public void show() {

		dialog.show();
	}

	public void show(String title) {

		tvMessage.setText(title);
		dialog.show();
	}
	
	public void show(Locale locale) {
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
		}
	}
	
	public interface Callback {
		void callback();
	}
}
