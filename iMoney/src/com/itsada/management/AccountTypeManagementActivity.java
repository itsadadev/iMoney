package com.itsada.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.AccountType;
import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.R.id;
import com.itsada.imoney.R.layout;
import com.itsada.imoney.R.string;

@SuppressLint("InflateParams")
public class AccountTypeManagementActivity extends BaseActivity implements
		OnClickListener {

	private CheckBox checkBox;
	private TextView tvCancel, tvAccept;
	private Configuration configuration;
	private ArrayList<AccountType> accountTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.account_type_list_layout);
		onInit();
		setActionBar();
	}

	@Override
	protected void setActionBar() {
		ActionBar actionBar = (ActionBar) getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar_event,
				null);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		TextView tvEvent = (TextView) mCustomView.findViewById(R.id.tvEvent);
		tvEvent.setText(getResources().getString(R.string.accounTypeManage));
		ImageView imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);
		((ImageView) mCustomView.findViewById(R.id.imgDone))
				.setVisibility(View.GONE);
	}

	@Override
	protected void onInit() {

		configuration = App.configuration;

		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setOnClickListener(this);
		tvAccept = (TextView) findViewById(R.id.tvAccept);
		tvAccept.setOnClickListener(this);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				getApplicationContext(), App.configuration.getLocale());

		accountTypes = accountTypeRepository.getAll(configuration.getLocale());

		LinearLayout accountTypeTemplate = null;
		TextView tvTitle = null;

		for (final AccountType accountType : accountTypes) {
			accountTypeTemplate = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.account_type_header_list_template, null);

			tvTitle = (TextView) accountTypeTemplate.findViewById(R.id.tvTitle);
			checkBox = (CheckBox) accountTypeTemplate
					.findViewById(R.id.checkBoxIsVisible);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					accountType.setVisible(isChecked);
				}
			});

			tvTitle.setText(accountType.getName());
			checkBox.setChecked(accountType.isVisible());

			root.addView(accountTypeTemplate);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvCancel:
		case R.id.imgBack:
			finish();
			break;

		case R.id.tvAccept:
			updateAccountType();
			break;

		default:
			break;
		}

	}

	private void updateAccountType() {

		try {

			AccountTypeRepository accountTypeRepository;
			for (AccountType accountType : accountTypes) {

				accountTypeRepository = new AccountTypeRepository(
						getApplicationContext(), configuration.getLocale());
				accountTypeRepository.update(accountType);
			}

		} catch (Exception e) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", "Failed");
			setResult(RESULT_OK, returnIntent);
		} finally {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", "Success");
			setResult(RESULT_OK, returnIntent);

			finish();

		}

	}
}
