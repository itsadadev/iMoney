package com.itsada.imoney.views;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.imoney.App;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionDetail;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class TransactionGroupByTypeListView {

	private Context context;
	private Activity activity;

	private List<Transaction> transactions;

	private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
//	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");

	// Layout
	private LinearLayout dailyTransactionInflater;
	private LinearLayout dailyLayout;
	private LinearLayout dailyLayoutTransactionRoot;

	private DailyViewHolder dailyHolder;
	private TransactionViewHolder transactionHolder;

	HashSet<String> datas;

	public TransactionGroupByTypeListView(Context context, Activity activity,
			List<Transaction> transactions) {
		this.context = context;
		this.activity = activity;
		this.transactions = transactions;
	}

	public static class DailyViewHolder {

		TextView transactionDate;
		TextView transactionDateBalance;

		ImageView imgCollapes;
		ImageView imgExpand;

		public DailyViewHolder(View view) {
			this.transactionDate = (TextView) view.findViewById(R.id.tvDate);
			this.transactionDateBalance = (TextView) view
					.findViewById(R.id.tvDateBalance);

			this.imgCollapes = (ImageView) view.findViewById(R.id.imgCollapes);
			this.imgExpand = (ImageView) view.findViewById(R.id.imgExpand);

			Log.d("viewHolder", "new");
		}
	}

	public static class TransactionViewHolder {

		TextView category;
		TextView name;
		TextView date;
		TextView amount;
		ImageView imgIcon;

		public TransactionViewHolder(View view) {
			this.category = (TextView) view.findViewById(R.id.tvCategory);
			this.name = (TextView) view.findViewById(R.id.tvName);
			this.date = (TextView) view.findViewById(R.id.tvDate);
			this.amount = (TextView) view.findViewById(R.id.tvAmount);
			this.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		}
	}

	public void onBindView(LinearLayout root) {

		if (transactions.size() > 0) {
			Set<String> group = new HashSet<String>();
			for (Transaction tt : transactions) {
				group.add(tt.getTransactionType());
			}

			for (String g : group) {

				dailyLayout = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.transaction_list_template, null);
				this.dailyHolder = new DailyViewHolder(dailyLayout);

				dailyHolder.transactionDate.setText(g);

				// container of transaction
				dailyLayoutTransactionRoot = (LinearLayout) dailyLayout
						.findViewById(R.id.root1);

				dailyLayoutTransactionRoot.removeAllViewsInLayout();
				double transactionBalance = 0d;
				for (Transaction t : transactions) {

					if (g.equals(t.getTransactionType())) {
						transactionBalance += t.getAmount();

						dailyTransactionInflater = (LinearLayout) LayoutInflater
								.from(context).inflate(
										R.layout.transtion_template, null);

						dailyTransactionInflater.setTag(t.getId());

						this.transactionHolder = new TransactionViewHolder(
								dailyTransactionInflater);

						dailyTransactionInflater
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent(context,
												TransactionDetail.class);

										intent.putExtra(App.TRANSACTION_ID,
												Integer.parseInt(String
														.valueOf(v.getTag())));

										activity.startActivityForResult(
												intent,
												App.EDIT_TRANSACTION_REQUEST_CODE);
									}
								});

						transactionHolder.imgIcon.setBackgroundResource(t
								.getIcon());

						transactionHolder.category.setText(t
								.getTransactionCategory().getName());

						transactionHolder.name.setText(t.getShotName());

						transactionHolder.date.setText(dateTimeFormat.format(t
								.getCreateDate()));

						transactionHolder.amount.setTextColor(t
								.getTransactionType().equals(
										TransactionType.Income.name()) ? Color
								.rgb(51, 204, 51) : Color.RED);
						transactionHolder.amount.setText(moneyFormat.format(t
								.getAmount()));

						dailyLayoutTransactionRoot
								.addView(dailyTransactionInflater);
					}
				}

				dailyHolder.transactionDateBalance.setText(moneyFormat
						.format(transactionBalance));
				transactionBalance = 0d;
				root.addView(dailyLayout);
			}
		} else {
			dailyLayout = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.transaction_list_template, null);

			dailyLayoutTransactionRoot = (LinearLayout) dailyLayout
					.findViewById(R.id.root1);

			dailyTransactionInflater = (LinearLayout) LayoutInflater.from(
					context).inflate(R.layout.transtion_emptry_template, null);

			((TextView) dailyTransactionInflater.findViewById(R.id.tvEmptry))
					.setText("ไม่มีรายการ");

			dailyLayoutTransactionRoot.addView(dailyTransactionInflater);

			root.addView(dailyLayout);
		}
	}
}
