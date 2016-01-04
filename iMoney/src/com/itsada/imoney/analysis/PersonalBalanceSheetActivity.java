package com.itsada.imoney.analysis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.AccountType;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.management.Format;

@SuppressLint("InflateParams")
public class PersonalBalanceSheetActivity extends BaseActivity implements
		OnClickListener {

	private TableLayout liquidLayout;
	private TableLayout invertmentLayout;
	private TableLayout personalLayout;
	private TableLayout assetRootLayout;
	private TableLayout shortTermLiabilitiesLayout;
	private TableLayout longTermLiabilitiesLayout;
	private TableLayout liabilitiesRootLayout;
	private TableLayout netWorseRootLayout;
	private TableLayout netWorseAndLiabilitiesRootLayout;

	private TextView tvLiquidTotal, tvLiquidPercentTotal, tvInvertmentTotal,
			tvInvertmentPercentTotal, tvPersonalTotal, tvPersonalPercentTotal,
			tvShortTermLiabilitiesTotal, tvShortTermLiabilitiesPercentTotal,
			tvLongTermLiabilitiesTotal, tvLongTermLiabilitiesPercentTotal,
			tvTotalAsset, tvTotalAssetPercent, tvTotalLiabilities,
			tvTotalLiabilitiesPercent, tvNetWorse, tvNetWorseAndLiabilities;
	private DecimalFormat moneyFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.personal_balance_sheet_layout);
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
		tvEvent.setText(getResources().getString(R.string.personalBalanceSheet));
		ImageView imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);
		((ImageView) mCustomView.findViewById(R.id.imgDone))
				.setVisibility(View.GONE);
	}

	@Override
	protected void onInit() {

		moneyFormat = Format.getInstance(this).getMoneyFormat();

		liquidLayout = (TableLayout) findViewById(R.id.liquidLayout);
		invertmentLayout = (TableLayout) findViewById(R.id.investmentRoot);
		personalLayout = (TableLayout) findViewById(R.id.personalRoot);
		assetRootLayout = (TableLayout) findViewById(R.id.assertRoot);
		shortTermLiabilitiesLayout = (TableLayout) findViewById(R.id.shortTermLiabilitiesLayout);
		longTermLiabilitiesLayout = (TableLayout) findViewById(R.id.longTermLiabilitiesLayout);
		liabilitiesRootLayout = (TableLayout) findViewById(R.id.liabilitiesRoot);
		netWorseRootLayout = (TableLayout) findViewById(R.id.netWorseRoot);
		netWorseAndLiabilitiesRootLayout = (TableLayout) findViewById(R.id.netWorseAndLiabilitiesRoot);

		// tvLiquidTotal = (TextView) findViewById(R.id.tvLiquidTotal);
		// tvLiquidPercentTotal = (TextView)
		// findViewById(R.id.tvLiquidPercentTotal);
		//
		// tvInvertmentTotal = (TextView) findViewById(R.id.tvInvertmentTotal);
		// tvInvertmentPercentTotal = (TextView)
		// findViewById(R.id.tvInvertmentPercentTotal);
		//
		// tvPersonalTotal = (TextView) findViewById(R.id.tvPersonalTotal);
		// tvPersonalPercentTotal = (TextView)
		// findViewById(R.id.tvPersonalPercentTotal);
		//
		// tvShortTermLiabilitiesTotal = (TextView)
		// findViewById(R.id.tvShortTermLiabilitiesTotal);
		// tvShortTermLiabilitiesPercentTotal = (TextView)
		// findViewById(R.id.tvShortTermLiabilitiesPercentTotal);
		//
		// tvLongTermLiabilitiesTotal = (TextView)
		// findViewById(R.id.tvLongTermLiabilitiesTotal);
		// tvLongTermLiabilitiesPercentTotal = (TextView)
		// findViewById(R.id.tvLongTermLiabilitiesPercentTotal);

		// tvTotalAsset = (TextView) findViewById(R.id.tvTotalAsset);
		// tvNetWorse = (TextView) findViewById(R.id.tvNetWorse);
		// tvNetWorseAndLiabilities = (TextView)
		// findViewById(R.id.tvNetWorseAndLiabilities);
		// tvTotalAssetPercent = (TextView)
		// findViewById(R.id.tvTotalAssetPercent);

		// tvTotalLiabilities = (TextView)
		// findViewById(R.id.tvTotalLiabilities);
		// tvTotalLiabilitiesPercent = (TextView)
		// findViewById(R.id.tvTotalLiabilities);

		Locale locale = App.configuration.getLocale();

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				getApplicationContext(), locale);
		ArrayList<AccountType> accountTypes = accountTypeRepository
				.getAll(locale);

		LinearLayout template;
		TextView tvName, tvAmount, tvPercent;
		double total, sumAssert = 0, sumLiabilities = 0;

		for (AccountType accountType : accountTypes) {

			total = 0;
			switch (accountType.getId()) {
			case 1:
				for (Account account : accountType.getAccounts()) {
					total += account.getBalance();
					createRow(
							liquidLayout,
							account.getName(),
							displayMoney(account.getBalance()),
							displayMoney(getAssertPercent(accountTypes,
									account.getBalance())));

				}
				sumAssert += total;
				createRow(liquidLayout, getResources().getString(R.string.sum),
						displayMoney(total),
						displayMoney(getAssertPercent(accountTypes, total)));

				break;
			case 2:
				for (Account account : accountType.getAccounts()) {
					total += account.getBalance();
					createRow(
							invertmentLayout,
							account.getName(),
							displayMoney(account.getBalance()),
							displayMoney(getAssertPercent(accountTypes,
									account.getBalance())));
				}
				sumAssert += total;
				createRow(invertmentLayout,
						getResources().getString(R.string.sum),
						displayMoney(total),
						displayMoney(getAssertPercent(accountTypes, total)));
				break;
			case 3:
				for (Account account : accountType.getAccounts()) {
					total += account.getBalance();
					createRow(
							personalLayout,
							account.getName(),
							displayMoney(account.getBalance()),
							displayMoney(getAssertPercent(accountTypes,
									account.getBalance())));
				}
				sumAssert += total;
				createRow(personalLayout, getResources()
						.getString(R.string.sum), displayMoney(total),
						displayMoney(getAssertPercent(accountTypes, total)));
				break;

			case 4:
				for (Account account : accountType.getAccounts()) {
					total += account.getBalance();
					createRow(
							shortTermLiabilitiesLayout,
							account.getName(),
							displayMoney(account.getBalance()),
							displayMoney(getLiabilitiesPercent(accountTypes,
									account.getBalance())));
				}
				sumLiabilities += total;
				createRow(
						shortTermLiabilitiesLayout,
						getResources().getString(R.string.sum),
						displayMoney(total),
						displayMoney(getLiabilitiesPercent(accountTypes, total)));
				break;
			case 5:
				for (Account account : accountType.getAccounts()) {
					total += account.getBalance();
					createRow(
							longTermLiabilitiesLayout,
							account.getName(),
							displayMoney(account.getBalance()),
							displayMoney(getLiabilitiesPercent(accountTypes,
									account.getBalance())));
				}
				sumLiabilities += total;
				createRow(
						longTermLiabilitiesLayout,
						getResources().getString(R.string.sum),
						displayMoney(total),
						displayMoney(getLiabilitiesPercent(accountTypes, total)));
			default:
				break;
			}
		}

		createRow(assetRootLayout, "#0101DF",
				getResources().getString(R.string.assetSum),
				displayMoney(sumAssert),
				displayMoney(getAssertPercent(accountTypes, sumAssert)));
		createRow(liabilitiesRootLayout, "#B40404",
				getResources().getString(R.string.liabilitySum),
				displayMoney(sumLiabilities),
				displayMoney(getLiabilitiesPercent(sumAssert, sumLiabilities)));

		createRow(netWorseRootLayout, "#04B404",
				getResources().getString(R.string.total),
				displayMoney(sumAssert - sumLiabilities),
				displayMoney(getAssertPercent(sumAssert, sumAssert - sumLiabilities)));
		createRow(netWorseAndLiabilitiesRootLayout, "#0101DF", getResources()
				.getString(R.string.totalNetWorseAndLiabilities),
				displayMoney(sumAssert),
				displayMoney(getAssertPercent(accountTypes, sumAssert)));
		// tvTotalAsset.setText(moneyFormat.format(sumAssert));
		// tvTotalAssetPercent.setText(displayMoney(getAssertPercent(accountTypes,
		// sumAssert)));
		// tvTotalLiabilities.setText(moneyFormat.format(sumLiabilities));
		// tvNetWorse.setText(displayMoney(sumAssert - sumLiabilities));
		// tvNetWorseAndLiabilities.setText(displayMoney(sumAssert));

	}

	private void createRow(LinearLayout layout, String name, String amount,
			String percent) {
		LinearLayout template;
		TextView tvName;
		TextView tvAmount;
		TextView tvPercent;

		template = (LinearLayout) getLayoutInflater().inflate(
				R.layout.personal_balance_sheet_template, null);
		tvName = (TextView) template.findViewById(R.id.tvName);
		tvAmount = (TextView) template.findViewById(R.id.tvAmount);
		tvPercent = (TextView) template.findViewById(R.id.tvPercent);

		tvName.setText(name);
		tvAmount.setText(amount);
		tvPercent.setText(percent);
		layout.addView(template);
	}

	private void createRow(LinearLayout layout, String color, String name,
			String amount, String percent) {
		LinearLayout template;
		TextView tvName;
		TextView tvAmount;
		TextView tvPercent;

		template = (LinearLayout) getLayoutInflater().inflate(
				R.layout.personal_balance_sheet_template, null);
		tvName = (TextView) template.findViewById(R.id.tvName);
		tvName.setTextColor(Color.parseColor(color));
		tvAmount = (TextView) template.findViewById(R.id.tvAmount);
		tvAmount.setTextColor(Color.parseColor(color));
		tvPercent = (TextView) template.findViewById(R.id.tvPercent);
		tvPercent.setTextColor(Color.parseColor(color));

		tvName.setText(name);
		tvAmount.setText(amount);
		tvPercent.setText(percent);
		layout.addView(template);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		default:
			break;
		}

	}

	private double getAssertPercent(ArrayList<AccountType> accountTypes,
			double balance) {
		double sum = AccountType.getAssertPercent(accountTypes);
		return (balance / sum) * 100;
	}
	
	private double getAssertPercent(double sum,
			double asset) {
		return (asset / sum) * 100;
	}

	private double getLiabilitiesPercent(ArrayList<AccountType> accountTypes,
			double balance) {
		double sum = AccountType.getLiabilitiesPercent(accountTypes);
		return (balance / sum) * 100;
	}

	private double getLiabilitiesPercent(double sum, double liabilities) {
		return (liabilities / sum) * 100;
	}

	private String displayAmountAndPercent(double amount, double percent) {
		return moneyFormat.format(amount) + " (" + moneyFormat.format(percent)
				+ ")";
	}

	private String displayMoney(double amount) {
		return moneyFormat.format(amount);
	}

}
