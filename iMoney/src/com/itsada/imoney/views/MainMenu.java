package com.itsada.imoney.views;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itsada.imoney.App;
import com.itsada.imoney.R;
import com.itsada.imoney.analysis.PersonalBalanceSheetActivity;
import com.itsada.management.AccountManagementActivity;
import com.itsada.management.AccountTypeManagementActivity;
import com.itsada.management.CategoryManagementActivity;
import com.itsada.management.SettingManagementActivity;
import com.itsada.management.TemplateManagementActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainMenu implements OnClickListener {

	private Activity activity;
	private SlidingMenu menu;

	public MainMenu(Activity activity, SlidingMenu slidingMenu) {
		this.activity = activity;
		this.menu = slidingMenu;
		createView();
	}

	public void createView() {

		RelativeLayout accountTypeLayout = (RelativeLayout) menu
				.findViewById(R.id.accountTypeLayout);
		accountTypeLayout.setOnClickListener(this);

		RelativeLayout accountLayout = (RelativeLayout) menu
				.findViewById(R.id.accountLayout);
		accountLayout.setOnClickListener(this);

		RelativeLayout templateLayout = (RelativeLayout) menu
				.findViewById(R.id.templateLayout);
		templateLayout.setOnClickListener(this);

		RelativeLayout categoryLayout = (RelativeLayout) menu
				.findViewById(R.id.categoryLayout);
		categoryLayout.setOnClickListener(this);

		RelativeLayout settingLayout = (RelativeLayout) menu
				.findViewById(R.id.settingLayout);
		settingLayout.setOnClickListener(this);

		RelativeLayout personalBalanceSheetLayout = (RelativeLayout) menu
				.findViewById(R.id.personalBalanceSheetLayout);
		personalBalanceSheetLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.accountTypeLayout:

			Intent accountTyoeIntent = new Intent(activity,
					AccountTypeManagementActivity.class);
			activity.startActivityForResult(accountTyoeIntent,
					App.UPDATE_ACCOUNT_TYPE_REQUEST_CODE);
			break;

		case R.id.accountLayout:
			Intent accountIntent = new Intent(activity,
					AccountManagementActivity.class);
			activity.startActivityForResult(accountIntent,
					App.UPDATE_ACCOUNT_TYPE_REQUEST_CODE);
			break;

		case R.id.templateLayout:
			Intent templateIntent = new Intent(activity,
					TemplateManagementActivity.class);
			activity.startActivityForResult(templateIntent,
					App.EDIT_TEMPLATE_REQUEST_CODE);
			break;

		case R.id.categoryLayout:
			Intent categoryIntent = new Intent(activity,
					CategoryManagementActivity.class);
			activity.startActivityForResult(categoryIntent,
					App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE);
			break;

		case R.id.settingLayout:
			Intent settingIntent = new Intent(activity,
					SettingManagementActivity.class);
			activity.startActivity(settingIntent);
			break;

		case R.id.personalBalanceSheetLayout:
			Intent personalBalanceSheetIntent = new Intent(activity,
					PersonalBalanceSheetActivity.class);
			activity.startActivity(personalBalanceSheetIntent);
			break;

		default:
			break;
		}

	}
}
