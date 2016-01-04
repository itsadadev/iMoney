package com.itsada.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.AccountType;
import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.CreateAccountEmptryActivity;
import com.itsada.imoney.EditAccountEmptryActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.BaseActivity.UpdateView;
import com.itsada.imoney.R.anim;
import com.itsada.imoney.R.id;
import com.itsada.imoney.R.layout;
import com.itsada.imoney.R.string;
import com.itsada.imoney.views.ConfirmDialog;

@SuppressLint("InflateParams")
public class AccountManagementActivity extends BaseActivity implements
		OnClickListener {

	private CheckBox checkBox;
	private TextView tvCancel, tvAccept;
	private Configuration configuration;
	private ArrayList<AccountType> accountTypes;
	private ArrayList<LinearLayout> categoryGroup = new ArrayList<LinearLayout>();

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
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar_account_type,
				null);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		TextView tvEvent = (TextView) mCustomView.findViewById(R.id.tvEvent);
		tvEvent.setText(getResources().getString(R.string.accounManage));
		ImageView imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);

		ImageView imgAdd = (ImageView) mCustomView.findViewById(R.id.imgDone);		
		imgAdd.setOnClickListener(this);
	}

	@Override
	protected void onInit() {

		configuration = App.configuration;

		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setVisibility(View.GONE);
		tvAccept = (TextView) findViewById(R.id.tvAccept);
		tvAccept.setVisibility(View.GONE);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				getApplicationContext(), App.configuration.getLocale());

		accountTypes = accountTypeRepository.getAll(configuration.getLocale());

		LinearLayout accountTypeTemplate = null;
		TextView tvTitle = null;
		ImageView imgAdd;

		RelativeLayout layoutExpand;
		LinearLayout editLayout, deleteLayout;
		ImageView iconCategory;
		TextView tvAccountName;

		for (final AccountType accountType : accountTypes) {
			accountTypeTemplate = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.account_header_list_template, null);

			((TextView) accountTypeTemplate.findViewById(R.id.tvSumL))
					.setVisibility(View.GONE);

			tvTitle = (TextView) accountTypeTemplate.findViewById(R.id.tvTitle);
			tvTitle.setText(accountType.getName());

			layoutExpand = (RelativeLayout) accountTypeTemplate
					.findViewById(R.id.layoutExpand);
			layoutExpand.setTag(accountType.getId());

			imgAdd = (ImageView) accountTypeTemplate.findViewById(R.id.imgAdd);
			imgAdd.setVisibility(View.VISIBLE);
			imgAdd.setOnClickListener(this);

			LinearLayout accountTemplate = null;
			LinearLayout account_layout;

			account_layout = (LinearLayout) accountTypeTemplate
					.findViewById(R.id.account_layout);
			account_layout.setTag(accountType.getId());
			categoryGroup.add(account_layout);

			layoutExpand.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int id = Integer.parseInt(v.getTag().toString());
					for (final LinearLayout layout : categoryGroup) {
						if (Integer.parseInt(layout.getTag().toString()) == id) {

							LinearLayout parent = (LinearLayout) layout
									.getParent();
							if (layout.getVisibility() == View.VISIBLE) {
								layout.setVisibility(View.GONE);
								layout.startAnimation(AnimationUtils
										.loadAnimation(
												AccountManagementActivity.this,
												R.anim.push_up_in));

								parent.findViewById(R.id.imgExpand)
										.setVisibility(View.VISIBLE);
								parent.findViewById(R.id.imgCollapes)
										.setVisibility(View.GONE);
							} else {
								layout.setVisibility(View.VISIBLE);
								// layout.startAnimation(AnimationUtils.loadAnimation(context,
								// R.anim.push_up_in));
								parent.findViewById(R.id.imgExpand)
										.setVisibility(View.GONE);
								parent.findViewById(R.id.imgCollapes)
										.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			});

			for (final Account account : accountType.getAccounts()) {
				accountTemplate = (LinearLayout) LayoutInflater.from(this)
						.inflate(R.layout.menu_item_template, null);

				iconCategory = (ImageView) accountTemplate
						.findViewById(R.id.imgIcon);
				iconCategory.setBackgroundResource(account.getIcon());

				tvAccountName = (TextView) accountTemplate
						.findViewById(R.id.tvTitle);

				tvAccountName.setText(account.getName() + " ("
						+ (account.isHide() ? "show" : "hide") + ") ");

				ImageView imgEvent = (ImageView) accountTemplate
						.findViewById(R.id.imgEvent);
				editLayout = (LinearLayout) accountTemplate
						.findViewById(R.id.editLayout);
				editLayout.setTag(account.getId());
				imgEvent.setTag(account.getId());

				imgEvent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(getApplicationContext(),
								EditAccountEmptryActivity.class);
						intent.putExtra(App.ACCOUNT_ID,
								Integer.parseInt(v.getTag().toString()));
						startActivity(intent);
						// startActivityForResult(intent,
						// App.EDIT_ACCOUNT_REQUEST_CODE);
					}
				});

				ImageView imgEventDelete = (ImageView) accountTemplate
						.findViewById(R.id.imgEventDel);
				deleteLayout = (LinearLayout) accountTemplate
						.findViewById(R.id.deleteLayout);
				deleteLayout.setTag(account.getId());
				imgEventDelete.setTag(account.getId());

				imgEventDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						try {

							ConfirmDialog confirmDialog = new ConfirmDialog(
									AccountManagementActivity.this);
							confirmDialog.show(getResources().getString(
									R.string.confirmDeleteAccount));
							confirmDialog.callback = new ConfirmDialog.Callback() {

								@Override
								public void callback() {
									AccountRepository accountRepository = new AccountRepository(
											getApplicationContext(),
											App.configuration.getLocale());

									accountRepository.delete(Integer.parseInt(v
											.getTag().toString()));

									showDialog(
											"",
											getResources()
													.getString(
															R.string.deleteAccountSuccess));
									updateView = new UpdateView() {
										@Override
										public void update() {
											onCreate(null);
											slidingMainMenuLeft.showContent();
										}
									};
								}
							};

						} catch (Exception e) {
							showDialog(
									"",
									getResources().getString(
											R.string.deleteAccountFail));
						}
					}
				});

				account_layout.addView(accountTemplate);

			}

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
		case R.id.imgAdd:
			addAccount();
			break;
		case R.id.imgDone:
			Intent intent = new Intent(getApplicationContext(),
					CreateAccountEmptryActivity.class);
			startActivityForResult(intent, App.CREATE_ACCOUNT_REQUEST_CODE);
			break;
		default:
			break;
		}

	}

	private void addAccount() {

		Intent intent = new Intent(getApplicationContext(),
				CreateAccountEmptryActivity.class);
		startActivityForResult(intent, App.CREATE_ACCOUNT_REQUEST_CODE);

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == App.CREATE_ACCOUNT_REQUEST_CODE
				|| requestCode == App.EDIT_ACCOUNT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				String title = requestCode == App.CREATE_ACCOUNT_REQUEST_CODE ? getResources()
						.getString(R.string.addAccount) : getResources()
						.getString(R.string.editAccount);

				if (result.equals("Success")) {
					showDialog(
							"",
							title
									+ " : "
									+ data.getStringExtra("account")
									+ " "
									+ getResources()
											.getString(R.string.success));
					updateView = new UpdateView() {
						@Override
						public void update() {
							try {
//								AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
//										getApplicationContext(),
//										App.configuration.getLocale());
								// App.accountTypes = accountTypeRepository
								// .getAll();
							} finally {
								onCreate(null);
								// slidingMenu.showContent();
							}
						}
					};
				} else {
					showDialog(
							"",
							title + " : " + data.getStringExtra("account")
									+ " "
									+ getResources().getString(R.string.fail));
					// slidingMenu.showContent();
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}
}
