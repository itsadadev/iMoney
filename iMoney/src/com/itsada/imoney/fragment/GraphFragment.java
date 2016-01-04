package com.itsada.imoney.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionFragmentActivity;
import com.itsada.imoney.views.MonthDialog;
import com.itsada.imoney.views.MonthDialog.CallBack;

@SuppressLint("InflateParams")
public class GraphFragment extends BaseFragemnt implements OnClickListener {

	// private ActionBar actionBar;
	private Account currentAccount;
	private Button btIcome, btExpande;
	private ImageView imgBtPreviousMonth;
	private ImageView imgBtNextMonth;
	// private ImageView imgGraph;
	private TextView tvMonthHeader;
	ArrayList<Transaction> transactions;

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

	private View root;
	private TransactionFragmentActivity activity;
	private int page;
	private String title;
	private static GraphFragment graphFragment;

	public static GraphFragment newInstance(int page, String title) {

		if (graphFragment == null) {
			graphFragment = new GraphFragment();
			Bundle args = new Bundle();
			args.putInt("page", page);
			args.putString("title", title);
			graphFragment.setArguments(args);
		}
		return graphFragment;
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
				R.layout.transaction_header_graph_layout, container, false);
		root = rootView;

		activity = (TransactionFragmentActivity) getActivity();
		onInit();

		return rootView;
	}

	@Override
	protected void onInit() {

		imgBtPreviousMonth = (ImageView) root
				.findViewById(R.id.imgBtPreviousMonth);
		imgBtPreviousMonth.setOnClickListener(this);
		imgBtNextMonth = (ImageView) root.findViewById(R.id.imgBtNextMonth);
		imgBtNextMonth.setOnClickListener(this);

		tvNoTransactionIncome = (TextView) root
				.findViewById(R.id.tvNoTransactionIncome);
		tvNoTransactionExpanse = (TextView) root
				.findViewById(R.id.tvNoTransactionExpanse);

		layoutIncome = (RelativeLayout) root
				.findViewById(R.id.graph_containerIncome);
		layoutExpanse = (RelativeLayout) root
				.findViewById(R.id.graph_containerExpanse);

		layoutRenderIncome = (RelativeLayout) root
				.findViewById(R.id.layoutRenderIncome);
		layoutRenderExpanse = (RelativeLayout) root
				.findViewById(R.id.layoutRenderExpanse);

		layoutLabelIncome = (LinearLayout) root
				.findViewById(R.id.layoutLabelIncome);
		layoutLabelExpanse = (LinearLayout) root
				.findViewById(R.id.layoutLabelExpanse);

		btIcome = (Button) root.findViewById(R.id.btIncome);
		btExpande = (Button) root.findViewById(R.id.btExpanse);

		btIcome.setTag(R.drawable.troggle_on_active_shape);
		btExpande.setTag(R.drawable.troggle_off_normal_shape);

		tvTotalMonth = (TextView) root.findViewById(R.id.tvTotalMonth);
		tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);

		tvMonthHeader.setText(monthFormat.format(((App) getActivity()
				.getApplication()).from));

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
				MonthDialog m = new MonthDialog(getActivity());
				m.show(app.from);

				m.callback = new CallBack() {

					@Override
					public void updateView() {

						if (!(f.equals(app.from) && t.equals(app.to))) {
							TransactionGroupFactory();
						}
					}
				};
			}
		});

	}

	@Override
	public void setActionBar() {

		super.setActionBar();
	}

	private void clearChild(ViewGroup layoutLabel) {
		for (int i = 0; i < layoutLabel.getChildCount(); i++)
			layoutLabel.removeView(layoutLabel.getChildAt(i));

		if (layoutLabel.getChildCount() > 0)
			clearChild(layoutLabel);
	}

	public void loadDataGraphIncome() {

		TransactionRepository transactionRepository = new TransactionRepository(
				getActivity().getApplicationContext(),
				App.configuration.getLocale());


		transactions = transactionRepository.getTransactionGroupByIncomes(
				activity.currentAccount.getId(), app.from.getTime(),
				app.to.getTime(), false);

		int length = transactions.size();

		String[] colors = { "#ff4444", "#99cc00", "#aa66cc", "#33b5e5",
				"#ffbb33", "#ff3344", "#99dd00", "#aa22cc", "#32b5e5",
				"#ffbb11" };

		double sum = 0;

		if (activity.imgGraph.getTag().equals(
				R.drawable.ic_equalizer_white_24dp)) {
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

					template = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.label_graph_template, null);
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
				mGraphViewIncome = ChartFactory.getPieChartView(getActivity()
						.getApplicationContext(), series, renderer);

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
				multiRenderer.setYAxisMin(getMin(transactions));
				multiRenderer.setYAxisMax(getMax(transactions));
				multiRenderer.setXAxisMin(0);
				multiRenderer.setXAxisMax(transactions.size() - 1);

				for (int i = 0; i < transactions.size(); i++) {
					Transaction t = transactions.get(i);
					xySerie = new XYSeries(t.getTransactionCategory().getName());
					xySerie.add(i, t.getAmount());
					dataset.addSeries(xySerie);

					// multiRenderer.addXTextLabel(i, t.getTransactionCategory()
					// .getName());

					XYSeriesRenderer renderer = new XYSeriesRenderer();
					// renderer.setFillPoints(true);
					// renderer.setLineWidth(2);
					renderer.setColor(Color.parseColor(colors[i]));
					renderer.setDisplayChartValues(true);

					multiRenderer.addSeriesRenderer(renderer);

					sum += t.getAmount();

					template = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.label_graph_template, null);
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

				mGraphViewIncome = ChartFactory.getBarChartView(getActivity()
						.getApplicationContext(), dataset, multiRenderer,
						Type.STACKED);
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
				getActivity().getApplicationContext(),
				App.configuration.getLocale());

		transactions = transactionRepository.getTransactionGroupByExpends(
				activity.currentAccount.getId(), app.from.getTime(),
				app.to.getTime(), false);
		int length = transactions.size();

		Log.d("", String.valueOf(transactions.size()));

		String[] colors = { "#ff4444", "#99cc00", "#aa66cc", "#33b5e5",
				"#ffbb33", "#ff3344", "#99dd00", "#aa22cc", "#32b5e5",
				"#ffbb11" };

		double sum = 0;

		if (activity.imgGraph.getTag().equals(
				R.drawable.ic_equalizer_white_24dp)) {
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

					template = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.label_graph_template, null);
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
				mGraphViewExpanse = ChartFactory.getPieChartView(getActivity()
						.getApplicationContext(), series, renderer);

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
				multiRenderer.setYAxisMin(getMin(transactions));
				multiRenderer.setYAxisMax(getMax(transactions));
				multiRenderer.setXAxisMin(0);
				multiRenderer.setXAxisMax(transactions.size() - 1);

				for (int i = 0; i < transactions.size(); i++) {
					Transaction t = transactions.get(i);
					xySerie = new XYSeries(t.getTransactionCategory().getName());
					xySerie.add(i, t.getAmount());
					dataset.addSeries(xySerie);

					// multiRenderer.addXTextLabel(i, t.getTransactionCategory()
					// .getName());

					XYSeriesRenderer renderer = new XYSeriesRenderer();
					// renderer.setFillPoints(true);
					// renderer.setLineWidth(2);
					renderer.setColor(Color.parseColor(colors[i]));
					renderer.setDisplayChartValues(true);

					multiRenderer.addSeriesRenderer(renderer);

					sum += t.getAmount();

					template = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.label_graph_template, null);
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

				mGraphViewExpanse = ChartFactory.getBarChartView(getActivity()
						.getApplicationContext(), dataset, multiRenderer,
						Type.STACKED);
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

	public float getWidth(int size) {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth(); // deprecated
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

			tvMonthHeader.setText(monthFormat.format(app.from));
			TransactionGroupFactory();

			break;

		case R.id.imgBtNextMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			tvMonthHeader.setText(monthFormat.format(app.from));

			TransactionGroupFactory();
			break;

		case R.id.imgHome:
			// case R.id.layoutBack:

			getActivity().finish();
			break;

		case R.id.layoutGraph:
		case R.id.imgGraph:

			// if (activity.imgGraph.getTag().equals(
			// R.drawable.ic_data_usage_white_24dp)) {
			// activity.imgGraph.setTag(R.drawable.ic_equalizer_white_24dp);
			// activity.imgGraph
			// .setBackgroundResource(R.drawable.ic_equalizer_white_24dp);
			// } else {
			// activity.imgGraph.setTag(R.drawable.ic_data_usage_white_24dp);
			// activity.imgGraph
			// .setBackgroundResource(R.drawable.ic_data_usage_white_24dp);
			// }
			// TransactionGroupFactory();
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
			tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);
		tvMonthHeader.setText(monthFormat.format(app.from));

		// if (btAll.getTag().equals(R.drawable.troggle_on_active_shape)) {
		//
		// Log.d("TransactionGroupFactory", "btAll");
		// loadDataGraphAll();
		// } else
		if (btIcome.getTag().equals(R.drawable.troggle_on_active_shape)) {
			loadDataGraphIncome();
		} else if (btExpande.getTag().equals(
				R.drawable.troggle_off_active_shape)) {
			loadDataGraphExpande();
		}

	}

	public void switchGraph(ImageView imgGraph) {
		if (imgGraph.getTag().equals(R.drawable.ic_data_usage_white_24dp)) {
			imgGraph.setTag(R.drawable.ic_equalizer_white_24dp);
			imgGraph.setBackgroundResource(R.drawable.ic_equalizer_white_24dp);
		} else {
			imgGraph.setTag(R.drawable.ic_data_usage_white_24dp);
			imgGraph.setBackgroundResource(R.drawable.ic_data_usage_white_24dp);
		}

		TransactionGroupFactory();
	}

}
