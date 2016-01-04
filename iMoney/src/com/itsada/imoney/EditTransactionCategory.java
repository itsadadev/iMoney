package com.itsada.imoney;

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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.management.ResourcesManagement;

public class EditTransactionCategory extends BaseActivity {

	private ImageView imgIcon;
	private int resourceIcon;
	private EditText etName;
	private TextView tvNameMessage;
	private ImageView doneBt;
	private TextView tvAccept;
	private TextView tvCancel;
	private RadioButton radioIncome;
	private RadioButton radioExpend;
	private CheckBox cbIsNeed;
	private LinearLayout layoutNeed;

	private TransactionCategory transactionCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_transaction_category_emptry_layout);
		onInit();
		setActionBar();
	}

	@Override
	protected void onInit() {

		TAG = CreateAccountEmptryActivity.class.getName();

		resourceIcon = R.drawable.ic_account_balance_black;

		etName = (EditText) findViewById(R.id.etName);
		etName.setHint("ชื่อประเภท");

		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		tvNameMessage = (TextView) findViewById(R.id.tvNameMessage);
		tvAccept = (TextView) findViewById(R.id.tvAccept);
		tvCancel = (TextView) findViewById(R.id.tvCancel);
		radioIncome = (RadioButton) findViewById(R.id.radioDeposit);
		radioExpend = (RadioButton) findViewById(R.id.radioWithDraw);
		cbIsNeed = (CheckBox) findViewById(R.id.cbIsNeed);

		layoutNeed = (LinearLayout) findViewById(R.id.layoutNeed);

		tvAccept.setEnabled(false);
		tvAccept.setTextColor(Color.GRAY);

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
					// doneBt.setEnabled(true);
					tvAccept.setEnabled(true);
					tvAccept.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.brown_500));
					tvNameMessage.setVisibility(View.GONE);
				} else {
					// doneBt.setEnabled(false);
					tvAccept.setEnabled(false);
					tvAccept.setTextColor(Color.GRAY);
					tvNameMessage.setVisibility(View.VISIBLE);
				}
			}
		});

		tvAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etName.getText().toString().equals("")) {

					try {

						transactionCategory
								.setName(etName.getText().toString());
						transactionCategory.setIcon(resourceIcon);
						transactionCategory.setType(radioIncome.isChecked() ? TransactionCategory.Type.Income
								.name() : TransactionCategory.Type.Expenses
								.name());
						transactionCategory.setNeed(cbIsNeed.isChecked());

						TransactionCategoryRepository categoryRepository = new TransactionCategoryRepository(
								getApplicationContext(),
								App.configuration.getLocale());
						categoryRepository.update(transactionCategory);

					} catch (Exception e) {

						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Failed");
						setResult(RESULT_OK, returnIntent);

						finish();

					} finally {

						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Success");
						setResult(RESULT_OK, returnIntent);

						finish();
					}
				} else {
					tvNameMessage.setVisibility(View.VISIBLE);
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

		TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
				getApplicationContext(),
				App.configuration.getLocale());

		Log.d(TAG,
				String.valueOf(getIntent().getIntExtra(
						App.TRANSACTION_CATEGORY_ID, 0)));
		transactionCategory = transactionCategoryRepository.getById(getIntent()
				.getIntExtra(App.TRANSACTION_CATEGORY_ID, 0));

		if (transactionCategory != null) {
			etName.setText(transactionCategory.getName());

			imgIcon.setBackgroundResource(transactionCategory.getIcon());
			if (transactionCategory.getType().equals(
					TransactionCategory.Type.Income.name())) {
				// cbIsNeed.setVisibility(View.GONE);
				layoutNeed.setVisibility(View.GONE);
				// radioIncome.setChecked(true);
			} else {
				cbIsNeed.setVisibility(View.VISIBLE);
				layoutNeed.setVisibility(View.VISIBLE);
				cbIsNeed.setChecked(transactionCategory.isNeed());
				// radioExpend.setChecked(true);
			}
		}

	}

	@SuppressLint("InflateParams")
	protected void openDialogChooseIcon() {
		final Dialog dialogIcon = new Dialog(this);
		dialogIcon.getWindow();
		dialogIcon.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogIcon.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogIcon.setCancelable(true);

		dialogIcon.setContentView(R.layout.custom_dialog_account_icon);

		((TextView) dialogIcon.findViewById(R.id.tvEvent))
				.setText(getResources().getString(R.string.categoryIcon));
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
		ImageView imgIconDialog;

		for (int icon : ResourcesManagement.getIcons()) {
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

	@SuppressLint("InflateParams")
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
			tvEvent.setText(getResources().getString(
					R.string.editTransactionCategory));

			doneBt = (ImageView) mCustomView.findViewById(R.id.imgDone);

			ImageView cancelBt = (ImageView) mCustomView
					.findViewById(R.id.imgBack);

			doneBt.setVisibility(View.GONE);
			cancelBt.setVisibility(View.GONE);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
