package com.itsada.imoney;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.views.MonthDialog;
import com.itsada.imoney.views.MonthDialog.CallBack;
import com.itsada.imoney.views.TransactionGroupByCategoryListView;
import com.itsada.imoney.views.TransactionGroupByDateListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint({ "NewApi", "InflateParams", "SimpleDateFormat" })
public class TransactionListAcitvity extends BaseActivity implements
		OnClickListener {

	private SlidingMenu slidingTransactionMenu;
	private Account currentAccount;
	private TextView tvAccountName;
	private TextView tvAccountBalance;
	private TextView tvIncome;
	private TextView tvExpand;
	private TextView tvTotalMonth;

	private LinearLayout rootDate, rootCategory;
	private ScrollView scrollViewDate, scrollViewCategory;
	private TextView tvMonthHeader;

	private ImageView imgBtPreviousMonth;
	private ImageView imgBtNextMonth;

	private ImageView btDeposit;
	private ImageView btWithdraw;
	private Button btOn, btOff;

	private Button btGroupBy;
	private Button btToday;
	private ImageView imgAccountIcon;
	private ImageView imgHome;
	private ImageView menu;
	private ImageView imgSort;

	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;
	private TransactionGroupByDateListView dateTransactionGroupListView;
	private TransactionGroupByCategoryListView transactionGroupByCategoryListView;
	private ArrayList<Transaction> transactions;
	private App app;
	private boolean ascDate = false;
	private boolean ascCategory = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.transaction_header_list_layout);

		onInit();
	}

	/* Method */
	private void createViewGroupByDate(Account a) {

		scrollViewDate.setVisibility(View.VISIBLE);
		scrollViewCategory.setVisibility(View.GONE);

		rootDate = null;
		rootDate = (LinearLayout) findViewById(R.id.rootDate);

		rootDate.invalidate();
		for (int i = 0; i < rootDate.getChildCount(); i++) {
			rootDate.removeView(rootDate.getChildAt(i));
		}
		rootDate.invalidate();

		dateTransactionGroupListView = new TransactionGroupByDateListView(
				getApplicationContext(), this, Integer.parseInt(getIntent()
						.getStringExtra(App.ACCOUNT_ID)), app.from, app.to,
				ascDate);

		dateTransactionGroupListView.onBindView(rootDate);
	}

	private void createViewGroupByCategory(Account currentAccount) {

		scrollViewDate.setVisibility(View.GONE);
		scrollViewCategory.setVisibility(View.VISIBLE);

		rootCategory = null;
		rootCategory = (LinearLayout) findViewById(R.id.rootCategory);

		rootCategory.invalidate();
		for (int i = 0; i < rootDate.getChildCount(); i++) {
			rootCategory.removeView(rootCategory.getChildAt(i));
		}
		rootCategory.invalidate();

		transactionGroupByCategoryListView = new TransactionGroupByCategoryListView(
				getApplicationContext(), this, Integer.parseInt(getIntent()
						.getStringExtra(App.ACCOUNT_ID)), app.from, app.to,
				ascCategory);

		transactionGroupByCategoryListView.onBindView(rootCategory);
	}

	@Override
	public void onClick(View v) {

		Calendar c = Calendar.getInstance();

		switch (v.getId()) {
		case R.id.imgBtPreviousMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, -1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			transactionRepository = new TransactionRepository(
					getApplicationContext(), App.configuration.getLocale());

			transactions = transactionRepository
					.getTransactionByInterval(
							Integer.parseInt(getIntent().getStringExtra(
									App.ACCOUNT_ID)), app.from.getTime(),
							app.to.getTime());

			TransactionGroupFactory();
			break;

		case R.id.imgBtNextMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			transactionRepository = new TransactionRepository(
					getApplicationContext(), App.configuration.getLocale());

			transactions = transactionRepository
					.getTransactionByInterval(
							Integer.parseInt(getIntent().getStringExtra(
									App.ACCOUNT_ID)), app.from.getTime(),
							app.to.getTime());

			TransactionGroupFactory();
			break;

		case R.id.imgHome:
			finish();
			break;

		case R.id.imgWithdraw:
			Intent withdraw = new Intent(getApplicationContext(),
					CreateTransactionActivity.class);
			withdraw.putExtra(App.TRANSACTION_TYPE, "withdraw");
			withdraw.putExtra(
					App.ACCOUNT_TYPE_ID,
					Integer.parseInt(getIntent().getStringExtra(
							App.ACCOUNT_TYPE_ID)));
			withdraw.putExtra(App.ACCOUNT_ID, currentAccount.getId());
			withdraw.putExtra(App.ACCOUNT_NAME, currentAccount.getName());
			withdraw.putExtra(App.ACCOUNT_BALANCE, currentAccount.getBalance());

			startActivityForResult(withdraw,
					App.CREATE_TRANSACTION_REQUEST_CODE);
			// startActivity(withdraw);
			break;

		case R.id.imgDeposit:
			Intent deposit = new Intent(getApplicationContext(),
					CreateTransactionActivity.class);
			deposit.putExtra(App.TRANSACTION_TYPE, "deposit");
			deposit.putExtra(
					App.ACCOUNT_TYPE_ID,
					Integer.parseInt(getIntent().getStringExtra(
							App.ACCOUNT_TYPE_ID)));
			deposit.putExtra(App.ACCOUNT_ID, currentAccount.getId());
			deposit.putExtra(App.ACCOUNT_NAME, currentAccount.getName());
			deposit.putExtra(App.ACCOUNT_BALANCE, currentAccount.getBalance());

			startActivityForResult(deposit, App.CREATE_TRANSACTION_REQUEST_CODE);
			// startActivity(deposit);
			break;

		case R.id.btGroupBy:
			break;
		case R.id.btToday:

			Log.e(TAG,
					dateFormat.format(c.getTime()) + ":"
							+ dateFormat.format(app.from));
			if (c.getTimeInMillis() != app.from.getTime()) {
				Log.e(TAG,
						dateFormat.format(app.from) + ":"
								+ dateFormat.format(app.to));

				app.from = c.getTime();
				c.add(Calendar.MONTH, 1);
				c.add(Calendar.DATE, -1);
				app.to = c.getTime();

				// Log.e(TAG,
				// dateFormat.format(app.from) + ":"
				// + dateFormat.format(app.to));

				transactionRepository = new TransactionRepository(
						getApplicationContext(), App.configuration.getLocale());

				transactions = transactionRepository.getTransactionByInterval(
						Integer.parseInt(getIntent().getStringExtra(
								App.ACCOUNT_ID)), app.from.getTime(),
						app.to.getTime());

				// onCreate(null);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void setActionBar() {

		try {

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(
					R.layout.custom_actionbar_account, null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			tvAccountName = (TextView) mCustomView
					.findViewById(R.id.tvAccountName);
			tvAccountBalance = (TextView) mCustomView
					.findViewById(R.id.tvAccountBalance);

			imgAccountIcon = (ImageView) mCustomView.findViewById(R.id.imgIcon);
			menu = (ImageView) mCustomView.findViewById(R.id.imgContextMenu);

			ImageView imgGraph = (ImageView) mCustomView
					.findViewById(R.id.imgGraph);
			ImageView imgSort = (ImageView) mCustomView
					.findViewById(R.id.imgSort);

			mCustomView.findViewById(R.id.linearLayoutAccount)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});

			imgSort.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (btOn.getTag()
							.equals(R.drawable.troggle_on_active_shape)) {
						ascDate = !ascDate;
					} else {
						ascCategory = !ascCategory;
					}

					TransactionGroupFactory();
				}
			});

			imgGraph.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							TransactionGraphActivity.class);
					intent.putExtra(App.ACCOUNT_ID, currentAccount.getId());

					startActivity(intent);
				}
			});

			if (currentAccount != null) {
				tvAccountName.setText(currentAccount.getName());
			} else {
				tvAccountName.setText("");
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	protected void onInit() {

		TAG = TransactionListAcitvity.class.getName();

		app = ((App) getApplication());
		accountRepository = new AccountRepository(getApplicationContext(),
				App.configuration.getLocale());
		currentAccount = accountRepository.getById(Integer.parseInt(getIntent()
				.getStringExtra(App.ACCOUNT_ID)));

		setActionBar();
		// slideMenuLeft();

		scrollViewDate = (ScrollView) findViewById(R.id.scrollViewDate);
		scrollViewCategory = (ScrollView) findViewById(R.id.scrollViewCategory);

		tvAccountBalance
				.setText(moneyFormat.format(currentAccount.getBalance()));

		imgBtPreviousMonth = (ImageView) findViewById(R.id.imgBtPreviousMonth);
		imgBtPreviousMonth.setOnClickListener(this);
		imgBtNextMonth = (ImageView) findViewById(R.id.imgBtNextMonth);
		imgBtNextMonth.setOnClickListener(this);

		// imgHome = (ImageView) findViewById(R.id.imgHome);
		// imgHome.setOnClickListener(this);

		tvIncome = (TextView) findViewById(R.id.tvIncome);
		tvExpand = (TextView) findViewById(R.id.tvExpand);
		tvTotalMonth = (TextView) findViewById(R.id.tvTotalMonth);

		btWithdraw = (ImageView) findViewById(R.id.imgWithdraw);
		btWithdraw.setOnClickListener(this);
		btDeposit = (ImageView) findViewById(R.id.imgDeposit);
		btDeposit.setOnClickListener(this);

		btGroupBy = (Button) findViewById(R.id.btGroupBy);
		btGroupBy.setOnClickListener(this);

		btToday = (Button) findViewById(R.id.btToday);
		btToday.setOnClickListener(this);

		btOn = (Button) findViewById(R.id.btOn);
		btOff = (Button) findViewById(R.id.btOff);
		btOn.setTag(R.drawable.troggle_on_active_shape);
		btOff.setTag(R.drawable.troggle_off_normal_shape);

		btOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btOn.setBackgroundResource(R.drawable.troggle_on_active_shape);
				btOff.setBackgroundResource(R.drawable.troggle_off_normal_shape);

				if (btOn.getTag().equals(R.drawable.troggle_on_normal_shape)) {
					createViewGroupByDate(currentAccount);

					btOn.setTag(R.drawable.troggle_on_active_shape);
					btOff.setTag(R.drawable.troggle_off_normal_shape);

				}

			}
		});

		btOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btOn.setBackgroundResource(R.drawable.troggle_on_normal_shape);
				btOff.setBackgroundResource(R.drawable.troggle_off_active_shape);

				if (btOff.getTag().equals(R.drawable.troggle_off_normal_shape)) {
					createViewGroupByCategory(currentAccount);
					btOn.setTag(R.drawable.troggle_on_normal_shape);
					btOff.setTag(R.drawable.troggle_off_active_shape);

				}

			}
		});

		// root = (LinearLayout) findViewById(R.id.root);
		transactionRepository = new TransactionRepository(
				getApplicationContext(), App.configuration.getLocale());

		transactions = transactionRepository.getTransactionByInterval(
				Integer.parseInt(getIntent().getStringExtra(App.ACCOUNT_ID)),
				app.from.getTime(), app.to.getTime());

		TransactionGroupFactory();

		imgAccountIcon.setBackgroundResource(currentAccount.getIcon());

		// Set month and year to header of Transactions group
		tvMonthHeader = (TextView) findViewById(R.id.tvMonthHeader);

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			tvMonthHeader.setText(monthYearEnFormat
					.format(((App) getApplication()).from));
		else
			tvMonthHeader.setText(monthYearThFormat
					.format(((App) getApplication()).from));

		// double income = 0;
		// double expand = 0;
		// for (Transaction t : transactions) {
		// if(t.getTransactionType().equals("Income"))
		// income += t.getAmount();
		// else
		// expand += t.getAmount();
		// }
		//
		//
		//
		// tvIncome.setText("+" + moneyFormat.format(income));
		// tvExpand.setText("-" + moneyFormat.format(expand));
		// tvTotalMonth.setText(moneyFormat.format(income - expand));

		tvMonthHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Date f = (Date) app.from.clone();
				final Date t = (Date) app.to.clone();
				MonthDialog m = new MonthDialog(TransactionListAcitvity.this);
				m.show(app.from);

				m.callback = new CallBack() {

					@Override
					public void updateView() {

						Log.d(TAG, app.from.toString());
						Log.d(TAG, app.to.toString());

						// transactionRepository = new TransactionRepository(
						// getApplicationContext());
						//
						// transactions = transactionRepository
						// .getTransactionByInterval(Integer
						// .parseInt(getIntent().getStringExtra(
						// App.ACCOUNT_ID)), app.from
						// .getTime(), app.to.getTime());

						if (!(f.equals(app.from) && t.equals(app.to)))
							TransactionGroupFactory();
					}
				};
			}
		});

		if (slidingTransactionMenu == null) {
			this.slideMenu();
			this.updateMenu();
		} else {
			this.updateMenu();
		}
	}

	public void slideMenu() {

		try {

			slidingTransactionMenu = new SlidingMenu(this);

			slidingTransactionMenu.setShadowWidth(5);
			slidingTransactionMenu.setFadeDegree(0.0f);
			slidingTransactionMenu.setMode(SlidingMenu.RIGHT);
			slidingTransactionMenu.setBehindWidth(100);
			slidingTransactionMenu.setShadowWidthRes(R.dimen.shadow_width);
			slidingTransactionMenu
					.setBehindOffsetRes(R.dimen.slidingmenuTransaction_offset);
			slidingTransactionMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			slidingTransactionMenu
					.setMenu(R.layout.menu_right_transaction_list_frame);
			slidingTransactionMenu.attachToActivity(this,
					SlidingMenu.SLIDING_WINDOW);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public void updateMenu() {

		imgSort = (ImageView) slidingTransactionMenu.findViewById(R.id.imgSort);

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (slidingTransactionMenu.isMenuShowing()) {
					slidingTransactionMenu.showContent(true);
					slidingTransactionMenu.showMenu(false);
				} else {
					slidingTransactionMenu.showContent(true);
					slidingTransactionMenu.showMenu(false);
				}
			}
		});

		slidingTransactionMenu.findViewById(R.id.sortLayout)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (btOn.getTag().equals(
								R.drawable.troggle_on_active_shape)) {
							ascDate = !ascDate;
							if (ascDate)
								imgSort.setBackgroundResource(R.drawable.ic_sort_white_24dp_convert);
							else
								imgSort.setBackgroundResource(R.drawable.ic_sort_white_24dp);

						} else {
							ascCategory = !ascCategory;
							if (ascCategory)
								imgSort.setBackgroundResource(R.drawable.ic_sort_white_24dp_convert);
							else
								imgSort.setBackgroundResource(R.drawable.ic_sort_white_24dp);
						}

						TransactionGroupFactory();

					}
				});

		slidingTransactionMenu.findViewById(R.id.graphLayout)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								TransactionGraphActivity.class);
						intent.putExtra(App.ACCOUNT_ID, currentAccount.getId());

						startActivity(intent);
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == App.CREATE_TRANSACTION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {

					showDialog(
							"",
							getResources().getString(
									R.string.addTransactionSuccess));
					updateView = new UpdateView() {

						@Override
						public void update() {
							try {
								AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
										getApplicationContext(),
										App.configuration.getLocale());
//								App.accountTypes = accountTypeRepository
//										.getAll();

								transactions = new TransactionRepository(
										getApplicationContext(),
										App.configuration.getLocale())
										.getTransactionByInterval(
												Integer.parseInt(getIntent()
														.getStringExtra(
																App.ACCOUNT_ID)),
												app.from.getTime(), app.to
														.getTime());

							} finally {

								App.isUpdateTransaction = true;
								TransactionGroupFactory();
							}
						}
					};
				} else {
					showDialog(
							"",
							getResources().getString(
									R.string.addTransactionFail));
				}

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		} else if (requestCode == App.EDIT_TRANSACTION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				String messageSuccess = getResources().getString(
						R.string.editTransactionSuccess);
				String messageFail = getResources().getString(
						R.string.editTransactionFail);

				if (result.equals("Success")) {

					showDialog("", messageSuccess);
					updateView = new UpdateView() {

						@Override
						public void update() {
							try {
								AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
										getApplicationContext(),
										App.configuration.getLocale());
//								App.accountTypes = accountTypeRepository
//										.getAll();

								transactions = new TransactionRepository(
										getApplicationContext(),
										App.configuration.getLocale())
										.getTransactionByInterval(
												Integer.parseInt(getIntent()
														.getStringExtra(
																App.ACCOUNT_ID)),
												app.from.getTime(), app.to
														.getTime());

							} finally {

								App.isUpdateTransaction = true;
								TransactionGroupFactory();
							}
						}
					};
				} else {
					showDialog("", messageFail);
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

	}

	public void TransactionGroupFactory() {

		Log.d("", "TransactionGroupFactory");
		Log.d("btOn", btOn.getTag().toString());
		Log.d("btOff", btOff.getTag().toString());

		if (tvMonthHeader == null)
			tvMonthHeader = (TextView) findViewById(R.id.tvMonthHeader);

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			tvMonthHeader.setText(monthYearEnFormat
					.format(((App) getApplication()).from));
		else
			tvMonthHeader.setText(monthYearThFormat
					.format(((App) getApplication()).from));

		if (btOn.getTag().equals(R.drawable.troggle_on_active_shape)) {
			createViewGroupByDate(currentAccount);
			btOn.setTag(R.drawable.troggle_on_active_shape);
			btOff.setTag(R.drawable.troggle_off_normal_shape);
		} else {
			createViewGroupByCategory(currentAccount);
			btOn.setTag(R.drawable.troggle_on_normal_shape);
			btOff.setTag(R.drawable.troggle_off_active_shape);
		}

		double income = 0;
		double expand = 0;
		for (Transaction t : transactions) {
			if (t.getTransactionType().equals("Income")
					|| t.getTransactionType().equals(
							TransactionType.TransferTo.name()))
				income += t.getAmount();
			else
				expand += t.getAmount();
		}

		tvIncome.setText("+" + moneyFormat.format(income));
		tvExpand.setText("-" + moneyFormat.format(expand));
		tvTotalMonth.setText(moneyFormat.format(income - expand));

	}
}
