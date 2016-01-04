package com.itsada.imoney.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity.UpdateView;
import com.itsada.imoney.CreateLiabilityTransactionActivity;
import com.itsada.imoney.CreateTransactionActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionFragmentActivity;
import com.itsada.imoney.views.MonthDialog;
import com.itsada.imoney.views.MonthDialog.CallBack;
import com.itsada.imoney.views.TransactionGroupByCategoryListView;
import com.itsada.imoney.views.TransactionGroupByDateListView;

@SuppressLint("InflateParams")
public class TransactionFragment extends BaseFragemnt implements
		OnClickListener {

	// private ActionBar actionBar;
	private Account currentAccount;
	private TextView tvIncome, tvExpand, tvTotalMonth, tvMonthHeader;

	private LinearLayout rootDate, rootCategory;
	private ScrollView scrollViewDate, scrollViewCategory;

	private ImageView imgBtPreviousMonth, imgBtNextMonth, btDeposit,
			btWithdraw, imgAccountIcon;// , imgSort;
	private Button btOn, btOff; // , btGroupBy, btToday;

	private TransactionRepository transactionRepository;
	private TransactionGroupByDateListView dateTransactionGroupListView;
	private TransactionGroupByCategoryListView transactionGroupByCategoryListView;
	private ArrayList<Transaction> transactions;
	private boolean ascDate = false;
	private boolean ascCategory = true;
	private View root;
	private int page;
	private String title;
	private static TransactionFragment transactionFragment;

	public static TransactionFragment newInstance(int page, String title) {

		if (transactionFragment == null) {
			transactionFragment = new TransactionFragment();

			Bundle args = new Bundle();
			args.putInt("page", page);
			args.putString("title", title);
			transactionFragment.setArguments(args);
		}
		return transactionFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("page", 0);
		title = getArguments().getString("title");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.transaction_header_list_layout, container, false);
		root = rootView;

		currentAccount = ((TransactionFragmentActivity) getActivity()).currentAccount;
		onInit();

		return rootView;
	}

	// Create Transaction list view group by Date
	private void createViewGroupByDate(Account a) {

		scrollViewDate.setVisibility(View.VISIBLE);
		scrollViewCategory.setVisibility(View.GONE);

		rootDate = null;
		rootDate = (LinearLayout) root.findViewById(R.id.rootDate);

		rootDate.invalidate();
		for (int i = 0; i < rootDate.getChildCount(); i++) {
			rootDate.removeView(rootDate.getChildAt(i));
		}
		rootDate.invalidate();

		dateTransactionGroupListView = new TransactionGroupByDateListView(
				getActivity().getApplicationContext(), getActivity(),
				Integer.parseInt(getActivity().getIntent().getStringExtra(
						App.ACCOUNT_ID)), app.from, app.to, ascDate);

		dateTransactionGroupListView.onBindView(rootDate);
	}

	// Create Transaction list view group by Category
	private void createViewGroupByCategory(Account currentAccount) {

		scrollViewDate.setVisibility(View.GONE);
		scrollViewCategory.setVisibility(View.VISIBLE);

		rootCategory = null;
		rootCategory = (LinearLayout) root.findViewById(R.id.rootCategory);

		rootCategory.invalidate();
		for (int i = 0; i < rootDate.getChildCount(); i++) {
			rootCategory.removeView(rootCategory.getChildAt(i));
		}
		rootCategory.invalidate();

		transactionGroupByCategoryListView = new TransactionGroupByCategoryListView(
				getActivity().getApplicationContext(), getActivity(),
				Integer.parseInt(getActivity().getIntent().getStringExtra(
						App.ACCOUNT_ID)), app.from, app.to, ascCategory);

		transactionGroupByCategoryListView.onBindView(rootCategory);
	}

	@Override
	public void onClick(View v) {

		Calendar c = Calendar.getInstance();

		switch (v.getId()) {

		case R.drawable.ic_launcher:
			getActivity().finish();
			break;

		// button left month
		case R.id.imgBtPreviousMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, -1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			transactionRepository = new TransactionRepository(getActivity()
					.getApplicationContext(), App.configuration.getLocale());

			transactions = transactionRepository.getTransactionByInterval(
					Integer.parseInt(getActivity().getIntent().getStringExtra(
							App.ACCOUNT_ID)), app.from.getTime(),
					app.to.getTime());

			TransactionGroupFactory();
			break;

		// button right month
		case R.id.imgBtNextMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			transactionRepository = new TransactionRepository(getActivity()
					.getApplicationContext(), App.configuration.getLocale());

			transactions = transactionRepository.getTransactionByInterval(
					Integer.parseInt(getActivity().getIntent().getStringExtra(
							App.ACCOUNT_ID)), app.from.getTime(),
					app.to.getTime());

			TransactionGroupFactory();
			break;

		case R.id.imgHome:
			// finish();
			break;

		case R.id.imgWithdraw:
			createTransaction();

			break;

		default:
			break;
		}

	}

	private void createTransaction() {

		// 1 สินทรัพย์สภาพคล่อง', 'Liquid Asset
		// 2 สินทรัพย์เพื่อการลงทุน', 'Invertment Asset
		// 3 สินทรัพย์ส่วนตัว', 'Personal Asset
		// 4 หนี้สินระยะสั้น', 'Shot-term Liabilities
		// 5 หนี้ระยะยาว', 'Long-term Liabilities
		switch (currentAccount.getAccountTypeId()) {
		//
		case 1:

			Intent withdraw = new Intent(getActivity().getApplicationContext(),
					CreateTransactionActivity.class);
			withdraw.putExtra(App.TRANSACTION_TYPE, "withdraw");
			withdraw.putExtra(
					App.ACCOUNT_TYPE_ID,
					Integer.parseInt(getActivity().getIntent().getStringExtra(
							App.ACCOUNT_TYPE_ID)));
			withdraw.putExtra(App.ACCOUNT_ID, currentAccount.getId());
			withdraw.putExtra(App.ACCOUNT_NAME, currentAccount.getName());
			withdraw.putExtra(App.ACCOUNT_BALANCE, currentAccount.getBalance());

			startActivityForResult(withdraw,
					App.CREATE_TRANSACTION_REQUEST_CODE);

			break;
			
		case 4 :
		case 5 :
			Intent liabilities = new Intent(getActivity().getApplicationContext(),
					CreateLiabilityTransactionActivity.class);
			liabilities.putExtra(App.TRANSACTION_TYPE, "withdraw");
			liabilities.putExtra(
					App.ACCOUNT_TYPE_ID,
					Integer.parseInt(getActivity().getIntent().getStringExtra(
							App.ACCOUNT_TYPE_ID)));
			liabilities.putExtra(App.ACCOUNT_ID, currentAccount.getId());
			liabilities.putExtra(App.ACCOUNT_NAME, currentAccount.getName());
			liabilities.putExtra(App.ACCOUNT_BALANCE, currentAccount.getBalance());

			startActivityForResult(liabilities,
					App.CREATE_TRANSACTION_REQUEST_CODE);

			break;

		default:
			break;
		}

	}

	protected void onInit() {

		try {

			scrollViewDate = (ScrollView) root
					.findViewById(R.id.scrollViewDate);
			scrollViewCategory = (ScrollView) root
					.findViewById(R.id.scrollViewCategory);

			imgBtPreviousMonth = (ImageView) root
					.findViewById(R.id.imgBtPreviousMonth);
			imgBtPreviousMonth.setOnClickListener(this);
			imgBtNextMonth = (ImageView) root.findViewById(R.id.imgBtNextMonth);
			imgBtNextMonth.setOnClickListener(this);

			// imgHome = (ImageView) findViewById(R.id.imgHome);
			// imgHome.setOnClickListener(this);

			tvIncome = (TextView) root.findViewById(R.id.tvIncome);
			tvExpand = (TextView) root.findViewById(R.id.tvExpand);
			tvTotalMonth = (TextView) root.findViewById(R.id.tvTotalMonth);

			btWithdraw = (ImageView) root.findViewById(R.id.imgWithdraw);
			btWithdraw.setOnClickListener(this);
			btDeposit = (ImageView) root.findViewById(R.id.imgDeposit);
			btDeposit.setOnClickListener(this);

			btOn = (Button) root.findViewById(R.id.btOn);
			btOff = (Button) root.findViewById(R.id.btOff);
			btOn.setTag(R.drawable.troggle_on_active_shape);
			btOff.setTag(R.drawable.troggle_off_normal_shape);

			btOn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("btOn", "btOn");
					btOn.setBackgroundResource(R.drawable.troggle_on_active_shape);
					btOff.setBackgroundResource(R.drawable.troggle_off_normal_shape);

					if (btOn.getTag()
							.equals(R.drawable.troggle_on_normal_shape)) {
						createViewGroupByDate(currentAccount);

						btOn.setTag(R.drawable.troggle_on_active_shape);
						btOff.setTag(R.drawable.troggle_off_normal_shape);
					}
				}
			});

			btOff.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Log.d("btOff", "btOff");

					btOn.setBackgroundResource(R.drawable.troggle_on_normal_shape);
					btOff.setBackgroundResource(R.drawable.troggle_off_active_shape);

					if (btOff.getTag().equals(
							R.drawable.troggle_off_normal_shape)) {
						createViewGroupByCategory(currentAccount);
						btOn.setTag(R.drawable.troggle_on_normal_shape);
						btOff.setTag(R.drawable.troggle_off_active_shape);
					}
				}
			});

			transactionRepository = new TransactionRepository(getActivity()
					.getApplicationContext(), App.configuration.getLocale());

			transactions = transactionRepository.getTransactionByInterval(
					Integer.parseInt(getActivity().getIntent().getStringExtra(
							App.ACCOUNT_ID)), app.from.getTime(),
					app.to.getTime());

			TransactionGroupFactory();

			// Set month and year to header of Transactions group
			tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);

			tvMonthHeader.setText(monthFormat.format(((App) getActivity()
					.getApplication()).from));

			tvMonthHeader.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final Date f = (Date) app.from.clone();
					final Date t = (Date) app.to.clone();
					MonthDialog m = new MonthDialog(getActivity());
					m.show(app.from);

					m.callback = new CallBack() {

						@Override
						public void updateView() {

							if (!(f.equals(app.from) && t.equals(app.to)))
								TransactionGroupFactory();
						}
					};
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void setActionBar() {

		super.setActionBar();

		// actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"
		// + currentAccount.getName() + " ("
		// + moneyFormat.format(currentAccount.getBalance()) + ")"
		// + "</font>"));

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
										getActivity().getApplicationContext(),
										App.configuration.getLocale());
								// App.accountTypes = accountTypeRepository
								// .getAll();

								transactions = new TransactionRepository(
										getActivity().getApplicationContext(),
										App.configuration.getLocale())
										.getTransactionByInterval(
												Integer.parseInt(getActivity()
														.getIntent()
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
			if (resultCode == -1) {
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
										getActivity().getApplicationContext(),
										App.configuration.getLocale());
								// App.accountTypes = accountTypeRepository
								// .getAll();

								transactions = new TransactionRepository(
										getActivity().getApplicationContext(),
										App.configuration.getLocale())
										.getTransactionByInterval(
												Integer.parseInt(getActivity()
														.getIntent()
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

		if (tvMonthHeader == null)
			tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);

		tvMonthHeader.setText(monthFormat.format(((App) getActivity()
				.getApplication()).from));

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

	public void sort(ImageView imgSort) {
		if (btOn.getTag().equals(R.drawable.troggle_on_active_shape)) {
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
}
