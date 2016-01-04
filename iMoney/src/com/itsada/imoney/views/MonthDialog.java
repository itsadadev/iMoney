package com.itsada.imoney.views;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;

public class MonthDialog implements OnClickListener {

	private Activity activity;
	private Dialog dialog;
	public CallBack callback;

	private TextView tvYear, tvContent;
	private Button btJan, btFeb, btMar, btApr, btMay, btJun, btJul, btAug,
			btSep, btOct, btNov, btDec, btCancel, btOk, btThisMonth;
	private ImageView btPreviousYear, btNextYear;

	private Date date;

	public MonthDialog(final Activity activity) {
		this.activity = activity;

		dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		dialog.setContentView(R.layout.custom_dialog_month);

		tvYear = (TextView) dialog.findViewById(R.id.tvYear);
		tvContent = (TextView) dialog.findViewById(R.id.tvContent);

		btPreviousYear = (ImageView) dialog.findViewById(R.id.btPreviousYear);
		btNextYear = (ImageView) dialog.findViewById(R.id.btNextYear);

		btCancel = (Button) dialog.findViewById(R.id.btCancel);
		btOk = (Button) dialog.findViewById(R.id.btOk);
		btThisMonth = (Button) dialog.findViewById(R.id.btThisMonth);

		btJan = (Button) dialog.findViewById(R.id.btJan);
		btFeb = (Button) dialog.findViewById(R.id.btFeb);
		btMar = (Button) dialog.findViewById(R.id.btMar);
		btApr = (Button) dialog.findViewById(R.id.btApr);
		btMay = (Button) dialog.findViewById(R.id.btMay);
		btJun = (Button) dialog.findViewById(R.id.btJun);
		btJul = (Button) dialog.findViewById(R.id.btJul);
		btAug = (Button) dialog.findViewById(R.id.btAug);
		btSep = (Button) dialog.findViewById(R.id.btSep);
		btOct = (Button) dialog.findViewById(R.id.btOct);
		btNov = (Button) dialog.findViewById(R.id.btNov);
		btDec = (Button) dialog.findViewById(R.id.btDec);

		btJan.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btFeb.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMar.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btApr.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMay.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJun.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJul.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btAug.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btSep.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btOct.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btNov.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btDec.setBackgroundResource(R.drawable.calendar_circle_normal_shape);

		btPreviousYear.setOnClickListener(this);
		btNextYear.setOnClickListener(this);

		btCancel.setOnClickListener(click());
		btOk.setOnClickListener(click());
		btThisMonth.setOnClickListener(this);

		btJan.setOnClickListener(this);
		btFeb.setOnClickListener(this);
		btMar.setOnClickListener(this);
		btApr.setOnClickListener(this);
		btMay.setOnClickListener(this);
		btJun.setOnClickListener(this);
		btJul.setOnClickListener(this);
		btAug.setOnClickListener(this);
		btSep.setOnClickListener(this);
		btOct.setOnClickListener(this);
		btNov.setOnClickListener(this);
		btDec.setOnClickListener(this);

	}

	public void show(Date date) {

		this.date = date;

		if (App.configuration.getLocale().getLanguage().equals("en")) {
			tvYear.setText(BaseActivity.yearFormat.format(date));
			tvContent.setText(BaseActivity.monthYearEnFormat.format(date));
		} else {
			tvYear.setText(BaseActivity.yearFormat.format(date));
			tvContent.setText(BaseActivity.monthYearThFormat.format(date));
		}

		// tvYear.setText(BaseActivity.yearFormat.format(date));
		// tvContent.setText(BaseActivity.monthyearFormat.format(date));
		initMonth();

		dialog.show();
	}

	private void initMonth() {
		switch (BaseActivity.monthFormat.format(date)) {

		case "ม.ค.":
			btJan.setTextColor(Color.WHITE);
			btJan.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ก.พ.":
			btFeb.setTextColor(Color.WHITE);
			btFeb.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "มี.ค.":
			btMar.setTextColor(Color.WHITE);
			btMar.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "เม.ย.":
			btApr.setTextColor(Color.WHITE);
			btApr.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "พ.ค.":
			btMay.setTextColor(Color.WHITE);
			btMay.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "มิ.ย.":
			btJun.setTextColor(Color.WHITE);
			btJun.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ก.ค.":
			btJul.setTextColor(Color.WHITE);
			btJul.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ส.ค.":
			btAug.setTextColor(Color.WHITE);
			btAug.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ก.ย.":
			btSep.setTextColor(Color.WHITE);
			btSep.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ต.ค.":
			btOct.setTextColor(Color.WHITE);
			btOct.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "พ.ย.":
			btNov.setTextColor(Color.WHITE);
			btNov.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case "ธ.ค.":
			btDec.setTextColor(Color.WHITE);
			btDec.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;

		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		int year;
		App app = ((App) activity.getApplication());
		Calendar c = Calendar.getInstance();

		switch (v.getId()) {

		case R.id.btJan:
			date.setMonth(Calendar.JANUARY);
			app.to.setMonth(Calendar.FEBRUARY);
			app.to.setDate(-1);
			setFocus(btJan);
			break;
		case R.id.btFeb:
			date.setMonth(Calendar.FEBRUARY);
			app.to.setMonth(Calendar.MAY);
			app.to.setDate(-1);
			setFocus(btFeb);
			break;
		case R.id.btMar:
			date.setMonth(Calendar.MAY);
			app.to.setMonth(Calendar.APRIL);
			app.to.setDate(-1);
			setFocus(btMar);
			break;
		case R.id.btApr:
			date.setMonth(Calendar.APRIL);
			app.to.setMonth(Calendar.MAY);
			app.to.setDate(-1);
			setFocus(btApr);
			break;
		case R.id.btMay:
			date.setMonth(Calendar.MAY);
			app.to.setMonth(Calendar.JUNE);
			app.to.setDate(-1);
			setFocus(btMay);
			break;
		case R.id.btJun:
			date.setMonth(Calendar.JUNE);
			app.to.setMonth(Calendar.JULY);
			app.to.setDate(-1);
			setFocus(btJun);
			break;
		case R.id.btJul:
			date.setMonth(Calendar.JULY);
			app.to.setMonth(Calendar.AUGUST);
			app.to.setDate(-1);
			setFocus(btJul);
			break;
		case R.id.btAug:
			date.setMonth(Calendar.AUGUST);
			app.to.setMonth(Calendar.SEPTEMBER);
			app.to.setDate(-1);
			setFocus(btAug);
			break;
		case R.id.btSep:
			date.setMonth(Calendar.SEPTEMBER);
			app.to.setMonth(Calendar.OCTOBER);
			app.to.setDate(-1);
			setFocus(btSep);
			break;
		case R.id.btOct:
			date.setMonth(Calendar.OCTOBER);
			app.to.setMonth(Calendar.NOVEMBER);
			app.to.setDate(-1);
			setFocus(btOct);
			break;
		case R.id.btNov:
			date.setMonth(Calendar.NOVEMBER);
			app.to.setMonth(Calendar.DECEMBER);
			app.to.setDate(-1);
			setFocus(btNov);
			break;
		case R.id.btDec:
			date.setMonth(Calendar.DECEMBER);
			app.to.setMonth(Calendar.JANUARY);
			app.to.setDate(-1);
			setFocus(btDec);
			break;

		case R.id.btThisMonth:
			date.setMonth(c.getTime().getMonth());
			date.setYear(c.getTime().getYear());
			app.to.setMonth(c.getTime().getMonth() + 1);
			app.to.setDate(-1);
			setFocus(c.getTime().getMonth());
			tvYear.setText(BaseActivity.yearFormat.format(date));
			break;

		case R.id.btPreviousYear:
			year = date.getYear();
			date.setYear(year - 1);
			tvYear.setText(BaseActivity.yearFormat.format(date));

			c.setTime(app.from);
			c.add(Calendar.MONTH, -1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			break;
		case R.id.btNextYear:
			year = date.getYear();
			date.setYear(year + 1);
			tvYear.setText(BaseActivity.yearFormat.format(date));

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();
			break;
		}

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			tvContent.setText(BaseActivity.monthyearFormat.format(date));
		else
			tvContent.setText(BaseActivity.monthYearThFormat.format(date));
	}

	public void setFocus(Button bt) {

		btJan.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btFeb.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMar.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btApr.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMay.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJun.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJul.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btAug.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btSep.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btOct.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btNov.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btDec.setBackgroundResource(R.drawable.calendar_circle_normal_shape);

		btJan.setTextColor(Color.BLACK);
		btFeb.setTextColor(Color.BLACK);
		btMar.setTextColor(Color.BLACK);
		btApr.setTextColor(Color.BLACK);
		btMay.setTextColor(Color.BLACK);
		btJun.setTextColor(Color.BLACK);
		btJul.setTextColor(Color.BLACK);
		btAug.setTextColor(Color.BLACK);
		btSep.setTextColor(Color.BLACK);
		btNov.setTextColor(Color.BLACK);
		btDec.setTextColor(Color.BLACK);

		switch (bt.getId()) {
		case R.id.btJan:
			btJan.setTextColor(Color.WHITE);
			btJan.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btFeb:
			btFeb.setTextColor(Color.WHITE);
			btFeb.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btMar:
			btMar.setTextColor(Color.WHITE);
			btMar.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btApr:
			btApr.setTextColor(Color.WHITE);
			btApr.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btMay:
			btMay.setTextColor(Color.WHITE);
			btMay.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btJun:
			btJun.setTextColor(Color.WHITE);
			btJun.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btJul:
			btJul.setTextColor(Color.WHITE);
			btJul.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btAug:
			btAug.setTextColor(Color.WHITE);
			btAug.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btSep:
			btSep.setTextColor(Color.WHITE);
			btSep.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btOct:
			btOct.setTextColor(Color.WHITE);
			btOct.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btNov:
			btNov.setTextColor(Color.WHITE);
			btNov.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case R.id.btDec:
			btDec.setTextColor(Color.WHITE);
			btDec.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		}

	}

	public void setFocus(int m) {

		btJan.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btFeb.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMar.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btApr.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btMay.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJun.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btJul.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btAug.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btSep.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btOct.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btNov.setBackgroundResource(R.drawable.calendar_circle_normal_shape);
		btDec.setBackgroundResource(R.drawable.calendar_circle_normal_shape);

		btJan.setTextColor(Color.BLACK);
		btFeb.setTextColor(Color.BLACK);
		btMar.setTextColor(Color.BLACK);
		btApr.setTextColor(Color.BLACK);
		btMay.setTextColor(Color.BLACK);
		btJun.setTextColor(Color.BLACK);
		btJul.setTextColor(Color.BLACK);
		btAug.setTextColor(Color.BLACK);
		btSep.setTextColor(Color.BLACK);
		btNov.setTextColor(Color.BLACK);
		btDec.setTextColor(Color.BLACK);

		Log.d("", String.valueOf(m));
		switch (m) {

		case 0:
			btJan.setTextColor(Color.WHITE);
			btJan.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 1:
			btFeb.setTextColor(Color.WHITE);
			btFeb.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 2:
			btMar.setTextColor(Color.WHITE);
			btMar.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 3:
			btApr.setTextColor(Color.WHITE);
			btApr.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 4:
			btMay.setTextColor(Color.WHITE);
			btMay.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 5:
			btJun.setTextColor(Color.WHITE);
			btJun.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 6:
			btJul.setTextColor(Color.WHITE);
			btJul.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 7:
			btAug.setTextColor(Color.WHITE);
			btAug.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 8:
			btSep.setTextColor(Color.WHITE);
			btSep.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 9:
			btOct.setTextColor(Color.WHITE);
			btOct.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 10:
			btNov.setTextColor(Color.WHITE);
			btNov.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		case 11:
			btDec.setTextColor(Color.WHITE);
			btDec.setBackgroundResource(R.drawable.calendar_circle_focus_shape);
			break;
		}

	}

	public OnClickListener click() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.btCancel:
					dialog.dismiss();
					break;

				case R.id.btOk:
					if (callback != null)
						callback.updateView();
					dialog.dismiss();
					break;
				}

			}
		};
	}

	public interface CallBack {
		void updateView();
	}
}
