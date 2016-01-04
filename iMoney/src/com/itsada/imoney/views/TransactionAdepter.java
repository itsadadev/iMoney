package com.itsada.imoney.views;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionDetail;

@SuppressLint("SimpleDateFormat")
public class TransactionAdepter extends BaseAdapter {

	private Context mContext;
	private Activity mActivity;
	private ArrayList<Transaction> mTransactions;

	public TransactionAdepter(Activity activity,
			ArrayList<Transaction> transactions) {
		this.mActivity = activity;
		this.mContext = activity.getApplicationContext();

//		if (transactions.size() > 0) {
			this.mTransactions = transactions;
//		} else {
//			//set empty
//			this.mTransactions = new ArrayList<Transaction>();
//			
//			Transaction t = new Transaction();
//			t.setId(-999);
//			this.mTransactions.add(t);
//		}
	}

	@Override
	public int getCount() {
		return mTransactions.size();
	}

	@Override
	public Object getItem(int position) {
		return mTransactions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("t", "getVIEW");
		final ViewHolder viewHolder;
		final Transaction t = mTransactions.get(position);

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//			if (mTransactions.get(0).getId() > 0) {
				convertView = inflater.inflate(R.layout.transtion_template,
						parent, false);
//			} else {
//				convertView = inflater.inflate(
//						R.layout.transtion_emptry_template, parent, false);
//
//				((TextView) convertView.findViewById(R.id.tvEmptry))
//						.setText(mActivity.getResources().getString(
//								R.string.noTransaction));
//			}

			viewHolder = new ViewHolder();

			viewHolder.category = (TextView) convertView
					.findViewById(R.id.tvCategory);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
			viewHolder.amount = (TextView) convertView
					.findViewById(R.id.tvAmount);
			viewHolder.imgIcon = (ImageView) convertView
					.findViewById(R.id.imgIcon);

			viewHolder.transactionLayout = (LinearLayout) convertView;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		if (mTransactions.get(0).getId() > 0) {
			viewHolder.transactionLayout
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									TransactionDetail.class);

							intent.putExtra(App.TRANSACTION_ID, t.getId());

							mActivity.startActivityForResult(intent,
									App.EDIT_TRANSACTION_REQUEST_CODE);
						}
					});

			viewHolder.imgIcon.setBackgroundResource(t.getIcon());

			viewHolder.category.setText(t
					.getTransactionCategory().getName());

			viewHolder.name.setText(t.getShotName());

			viewHolder.date.setText(BaseActivity.timeFormat.format(t
					.getCreateDate()));

			viewHolder.amount.setTextColor(t.getTransactionType().equals(
					TransactionType.Income.name()) ? Color.rgb(0, 100, 00)
					: Color.rgb(200, 0, 0));
			viewHolder.amount.setText(t.getTransactionType().equals(
					TransactionType.Income.name()) ? "+"
					+ BaseActivity.moneyFormat.format(t.getAmount()) : "-"
					+ BaseActivity.moneyFormat.format(t.getAmount()));
//		}

		return convertView;
	}

	static class ViewHolder {
		TextView category;
		TextView name;
		TextView date;
		TextView amount;
		ImageView imgIcon;
		LinearLayout transactionLayout;
	}
}
