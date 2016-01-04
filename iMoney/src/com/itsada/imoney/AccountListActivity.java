package com.itsada.imoney;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.genaral.DateTime;
import com.itsada.framework.models.Configuration;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.views.AccountTypeListView;
import com.itsada.imoney.views.MainMenu;
import com.itsada.management.Format;

@SuppressLint("InflateParams")
public class AccountListActivity extends BaseActivity {

	private Configuration configuration;
	private GraphicalView mGraphView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.account_list_layout);

		setActionBar();
		onInit();
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
								AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
										getApplicationContext(),
										App.configuration.getLocale());
//								App.accountTypes = accountTypeRepository
//										.getAll();
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
		} else if (requestCode == App.CREATE_TRANSACTION_CATEGORY_REQUEST_CODE
				|| requestCode == App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE) {

			String title = requestCode == App.CREATE_TRANSACTION_CATEGORY_REQUEST_CODE ? getResources()
					.getString(R.string.addCategory) : getResources()
					.getString(R.string.editCategory);

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {
					//renderTransactionCategory();
					showDialog(
							"",
							title
									+ " "
									+ getResources()
											.getString(R.string.success));
				} else {
					showDialog(
							"",
							title
									+ " "
									+ getResources()
											.getString(R.string.success));
				}
				// slidingMenu.showContent();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		} else if (requestCode == App.CREATE_TRANSACTION_REQUEST_CODE) {
			String title = requestCode == App.CREATE_TRANSACTION_REQUEST_CODE ? getResources()
					.getString(R.string.addTransaction) : getResources()
					.getString(R.string.editTransaction);

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {
					//renderTransactionCategory();
					showDialog(
							"",
							title
									+ " "
									+ getResources()
											.getString(R.string.success));
					onCreate(null);
				} else {
					showDialog(
							"",
							title
									+ " "
									+ getResources()
											.getString(R.string.success));
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		} else if (requestCode == App.EDIT_TEMPLATE_REQUEST_CODE) {

			String messageSuccess = getResources().getString(
					R.string.editTemplateSuccess);
			String messageFail = getResources().getString(
					R.string.editTemplateFail);

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {
					//renderTransactionCategory();
					showDialog("", messageSuccess);
					onCreate(null);
				} else {
					showDialog("", messageFail);
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		} else if (requestCode == App.UPDATE_ACCOUNT_TYPE_REQUEST_CODE) {

			String messageSuccess = getResources().getString(
					R.string.updateAccounTypeSuccess);
			String messageFail = getResources().getString(
					R.string.updateAccounTypeFail);

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {
					showDialog("", messageSuccess);
					onCreate(null);
				} else {
					showDialog("", messageFail);
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

	}

	@Override
	protected void onInit() {
		TAG = AccountListActivity.class.getName();

		configuration = App.configuration;

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		TextView tvAssetSummary = (TextView) findViewById(R.id.tvAssetSummary);
		TextView tvLiabilitiesSummary = (TextView) findViewById(R.id.tvLiabilitiesSummary);
		TextView tvSumBalance = (TextView) findViewById(R.id.tvSumBalance);
		tvSumBalance.setPaintFlags(tvSumBalance.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				getApplicationContext(), App.configuration.getLocale());
		AccountTypeListView accountTypeListView = null;

		if (configuration == null)
			accountTypeListView = new AccountTypeListView(
					getApplicationContext(), this,
					accountTypeRepository.getAll(configuration.getLocale()));
		else
			accountTypeListView = new AccountTypeListView(
					getApplicationContext(), this,
					accountTypeRepository.getAll(configuration.getLocale()));

		accountTypeListView.onBindView(root);

		((ImageView) findViewById(R.id.imgWithdraw))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								CreateTransactionEmptryActivity.class);
						startActivityForResult(intent,
								App.CREATE_TRANSACTION_REQUEST_CODE);
					}
				});

		tvSumBalance.setTextColor(accountTypeListView.sum > 0 ? Color.rgb(0,
				100, 00) : Color.rgb(200, 0, 0));
		tvLiabilitiesSummary.setTextColor(Color.rgb(200, 0, 0));

		tvAssetSummary.setText(Format.getInstance(this).getMoneyFormat()
				.format(accountTypeListView.assetSum));
		tvLiabilitiesSummary.setText(Format.getInstance(this).getMoneyFormat()
				.format(accountTypeListView.liabilitiesSum));
		tvSumBalance.setText(Format
				.getInstance(this)
				.getMoneyFormat()
				.format(accountTypeListView.assetSum
						- accountTypeListView.liabilitiesSum));

		if (slidingMainMenuLeft == null) {
			slidingMainMenuLeft();
			updateMenu();
		} else {
			updateMenu();
		}

		loadGraph();
	}

	private double getTransactionIncome(Date from, Date to) {
		TransactionRepository repository = new TransactionRepository(
				getApplicationContext(), App.configuration.getLocale());
		return repository.getAmountGroupByIncomes(from.getTime(), to.getTime(),
				false);
	}

	private double getTransactionExpends(Date from, Date to) {
		TransactionRepository repository = new TransactionRepository(
				getApplicationContext(), App.configuration.getLocale());
		return repository.getAmountGroupByExpends(from.getTime(), to.getTime(),
				false);
	}

	private void loadGraph() {

		RelativeLayout layoutGraph = (RelativeLayout) findViewById(R.id.graph);

		Date from6 = DateTime.getFirstDateOfCurrentMonth();
		Date to6 = DateTime.getLastDateOfCurrentMonth();
		Date from5 = DateTime.getPreviousFirstDateOfCurrentMonth(1);
		Date to5 = DateTime.getPreviousLastDateOfCurrentMonth(1);
		Date from4 = DateTime.getPreviousFirstDateOfCurrentMonth(2);
		Date to4 = DateTime.getPreviousLastDateOfCurrentMonth(2);
		Date from3 = DateTime.getPreviousFirstDateOfCurrentMonth(3);
		Date to3 = DateTime.getPreviousLastDateOfCurrentMonth(3);
		Date from2 = DateTime.getPreviousFirstDateOfCurrentMonth(4);
		Date to2 = DateTime.getPreviousLastDateOfCurrentMonth(4);
		Date from1 = DateTime.getPreviousFirstDateOfCurrentMonth(5);
		Date to1 = DateTime.getPreviousLastDateOfCurrentMonth(5);

		Log.d("from6", String.valueOf(from6.toString()));
		Log.d("to6", String.valueOf(to6.toString()));
		Log.d("from5", String.valueOf(from5.toString()));
		Log.d("to5", String.valueOf(to5.toString()));

		ArrayList<TempTransaction> transactionIncomes = new ArrayList<TempTransaction>();
		ArrayList<TempTransaction> transactionExpands = new ArrayList<TempTransaction>();

		SimpleDateFormat mFormat = new SimpleDateFormat("MMM");

		transactionIncomes.add(new TempTransaction(mFormat.format(from1),
				getTransactionIncome(from1, to1)));
		transactionIncomes.add(new TempTransaction(mFormat.format(from2),
				getTransactionIncome(from2, to2)));
		transactionIncomes.add(new TempTransaction(mFormat.format(from3),
				getTransactionIncome(from3, to3)));
		transactionIncomes.add(new TempTransaction(mFormat.format(from4),
				getTransactionIncome(from4, to4)));
		transactionIncomes.add(new TempTransaction(mFormat.format(from5),
				getTransactionIncome(from5, to5)));
		transactionIncomes.add(new TempTransaction(mFormat.format(from6),
				getTransactionIncome(from6, to6)));

		transactionExpands.add(new TempTransaction(mFormat.format(from1),
				getTransactionExpends(from1, to1)));
		transactionExpands.add(new TempTransaction(mFormat.format(from2),
				getTransactionExpends(from2, to2)));
		transactionExpands.add(new TempTransaction(mFormat.format(from3),
				getTransactionExpends(from3, to3)));
		transactionExpands.add(new TempTransaction(mFormat.format(from4),
				getTransactionExpends(from4, to4)));
		transactionExpands.add(new TempTransaction(mFormat.format(from5),
				getTransactionExpends(from5, to5)));
		transactionExpands.add(new TempTransaction(mFormat.format(from6),
				getTransactionExpends(from6, to6)));

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries xySerieIncome = null;
		XYSeries xySerieExpands = null;

		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXTitle("Category");
		multiRenderer.setYTitle("Amount");

		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setBackgroundColor(Color.WHITE);
		multiRenderer.setMarginsColor(Color.WHITE);
		multiRenderer.setZoomEnabled(true);
		multiRenderer.setBarSpacing(1);
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });
		multiRenderer.setBarWidth(getWidth(12));

		// multiRenderer.setAxisTitleTextSize(16);
		// multiRenderer.setChartTitleTextSize(20);
		// multiRenderer.setLabelsTextSize(15);
		// multiRenderer.setLegendTextSize(15);
		multiRenderer.setChartTitle("Expanse Chart");
		multiRenderer.setShowLegend(false);
		multiRenderer.setShowAxes(true);
		multiRenderer.setShowLabels(true);
		multiRenderer.setShowGrid(true);
		multiRenderer.setShowCustomTextGrid(false);
		// multiRenderer.setYAxisMin(getMin(transactionIncomes));
		multiRenderer
				.setYAxisMax(getMax(transactionIncomes, transactionExpands));
		multiRenderer.setXAxisMin(0);
		multiRenderer.setXAxisMax(6);

		xySerieIncome = new XYSeries("In");
		xySerieExpands = new XYSeries("Ex");
		for (int i = 0; i < transactionIncomes.size(); i++) {
			TempTransaction tIncome = transactionIncomes.get(i);

			Log.d("tIncome",
					tIncome.getName() + ":"
							+ String.valueOf(tIncome.getAmount()));

			xySerieIncome.add(i, tIncome.getAmount());

			TempTransaction tExpands = transactionExpands.get(i);
			Log.d("tExpands",
					tExpands.getName() + ":"
							+ String.valueOf(tExpands.getAmount()));
			xySerieExpands.add(i, tExpands.getAmount());

			multiRenderer.addXTextLabel(i, tIncome.getName());
		}

		dataset.addSeries(xySerieIncome);
		dataset.addSeries(xySerieExpands);

		XYSeriesRenderer rendererIncome = new XYSeriesRenderer();
		rendererIncome.setColor(Color.GREEN);
		rendererIncome.setDisplayChartValues(true);
		XYSeriesRenderer rendererExpends = new XYSeriesRenderer();
		rendererExpends.setColor(Color.RED);
		rendererExpends.setDisplayChartValues(true);

		multiRenderer.addSeriesRenderer(rendererIncome);
		multiRenderer.addSeriesRenderer(rendererExpends);

		mGraphView = ChartFactory.getBarChartView(getBaseContext(), dataset,
				multiRenderer, Type.DEFAULT);
		layoutGraph.addView(mGraphView);
	}

	public float getWidth(int size) {

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth(); // deprecated
		float w = width / size;

		return w;
	}

	public double getMax(ArrayList<TempTransaction> transactionIncome,
			ArrayList<TempTransaction> transactionExpends) {

		double max = 0;
		for (int i = 0; i < transactionIncome.size(); i++) {
			TempTransaction t = transactionIncome.get(i);
			max = t.getAmount() > max ? t.getAmount() : max;
		}
		for (int i = 0; i < transactionExpends.size(); i++) {
			TempTransaction t = transactionExpends.get(i);
			max = t.getAmount() > max ? t.getAmount() : max;
		}

		return max;
	}

	public double getMin(ArrayList<Transaction> transactions) {

		double min = 0;
		for (int i = 0; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			min = t.getAmount() < min ? t.getAmount() : min;
		}

		return min;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();

		if (App.isCreateTransaction) {
			App.isCreateTransaction = false;
			onCreate(null);
		}

		if (App.isUpdateTransaction) {
			App.isUpdateTransaction = false;
			onCreate(null);
		}

		if (App.isUpdateTransactionCatrgory) {
			App.isUpdateTransactionCatrgory = false;
			onCreate(null);
		}
		
		if(App.isUpdateAccount){
			App.isUpdateAccount = false;
			onCreate(null);
		}
		
		if(App.isUpdateLangage){
			App.isUpdateLangage = false;
			
			super.onCreate(null);
			onCreate(null);
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		System.gc();
	}

	public class TempTransaction {
		private double amount;
		private String name;

		public TempTransaction(String name, double amount) {
			this.amount = amount;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public double getAmount() {
			return this.amount;
		}
	}
}
