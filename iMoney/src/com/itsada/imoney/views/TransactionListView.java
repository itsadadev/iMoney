package com.itsada.imoney.views;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class TransactionListView {

	private Context context;
	private List<Transaction> transactions;
	private ViewHolder holder;
	public LinearLayout view;

	public TransactionListView(Context context, List<Transaction> transactions) {
		this.context = context;
		this.transactions = transactions;

		onCreateView();
	}

	private void onCreateView() {

		view = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.transtion_template, null, false);
		
		holder = new ViewHolder(view);
	}

	public double onBindView(final Activity activity, LinearLayout root,
			String group) {

		double amountSumOfGroup = 0d;

		for (Transaction t : transactions) {

			if (group.equals(new SimpleDateFormat("dd/MM/yyyy").format(t
					.getCreateDate()))) {

				amountSumOfGroup += t.getAmount();

				holder.category.setText(t.getTransactionCategory().getName());
				holder.name.setText(t.getShotName());
				holder.date.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
						.format(t.getCreateDate()));
				holder.amount.setText(new DecimalFormat("#,##0.00").format(t
						.getAmount()));
				holder.amount.setTextColor(t.getTransactionType().equals(
						TransactionType.Income.name()) ? Color.rgb(51, 204, 51)
						: Color.RED);
				holder.imgIcon.setBackgroundResource(t.getTransactionCategory()
						.getIcon());

				view.setTag(t.getId());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								TransactionDetail.class);

						intent.putExtra(App.TRANSACTION_ID,
								Integer.parseInt(String.valueOf(v.getTag())));

						activity.startActivityForResult(intent,
								App.EDIT_TRANSACTION_REQUEST_CODE);
					}
				});

				root.addView(view);
			}
		}
		return amountSumOfGroup;
	}

	public class ViewHolder {
		TextView category;
		TextView name;
		TextView date;
		TextView amount;
		ImageView imgIcon;

		public ViewHolder(View view) {
			this.category = (TextView) view.findViewById(R.id.tvCategory);
			this.name = (TextView) view.findViewById(R.id.tvName);
			this.date = (TextView) view.findViewById(R.id.tvDate);
			this.amount = (TextView) view.findViewById(R.id.tvAmount);
			this.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		}
	}

}
