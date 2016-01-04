package com.itsada.imoney;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.management.ResourcesManagement;

@SuppressLint("InflateParams")
public class CreateAccountActivity extends BaseActivity {

	private ImageView imgIcon;
	private int resourceIcon;
	private EditText etName;
	private EditText etBalance;
	private TextView tvMessage;
	private ImageView doneBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_account_layout);

		TAG = CreateAccountActivity.class.getName();

		resourceIcon = R.drawable.ic_account_balance_black;

		etName = (EditText) findViewById(R.id.etName);
		etName.setHint(getResources().getString(R.string.accountName));
		etBalance = (EditText) findViewById(R.id.etBalance);
		etBalance.setHint("0");
		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		imgIcon.setBackgroundResource(R.drawable.ic_account_balance_black);
		tvMessage = (TextView) findViewById(R.id.tvAccountMessage);

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
					tvMessage.setVisibility(View.GONE);
				} else {
					doneBt.setEnabled(false);
					tvMessage.setVisibility(View.VISIBLE);
				}
			}
		});

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
		((ImageView) dialogIcon.findViewById(R.id.imgBack))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogIcon.dismiss();
					}
				});

		LinearLayout root = (LinearLayout) dialogIcon.findViewById(R.id.root);

		LinearLayout template;
		ImageView imgIconDialog;

		for (int icon : ResourcesManagement.getIconAccount()) {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.account_type_icon_list_template, null);

			template.setTag(icon);

			imgIconDialog = (ImageView) template.findViewById(R.id.icon);
			imgIconDialog.setBackgroundResource(icon);

			template.setOnClickListener(new OnClickListener() {

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
			tvEvent.setText(getResources().getString(R.string.addAccount));

			doneBt = (ImageView) mCustomView.findViewById(R.id.imgDone);

			doneBt.setEnabled(false);

			ImageView cancelBt = (ImageView) mCustomView
					.findViewById(R.id.imgBack);

			doneBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!etName.getText().toString().equals("")) {

						AccountRepository accountRepository = null;
						try {

							double amount = etBalance.getText().length() > 0 ? Double
									.parseDouble(etBalance.getText().toString())
									: 0d;

							Account account = new Account();
							account.setName(etName.getText()
									.toString());
							account.setBalance(amount);									
							account.setCreateDate(new Date(Calendar
									.getInstance().getTimeInMillis()));
							account.setIcon(resourceIcon);
							account.setAccountTypeId(getIntent().getIntExtra(
									App.ACCOUNT_TYPE_ID, -1));

							
							 accountRepository = new AccountRepository(
							 getApplicationContext(),
								App.configuration.getLocale());
							 accountRepository.add(account);
							 

						} catch (Exception e) {

							Intent returnIntent = new Intent();
							returnIntent.putExtra("result", "Failed");
							setResult(RESULT_OK, returnIntent);

							finish();

						} finally {

//							accountRepository.dispose();

							Intent returnIntent = new Intent();
							returnIntent.putExtra("result", "Success");
							setResult(RESULT_OK, returnIntent);

							finish();
						}
					} else {
						tvMessage.setVisibility(View.VISIBLE);
					}
				}
			});

			cancelBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					setResult(RESULT_CANCELED, returnIntent);
					finish();
				}
			});

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
