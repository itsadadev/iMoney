package com.itsada.framework.views;



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class MonthDialog {

	private Context context;
	private Dialog dialog;
	
	public MonthDialog(Context context) {
		this.context = context;
		
		dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		dialog.setContentView(com.itsada.framework.R.layout.custom_dialog_month);
		
		dialog.show();
	}

}
