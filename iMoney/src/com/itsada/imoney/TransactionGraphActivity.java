package com.itsada.imoney;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.views.MonthDialog;
import com.itsada.imoney.views.MonthDialog.CallBack;

@SuppressLint("SimpleDateFormat")
public class TransactionGraphActivity extends BaseActivity implements
		OnClickListener {

	private Account currentAccount;
	private Button btIcome, btExpande;
	private ImageView imgBtPreviousMonth;
	private ImageView imgBtNextMonth;
	private ImageView imgGraph;
	private App app;
	private TextView tvMonthHeader;
	ArrayList<Transaction> transactions;
	private TextView tvAccountName;
	private TextView tvAccountBalance;
	private TextView tvTotalMonth;
	LinearLayout template;
	LinearLayout bg1, bg2;
	TextView tv1, tv2;
	RelativeLayout layoutIncome, layoutExpanse;
	RelativeLayout layoutRenderIncome, layoutRenderExpanse;
	LinearLayout layoutLabelIncome, layoutLabelExpanse;
	LinearLayout layoutGraph;
	GraphicalView mGraphViewIncome = null;
	GraphicalView mGraphViewExpanse = null;
	TextView tvNoTransactionIncome, tvNoTransactionExpanse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.transaction_header_graph_layout);

		AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(),
				App.configuration.getLocale());
		currentAccount = accountRepository.getById(getIntent().getIntExtra(
				App.ACCOUNT_ID, 0));

		setActionBar();
		onInit();
	}

	@Override
	protected void onInit() {

		app = (App) getApplication();

		imgBtPreviousMonth = (ImageView) findViewById(R.id.imgBtPreviousMonth);
		imgBtPreviousMonth.setOnClickListener(this);
		imgBtNextMonth = (ImageView) findViewById(R.id.imgBtNextMonth);
		imgBtNextMonth.setOnClickListener(this);

		tvNoTransactionIncome = (TextView) findViewById(R.id.tvNoTransactionIncome);
		tvNoTransactionExpanse = (TextView) findViewById(R.id.tvNoTransactionExpanse);

		layoutIncome = (RelativeLayout) findViewById(R.id.graph_containerIncome);
		layoutExpanse = (RelativeLayout) findViewById(R.id.graph_containerExpanse);

		layoutRenderIncome = (RelativeLayout) findViewById(R.id.layoutRenderIncome);
		layoutRenderExpanse = (RelativeLayout) findViewById(R.id.layoutRenderExpanse);

		layoutLabelIncome = (LinearLayout) findViewById(R.id.layoutLabelIncome);
		layoutLabelExpanse = (LinearLayout) findViewById(R.id.layoutLabelExpanse);

		btIcome = (Button) findViewById(R.id.btIncome);
		btExpande = (Button) findViewById(R.id.btExpanse);

		btIcome.setTag(R.drawable.troggle_on_active_shape);
		btExpande.setTag(R.drawable.troggle_off_normal_shape);

		tvTotalMonth = (TextView) findViewById(R.id.tvTotalMonth);
		tvMonthHeader = (TextView) findViewById(R.id.tvMonthHeader);

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			tvMonthHeader.setText(monthYearEnFormat
					.format(((App) getApplication()).from));
		else
			tvMonthHeader.setText(monthYearThFormat
					.format(((App) getApplication()).from));

		loadDataGraphIncome();

		btIcome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIcome.setBackgroundResource(R.drawable.troggle_on_active_shape);
				btExpande
						.setBackgroundResource(R.drawable.troggle_off_normal_shape);

				Log.d("", "btIcome" + String.valueOf(btIcome.getTag()) + ":"
						+ R.drawable.troggle_on_normal_shape);

				if (btIcome.getTag().equals(R.drawable.troggle_on_normal_shape)) {
					loadDataGraphIncome();
					btIcome.setTag(R.drawable.troggle_on_active_shape);
					btExpande.setTag(R.drawable.troggle_off_normal_shape);
				}
			}
		});
		btExpande.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIcome.setBackgroundResource(R.drawable.troggle_on_normal_shape);
				btExpande
						.setBackgroundResource(R.drawable.troggle_off_active_shape);

				Log.d("", "btExpande" + String.valueOf(btExpande.getTag())
						+ ":" + R.drawable.troggle_off_normal_shape);
				if (btExpande.getTag().equals(
						R.drawable.troggle_off_normal_shape)) {
					loadDataGraphExpande();
					btIcome.setTag(R.drawable.troggle_on_normal_shape);
					btExpande.setTag(R.drawable.troggle_off_active_shape);
				}
			}
		});

		tvMonthHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Date f = (Date) app.from.clone();
				final Date t = (Date) app.to.clone();
				MonthDialog m = new MonthDialog(TransactionGraphActivity.this);
				m.show(app.from);

				m.callback = new CallBack() {

					@Override
					public void updateView() {

						Log.d(TAG, app.from.toString());
						Log.d(TAG, app.to.toString());

						if (!(f.equals(app.from) && t.equals(app.to))) {
							TransactionGroupFactory();
						}
					}
				};
			}
		});

	}

	public void loadDataGraphIncome() {

		Log.d("", "loadDataGraphIncome");
		TransactionRepository transactionRepository = new TransactionRepository(
				getApplicationContext(),
				App.configuration.getLocale());

		transactions = transactionRepository.getTransactionGroupByIncomes(
				currentAccount.getId(), app.from.getTime(), app.to.getTime(),
				false);
		int length = transactions.size();

		String[] colors = { "#ff4444", "#99cc00", "#aa66cc", "#33b5e5",
				"#ffbb33", "#ff3344", "#99dd00", "#aa22cc", "#32b5e5",
				"#ffbb11" };

		double sum = 0;

		if (imgGraph.getTag().equals(R.drawable.ic_equalizer_white_24dp)) {
			CategorySeries series = new CategorySeries("");

			DefaultRenderer renderer = new DefaultRenderer();

			for (int i = 0; i < layoutRenderIncome.getChildCount(); i++) {
				if (layoutRenderIncome.getChildAt(i) instanceof GraphicalView)
					layoutRenderIncome.removeView(layoutRenderIncome
							.getChildAt(i));
			}

			// clearChild(layoutRenderIncome);
			clearChild(layoutLabelIncome);

			LinearLayout template;
			TextView tvCategoryName, tvValue;
			ImageView imgColor;

			if (length > 0) {
				for (int i = 0; i < length; i++) {

					Transaction t = transactions.get(i);

					sum += t.getAmount();

					series.add(t.getTransactionCategory().getName(), Float
							.parseFloat(String.valueOf(transactions.get(i)
									.getAmount())));
					SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
					seriesRenderer.setColor(Color.parseColor(colors[i]));

					renderer.addSeriesRenderer(seriesRenderer);

					template = (LinearLayout) getLayoutInflater().inflate(
							R.layout.label_graph_template, null);
					imgColor = (ImageView) template.findViewById(R.id.imgColor);
					tvCategoryName = (TextView) template
							.findViewById(R.id.tvCategoryName);
					tvValue = (TextView) template.findViewById(R.id.tvValue);

					imgColor.setBackgroundColor(Color.parseColor(colors[i]));
					tvCategoryName.setText(t.getTransactionCategory().getName()
							+ " " + getPercent(transactions, t));
					tvValue.setText(moneyFormat.format(t.getAmount()));

					layoutLabelIncome.addView(template);
				}

				renderer.setChartTitleTextSize(60);
				renderer.setChartTitle("");
				renderer.setLabelsTextSize(30);
				renderer.setLabelsColor(Color.GRAY);
				renderer.setLegendTextSize(30);

				renderer.setShowLegend(false);
				mGraphViewIncome = ChartFactory.getPieChartView(
						getApplicationContext(), series, renderer);

				tvNoTransactionIncome.setVisibility(View.GONE);
				layoutRenderIncome.addView(mGraphViewIncome);

				Log.d("", "if");

			} else {
				series.add("", 0);
				SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
				seriesRenderer.setColor(Color.GRAY);
				renderer.addSeriesRenderer(seriesRenderer);

				tvNoTransactionIncome.setVisibility(View.VISIBLE);
				tvNoTransactionIncome.setText(getResources().getString(
						R.string.noTransaction));

				Log.d("", "else");
			}

			layoutIncome.setVisibility(View.VISIBLE);
			layoutExpanse.setVisibility(View.GONE);
		} else {

			for (int i = 0; i < layoutRenderIncome.getChildCount(); i++) {
				if (layoutRenderIncome.getChildAt(i) instanceof GraphicalView)
					layoutRenderIncome.removeView(layoutRenderIncome
							.getChildAt(i));

			}

			// clearChild(layoutRenderIncome);
			clearChild(layoutLabelIncome);

			LinearLayout template;
			TextView tvCategoryName, tvValue;
			ImageView imgColor;

			if (length > 0) {

				XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
				XYSeries xySerie = null;

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
				 multiRenderer.setBarSpacing(1.0);
				multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });
				multiRenderer.setBarWidth(getWidth(length));

//				multiRenderer.setAxisTitleTextSize(16);
//				multiRenderer.setChartTitleTextSize(20);
//				multiRenderer.setLabelsTextSize(15);
//				multiRenderer.setLegendTextSize(15);
				multiRenderer.setChartTitle("Expanse Chart");
				multiRenderer.setShowLegend(false);
				multiRenderer.setShowAxes(true);
				multiRenderer.setShowLabels(true);
				multiRenderer.setShowGrid(true);
				multiRenderer.setShowCustomTextGrid(false);
				multiRenderer.setYAxisMin(getMin(transactions));
				multiRenderer.setYAxisMax(getMax(transactions));
				multiRenderer.setXAxisMin(0);
				multiRenderer.setXAxisMax(transactions.size() - 1);

				for (int i = 0; i < transactions.size(); i++) {
					Transaction t = transactions.get(i);
					xySerie = new XYSeries(t.getTransactionCategory().getName());
					xySerie.add(i, t.getAmount());
					dataset.addSeries(xySerie);

//					multiRenderer.addXTextLabel(i, t.getTransactionCategory()
//							.getName());

					XYSeriesRenderer renderer = new XYSeriesRenderer();
					// renderer.setFillPoints(true);
					// renderer.setLineWidth(2);
					renderer.setColor(Color.parseColor(colors[i]));
					renderer.setDisplayChartValues(true);

					multiRenderer.addSeriesRenderer(renderer);

					sum += t.getAmount();

					template = (LinearLayout) getLayoutInflater().inflate(
							R.layout.label_graph_template, null);
					imgColor = (ImageView) template.findViewById(R.id.imgColor);
					tvCategoryName = (TextView) template
							.findViewById(R.id.tvCategoryName);
					tvValue = (TextView) template.findViewById(R.id.tvValue);

					imgColor.setBackgroundColor(Color.parseColor(colors[i]));
					tvCategoryName.setText(t.getTransactionCategory().getName()
							+ " " + getPercent(transactions, t));
					tvValue.setText(moneyFormat.format(t.getAmount()));

					layoutLabelIncome.addView(template);
				}

				mGraphViewIncome = ChartFactory.getBarChartView(
						getBaseContext(), dataset, multiRenderer, Type.STACKED);
				// mGraphViewIncome.setBackgroundColor(Color.WHITE);
				layoutRenderIncome.addView(mGraphViewIncome);
			}

			else {

				tvNoTransactionIncome.setVisibility(View.VISIBLE);
				tvNoTransactionIncome.setText(getResources().getString(
						R.string.noTransaction));

				Log.d("", "else");
			}

			layoutIncome.setVisibility(View.VISIBLE);
			layoutExpanse.setVisibility(View.GONE);
		}

		tvTotalMonth.setText(moneyFormat.format(sum));
	}

	public void loadDataGraphExpande() {

		Log.d("", "loadDataGraphExpande");
		TransactionRepository transactionRepository = new TransactionRepository(
				getApplicationContext(),
				App.configuration.getLocale());

		transactions = transactionRepository.getTransactionGroupByExpends(
				currentAccount.getId(), app.from.getTime(), app.to.getTime(),
				false);
		int length = transactions.size();

		Log.d("", String.valueOf(transactions.size()));

		String[] colors = { "#ff4444", "#99cc00", "#aa66cc", "#33b5e5",
				"#ffbb33", "#ff3344", "#99dd00", "#aa22cc", "#32b5e5",
				"#ffbb11" };

		double sum = 0;

		if (imgGraph.getTag().equals(R.drawable.ic_equalizer_white_24dp)) {
			CategorySeries series = new CategorySeries("");

			DefaultRenderer renderer = new DefaultRenderer();

			for (int i = 0; i < layoutRenderExpanse.getChildCount(); i++) {
				if (layoutRenderExpanse.getChildAt(i) instanceof GraphicalView)
					layoutRenderExpanse.removeView(layoutRenderExpanse
							.getChildAt(i));
			}

			// clearChild(layoutRenderExpanse);
			clearChild(layoutLabelExpanse);

			LinearLayout template;
			TextView tvCategoryName, tvValue;
			ImageView imgColor;

			if (length > 0) {
				for (int i = 0; i < length; i++) {

					Transaction t = transactions.get(i);

					sum += t.getAmount();

					series.add(t.getTransactionCategory().getName(), Float
							.parseFloat(String.valueOf(transactions.get(i)
									.getAmount())));
					SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
					seriesRenderer.setColor(Color.parseColor(colors[i]));

					renderer.addSeriesRenderer(seriesRenderer);

					template = (LinearLayout) getLayoutInflater().inflate(
							R.layout.label_graph_template, null);
					imgColor = (ImageView) template.findViewById(R.id.imgColor);
					tvCategoryName = (TextView) template
							.findViewById(R.id.tvCategoryName);
					tvValue = (TextView) template.findViewById(R.id.tvValue);

					imgColor.setBackgroundColor(Color.parseColor(colors[i]));
					tvCategoryName.setText(t.getTransactionCategory().getName()
							+ " " + getPercent(transactions, t));
					tvValue.setText(moneyFormat.format(t.getAmount()));

					layoutLabelExpanse.addView(template);
				}

				renderer.setChartTitleTextSize(60);
				renderer.setChartTitle("");
				renderer.setLabelsTextSize(30);
				renderer.setLabelsColor(Color.GRAY);
				renderer.setLegendTextSize(30);

				renderer.setShowLegend(false);
				mGraphViewExpanse = ChartFactory.getPieChartView(
						getApplicationContext(), series, renderer);

				tvNoTransactionExpanse.setVisibility(View.GONE);
				layoutRenderExpanse.addView(mGraphViewExpanse);

				Log.d("", "if");

			} else {
				series.add("", 0);
				SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
				seriesRenderer.setColor(Color.GRAY);
				renderer.addSeriesRenderer(seriesRenderer);

				tvNoTransactionExpanse.setVisibility(View.VISIBLE);
				tvNoTransactionExpanse.setText(getResources().getString(
						R.string.noTransaction));

				Log.d("", "else");
			}

			layoutIncome.setVisibility(View.GONE);
			layoutExpanse.setVisibility(View.VISIBLE);
		} else {

			for (int i = 0; i < layoutRenderExpanse.getChildCount(); i++) {
				if (layoutRenderExpanse.getChildAt(i) instanceof GraphicalView)
					layoutRenderExpanse.removeView(layoutRenderExpanse
							.getChildAt(i));
			}

			// clearChild(layoutRenderExpanse);
			clearChild(layoutLabelExpanse);

			LinearLayout template;
			TextView tvCategoryName, tvValue;
			ImageView imgColor;

			if (length > 0) {

				XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
				XYSeries xySerie = null;

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
				 multiRenderer.setBarSpacing(1.0);
				multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });
				multiRenderer.setBarWidth(getWidth(length));

//				multiRenderer.setAxisTitleTextSize(16);
//				multiRenderer.setChartTitleTextSize(20);
//				multiRenderer.setLabelsTextSize(15);
//				multiRenderer.setLegendTextSize(15);
				multiRenderer.setChartTitle("Expanse Chart");
				multiRenderer.setShowLegend(false);
				multiRenderer.setShowAxes(true);
				multiRenderer.setShowLabels(true);
				multiRenderer.setShowGrid(true);
				multiRenderer.setShowCustomTextGrid(false);
				multiRenderer.setYAxisMin(getMin(transactions));
				multiRenderer.setYAxisMax(getMax(transactions));
				multiRenderer.setXAxisMin(0);
				multiRenderer.setXAxisMax(transactions.size() - 1);

				for (int i = 0; i < transactions.size(); i++) {
					Transaction t = transactions.get(i);
					xySerie = new XYSeries(t.getTransactionCategory().getName());
					xySerie.add(i, t.getAmount());
					dataset.addSeries(xySerie);

//					multiRenderer.addXTextLabel(i, t.getTransactionCategory()
//							.getName());

					XYSeriesRenderer renderer = new XYSeriesRenderer();
					// renderer.setFillPoints(true);
					// renderer.setLineWidth(2);
					renderer.setColor(Color.parseColor(colors[i]));
					renderer.setDisplayChartValues(true);

					multiRenderer.addSeriesRenderer(renderer);

					sum += t.getAmount();

					template = (LinearLayout) getLayoutInflater().inflate(
							R.layout.label_graph_template, null);
					imgColor = (ImageView) template.findViewById(R.id.imgColor);
					tvCategoryName = (TextView) template
							.findViewById(R.id.tvCategoryName);
					tvValue = (TextView) template.findViewById(R.id.tvValue);

					imgColor.setBackgroundColor(Color.parseColor(colors[i]));
					tvCategoryName.setText(t.getTransactionCategory().getName()
							+ " " + getPercent(transactions, t));
					tvValue.setText(moneyFormat.format(t.getAmount()));

					layoutLabelExpanse.addView(template);
				}

				mGraphViewExpanse = ChartFactory.getBarChartView(
						getBaseContext(), dataset, multiRenderer, Type.STACKED);
				mGraphViewExpanse.setBackgroundColor(Color.WHITE);
				layoutRenderExpanse.addView(mGraphViewExpanse);
			}

			else {
				tvNoTransactionExpanse.setVisibility(View.VISIBLE);
				tvNoTransactionExpanse.setText(getResources().getString(
						R.string.noTransaction));

				Log.d("", "else");
			}

			layoutIncome.setVisibility(View.GONE);
			layoutExpanse.setVisibility(View.VISIBLE);
		}

		tvTotalMonth.setText(moneyFormat.format(sum));
	}
	
	public float getWidth(int size){
		
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display. getWidth();  // deprecated
		float w = width / size;
		
		return w;
	}

	public double getMax(ArrayList<Transaction> transactions) {

		double max = 0;
		for (int i = 0; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
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

	public String getValue(ArrayList<Transaction> transactions, Transaction t) {
		double sum = 0;
		double result = 0;
		for (Transaction transaction : transactions) {
			sum += transaction.getAmount();
		}

		result = (t.getAmount() / sum) * 100;

		// return String.valueOf(t.getTransactionCategory().getName() + " "
		// + t.getAmount())
		// + "(" + moneyFormat.format(result) + "%)";

		return t.getTransactionCategory().getName() + " " + "("
				+ moneyFormat.format(result) + "%)";

	}

	public String getValueOnly(ArrayList<Transaction> transactions,
			Transaction t) {
		double sum = 0;
		double result = 0;
		for (Transaction transaction : transactions) {
			sum += transaction.getAmount();
		}

		result = (t.getAmount() / sum) * 100;

		return String.valueOf(moneyFormat.format(t.getAmount())) + " ("
				+ moneyFormat.format(result) + "%)";
	}

	public String getPercent(ArrayList<Transaction> transactions, Transaction t) {
		double sum = 0;
		double result = 0;
		for (Transaction transaction : transactions) {
			sum += transaction.getAmount();
		}

		result = (t.getAmount() / sum) * 100;

		return "(" + moneyFormat.format(result) + "%)";
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

			if (App.configuration.getLocale().equals(Locale.ENGLISH))
				tvMonthHeader.setText(monthYearEnFormat.format(app.from));
			else
				tvMonthHeader.setText(monthYearThFormat.format(app.from));

			TransactionGroupFactory();

			break;

		case R.id.imgBtNextMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			if (App.configuration.getLocale().equals(Locale.ENGLISH))
				tvMonthHeader.setText(monthYearEnFormat.format(app.from));
			else
				tvMonthHeader.setText(monthYearThFormat.format(app.from));

			TransactionGroupFactory();
			break;

		case R.id.imgHome:
			// case R.id.layoutBack:

			finish();
			break;

		case R.id.layoutGraph:
		case R.id.imgGraph:

			if (imgGraph.getTag().equals(R.drawable.ic_data_usage_white_24dp)) {
				imgGraph.setTag(R.drawable.ic_equalizer_white_24dp);
				imgGraph.setBackgroundResource(R.drawable.ic_equalizer_white_24dp);
			} else {
				imgGraph.setTag(R.drawable.ic_data_usage_white_24dp);
				imgGraph.setBackgroundResource(R.drawable.ic_data_usage_white_24dp);
			}
			TransactionGroupFactory();
			break;

		// case R.id.imgWithdraw:
		// Intent withdraw = new Intent(getApplicationContext(),
		// CreateTransactionActivity.class);
		// withdraw.putExtra(App.TRANSACTION_TYPE, "withdraw");
		// // withdraw.putExtra(
		// // App.ACCOUNT_TYPE_ID,
		// // Integer.parseInt(getIntent().getStringExtra(
		// // App.ACCOUNT_TYPE_ID)));
		// withdraw.putExtra(App.ACCOUNT_ID, currentAccount.getId());
		// withdraw.putExtra(App.ACCOUNT_NAME, currentAccount.getName());
		// withdraw.putExtra(App.ACCOUNT_BALANCE, currentAccount.getBalance());
		//
		// startActivityForResult(withdraw,
		// App.CREATE_TRANSACTION_REQUEST_CODE);
		// // startActivity(withdraw);
		// break;
		}

	}

	public void TransactionGroupFactory() {

		if (tvMonthHeader == null)
			tvMonthHeader = (TextView) findViewById(R.id.tvMonthHeader);

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			tvMonthHeader.setText(monthYearEnFormat
					.format(((App) getApplication()).from));
		else
			tvMonthHeader.setText(monthYearThFormat
					.format(((App) getApplication()).from));

		// Log.d("btAll", String.valueOf(btAll.getTag()));
		Log.d("btIcome", String.valueOf(btIcome.getTag()));
		Log.d("btExpande", String.valueOf(btExpande.getTag()));

		// if (btAll.getTag().equals(R.drawable.troggle_on_active_shape)) {
		//
		// Log.d("TransactionGroupFactory", "btAll");
		// loadDataGraphAll();
		// } else
		if (btIcome.getTag().equals(R.drawable.troggle_on_active_shape)) {
			Log.d("TransactionGroupFactory", "btIcome");
			loadDataGraphIncome();
		} else if (btExpande.getTag().equals(
				R.drawable.troggle_off_active_shape)) {
			Log.d("TransactionGroupFactory", "btExpande");
			loadDataGraphExpande();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == App.CREATE_TRANSACTION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("Success")) {

					showDialog(
							"",
							getResources().getString(R.string.addTransaction)
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

								App.isUpdateTransaction = true;
								onCreate(null);
							}
						}
					};
				} else {
					showDialog(
							"",
							getResources().getString(R.string.addTransaction)
									+ " "
									+ getResources().getString(R.string.fail));
				}

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

	}

	@Override
	protected void setActionBar() {

		try {

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(
					R.layout.custom_actionbar_graph, null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			mCustomView.findViewById(R.id.linearLayoutAccount)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
			// imgHome = (ImageView) findViewById(R.id.imgHome);
			// imgHome.setOnClickListener(this);

			// LinearLayout layoutBack = (LinearLayout)
			// findViewById(R.id.layoutBack);
			// layoutBack.setOnClickListener(this);

			tvAccountName = (TextView) mCustomView
					.findViewById(R.id.tvAccountName);
			tvAccountBalance = (TextView) mCustomView
					.findViewById(R.id.tvAccountBalance);

			layoutGraph = ((LinearLayout) mCustomView
					.findViewById(R.id.layoutGraph));
			layoutGraph.setOnClickListener(this);
			imgGraph = ((ImageView) mCustomView.findViewById(R.id.imgGraph));
			imgGraph.setOnClickListener(this);
			imgGraph.setTag(R.drawable.ic_equalizer_white_24dp);

			if (currentAccount != null) {
				tvAccountName.setText(currentAccount.getName());
				tvAccountBalance.setText(moneyFormat.format(currentAccount
						.getBalance()));
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
