package com.itsada.imoney;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.AccountType;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
//import com.itsada.imoney.models.Account;
import com.itsada.management.ResourcesManagement;

@SuppressLint("InflateParams")
public class EditAccountEmptryActivity extends BaseActivity {

	private ImageView imgIcon;
	private int resourceIcon;
	private int accountTypeid;
	private EditText etName;
	private EditText etBalance;
	private TextView tvMessage;
	private ImageView doneBt;
	private TextView tvAccountType;
	private TextView tvAccept;
	private TextView tvCancel;
	private CheckBox checkBox;
	Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_account_emptry_layout);

		TAG = EditAccountEmptryActivity.class.getName();
		
		setActionBar();

		AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(),
				App.configuration.getLocale());
		account = accountRepository.getById(getIntent().getIntExtra(
				App.ACCOUNT_ID, 0));

		resourceIcon = account.getIcon();

		findViewById(R.id.setVisibleLayout).setVisibility(View.VISIBLE);
		checkBox = (CheckBox) findViewById(R.id.checkBoxIsVisible);
		checkBox.setChecked(account.isHide());
		
		etName = (EditText) findViewById(R.id.etName);
		etName.setHint(getResources().getString(R.string.accountName));
		etBalance = (EditText) findViewById(R.id.etBalance);
		etBalance.setHint("0");
		imgIcon = (ImageView) findViewById(R.id.imgIcon);

		tvMessage = (TextView) findViewById(R.id.tvAccountMessage);
		tvAccountType = (TextView) findViewById(R.id.tvAccountType);
		tvAccept = (TextView) findViewById(R.id.tvAccept);
		tvCancel = (TextView) findViewById(R.id.tvCancel);

		etName.setText(account.getName());
		etBalance.setText(String.valueOf(account.getBalance()));
		tvAccountType.setText(account.getAccountType(getApplicationContext(),
				App.configuration.getLocale()));
		imgIcon.setBackgroundResource(account.getIcon());

		accountTypeid = account.getAccountTypeId();

		((RelativeLayout) findViewById(R.id.accoutTypeLayout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						openDialogChooseAccountType();
					}
				});

		((RelativeLayout) findViewById(R.id.typeLayout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						openDialogChooseIcon();
					}
				});

		etName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				// Log.d(TAG, s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

				// Log.d(TAG, s.toString());
				if (s.toString().length() > 0) {
					doneBt.setEnabled(true);
					tvAccept.setEnabled(true);
					tvAccept.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.brown_500));
					tvMessage.setVisibility(View.GONE);
				} else {
					doneBt.setEnabled(false);
					tvAccept.setEnabled(false);
					tvAccept.setTextColor(Color.GRAY);
					tvMessage.setVisibility(View.VISIBLE);
				}
			}
		});

		tvAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etName.getText().toString().equals("")) {

					try {

						double amount = etBalance.getText().length() > 0 ? Double
								.parseDouble(etBalance.getText().toString())
								: 0d;

						account.setName(etName.getText().toString());
						account.setBalance(amount);
						account.setUpdateDate(new Date(Calendar.getInstance()
								.getTimeInMillis()));
						account.setIcon(resourceIcon);
						account.setAccountTypeId(accountTypeid);
						account.setHide(checkBox.isChecked());

						AccountRepository accountRepository = new AccountRepository(
								getApplicationContext(),
								App.configuration.getLocale());

						accountRepository.update(account);

					} catch (Exception e) {

						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Failed");
						returnIntent.putExtra("account", account.getName());
						setResult(RESULT_OK, returnIntent);

						finish();

					} finally {

						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Success");
						returnIntent.putExtra("account", account.getName());
						setResult(RESULT_OK, returnIntent);

						App.isUpdateAccount = true;
						finish();
					}
				} else {
					tvMessage.setVisibility(View.VISIBLE);
				}
			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		});

	}

	protected void openDialogChooseAccountType() {
		final Dialog dialogAccountType = new Dialog(this);
		dialogAccountType.getWindow();
		dialogAccountType.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogAccountType.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogAccountType.setCancelable(true);

		dialogAccountType.setContentView(R.layout.custom_dialog_account_type);

		((TextView) dialogAccountType.findViewById(R.id.tvEvent))
				.setText(getResources().getString(R.string.accountType));
		ImageView imgBack = (ImageView) dialogAccountType
				.findViewById(R.id.imgBack);
		imgBack.setBackgroundResource(R.drawable.selector_close_event);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAccountType.dismiss();
			}
		});

		LinearLayout root = (LinearLayout) dialogAccountType
				.findViewById(R.id.root);
		LinearLayout template;
		TextView tvName;

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				getApplicationContext(),
				App.configuration.getLocale());

		for (final AccountType accountType : accountTypeRepository
				.getAll(App.configuration.getLocale())) {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.account_type_template, null);

			template.setTag(accountType.getId());
			tvName = (TextView) template.findViewById(R.id.tvName);
			tvName.setText(accountType.getName());

			template.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					accountTypeid = Integer.parseInt(String.valueOf(v.getTag()));
					tvAccountType.setText(accountType.getName());
					dialogAccountType.dismiss();
				}
			});

			root.addView(template);
		}

		dialogAccountType.show();

	}

	public void openDialogChooseIcon() {
		final Dialog dialogIcon = new Dialog(this);
		dialogIcon.getWindow();
		dialogIcon.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogIcon.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogIcon.setCancelable(true);

		dialogIcon.setContentView(R.layout.custom_dialog_account_icon);

		((TextView) dialogIcon.findViewById(R.id.tvEvent))
				.setText(getResources().getString(R.string.accountIcon));

		ImageView imgBack = (ImageView) dialogIcon.findViewById(R.id.imgBack);
		imgBack.setBackgroundResource(R.drawable.selector_close_event);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogIcon.dismiss();
			}
		});
		
		ImageView imgAdd = (ImageView) dialogIcon.findViewById(R.id.imgAdd);
		imgAdd.setVisibility(View.GONE);

		LinearLayout root = (LinearLayout) dialogIcon.findViewById(R.id.root);

		LinearLayout template;
		for (int i = 0; i < ResourcesManagement.getIconAccount().size(); i += 3) {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.icon_template, null);

			ImageView imgLeft = (ImageView) template.findViewById(R.id.imgLeft);

			ImageView imgCenter = (ImageView) template
					.findViewById(R.id.imgCenter);
			ImageView imgRight = (ImageView) template
					.findViewById(R.id.imgRight);

			imgLeft.setTag(ResourcesManagement.getIconAccount().get(i));
			imgLeft.setBackgroundResource(ResourcesManagement.getIconAccount()
					.get(i));
			if (ResourcesManagement.getIconAccount().size() > i + 1) {
				imgCenter.setTag(ResourcesManagement.getIconAccount()
						.get(i + 1));
				imgCenter.setBackgroundResource(ResourcesManagement
						.getIconAccount().get(i + 1));
			}

			if (ResourcesManagement.getIconAccount().size() > i + 2) {
				imgRight.setTag(ResourcesManagement.getIconAccount().get(i + 2));
				imgRight.setBackgroundResource(ResourcesManagement
						.getIconAccount().get(i + 2));
			}

			imgLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					resourceIcon = Integer.parseInt(v.getTag().toString());
					imgIcon.setBackgroundResource(Integer.parseInt(v.getTag()
							.toString()));
					dialogIcon.dismiss();
				}
			});

			imgCenter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					resourceIcon = Integer.parseInt(v.getTag().toString());
					imgIcon.setBackgroundResource(Integer.parseInt(v.getTag()
							.toString()));
					dialogIcon.dismiss();
				}
			});

			imgRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					resourceIcon = Integer.parseInt(v.getTag().toString());
					imgIcon.setBackgroundResource(Integer.parseInt(v.getTag()
							.toString()));
					dialogIcon.dismiss();
				}
			});

			root.addView(template);
		}

		dialogIcon.show();
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setActionBar() {

		try {

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(
					R.layout.custom_actionbar_event, null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			TextView tvEvent = (TextView) mCustomView
					.findViewById(R.id.tvEvent);
			tvEvent.setText(getResources().getString(R.string.editAccount));

			doneBt = (ImageView) mCustomView.findViewById(R.id.imgDone);

			// doneBt.setEnabled(false);

			ImageView cancelBt = (ImageView) mCustomView
					.findViewById(R.id.imgBack);
			cancelBt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					finish();					
				}
			});

			doneBt.setVisibility(View.GONE);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
