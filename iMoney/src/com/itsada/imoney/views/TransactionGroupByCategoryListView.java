package com.itsada.imoney.views;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionDetail;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class TransactionGroupByCategoryListView {

	private Context context;
	private Activity activity;
	private int accountID;
	private Date from, to;
	private boolean asc;
	// private List<Transaction> transactions;

	private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	// private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
	// "dd/MM/yyyy HH:mm");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	// Layout
	private LinearLayout dailyTransactionInflater;
	private LinearLayout dailyLayout;
	private LinearLayout dailyLayoutTransactionRoot;

	private DailyViewHolder dailyHolder;
	private TransactionViewHolder transactionHolder;

	HashSet<String> datas;
	private ArrayList<LinearLayout> categoryGroup = null;

	public TransactionGroupByCategoryListView(Context context,
			Activity activity, int accId, Date f, Date t, boolean asc) {
		this.context = context;
		this.activity = activity;
		this.accountID = accId;
		this.from = f;
		this.to = t;
		this.asc = asc;
	}

	public static class DailyViewHolder {

		TextView transactionDate;
		TextView transactionDateBalance;

		TextView tvIncome;
		TextView tvExpande;
		
		ImageView imgCollapes;
		ImageView imgExpand;

		public DailyViewHolder(View view) {
			this.transactionDate = (TextView) view.findViewById(R.id.tvDate);
			this.transactionDateBalance = (TextView) view
					.findViewById(R.id.tvDateBalance);

			this.imgCollapes = (ImageView) view.findViewById(R.id.imgCollapes);
			this.imgExpand = (ImageView) view.findViewById(R.id.imgExpand);
			this.tvIncome = (TextView) view.findViewById(R.id.tvIncome);
			this.tvExpande = (TextView) view.findViewById(R.id.tvExpande);
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

		categoryGroup = new ArrayList<LinearLayout>();

		// root.removeView(root);
		for (int i = 0; i < root.getChildCount(); i++) {
			root.removeView(root.getChildAt(i));
		}

		if (root.getChildCount() > 0) {
			for (int i = 0; i < root.getChildCount(); i++) {
				root.removeView(root.getChildAt(i));
			}
		}

		TransactionRepository transactionRepository = new TransactionRepository(
				context,
				App.configuration.getLocale());
		ArrayList<Transaction> transactionGroup = transactionRepository
				.getTransactionGroupByCateogory(accountID, from.getTime(),
						to.getTime(), asc);

		if (transactionGroup.size() > 0) {

			for (Transaction tGroup : transactionGroup) {

				double income = 0d;
				double expande = 0d;
				
				dailyLayout = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.transaction_list_template, null);

				dailyLayout.setTag(tGroup.getTransactionCategory().getId());

				final LinearLayout transactionLayout = (LinearLayout) dailyLayout
						.findViewById(R.id.transactionLayout);
				transactionLayout.setTag(tGroup.getId());
				categoryGroup.add(transactionLayout);
				transactionLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String id = v.getTag().toString();
						for (final LinearLayout layout : categoryGroup) {
							if (layout.getTag().toString().equals(id)) {

								LinearLayout parent = (LinearLayout) layout
										.getParent();
								LinearLayout child = (LinearLayout) parent
										.findViewById(R.id.root1);

								if (child.getVisibility() == View.VISIBLE) {

									child.setVisibility(View.GONE);
									child.startAnimation(AnimationUtils
											.loadAnimation(context,
													R.anim.push_up_in));

									parent.findViewById(R.id.imgExpand)
											.setVisibility(View.VISIBLE);
									parent.findViewById(R.id.imgCollapes)
											.setVisibility(View.GONE);
								} else {
									child.setVisibility(View.VISIBLE);
									child.startAnimation(AnimationUtils
											.loadAnimation(context,
													R.anim.push_up_in));
									parent.findViewById(R.id.imgExpand)
											.setVisibility(View.GONE);
									parent.findViewById(R.id.imgCollapes)
											.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				});

				this.dailyHolder = new DailyViewHolder(dailyLayout);

				dailyHolder.transactionDate.setText(tGroup
						.getTransactionCategory().getName());

				// container of transaction
				dailyLayoutTransactionRoot = (LinearLayout) dailyLayout
						.findViewById(R.id.root1);

				// dailyLayoutTransactionRoot.setTag(g);
				dailyLayoutTransactionRoot.removeAllViewsInLayout();
				double transactionBalance = 0d;

				TransactionRepository repository = new TransactionRepository(
						context,
						App.configuration.getLocale());
				
				ArrayList<Transaction> transactions = repository
						.getTransactionByCategoryInterval(accountID, tGroup
								.getTransactionCategory().getId(), from
								.getTime(), to.getTime());

				for (Transaction t : transactions) {

					if (t.getTransactionType().equals(
							TransactionType.Income.name())
							|| t.getTransactionType().equals(
									TransactionType.TransferTo.name())){
						transactionBalance += t.getAmount();
						income += t.getAmount();
					}
					else{
						transactionBalance -= t.getAmount();
						expande += t.getAmount();
					}

					dailyTransactionInflater = (LinearLayout) LayoutInflater
							.from(context).inflate(R.layout.transtion_template,
									null);

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
											Integer.parseInt(String.valueOf(v
													.getTag())));

									activity.startActivityForResult(intent,
											App.EDIT_TRANSACTION_REQUEST_CODE);
								}
							});

					transactionHolder.imgIcon
							.setBackgroundResource(t.getIcon());

					transactionHolder.category.setText(dateFormat.format(t
							.getCreateDate()));

					transactionHolder.name.setText(t.getShotName());

					transactionHolder.date.setText(timeFormat.format(t
							.getCreateDate()));

					if (t.getTransactionType().equals(
							TransactionType.Income.name())
							|| t.getTransactionType().equals(
									TransactionType.TransferTo.name())) {

						transactionHolder.amount.setTextColor(Color.rgb(0, 100,
								00));
						transactionHolder.amount.setText("+"
								+ moneyFormat.format(t.getAmount()));
					} else {
						transactionHolder.amount.setTextColor(Color.rgb(200, 0,
								00));
						transactionHolder.amount.setText("-"
								+ moneyFormat.format(t.getAmount()));
					}
					
					dailyLayoutTransactionRoot
							.addView(dailyTransactionInflater);
				}

				dailyHolder.tvIncome.setText("+" + BaseActivity.moneyFormat.format(income));
				dailyHolder.tvIncome.setTextColor(Color.rgb(0, 100, 0));
				dailyHolder.tvIncome.setPaintFlags(dailyHolder.tvIncome.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);
				
				dailyHolder.tvExpande.setText("-" + BaseActivity.moneyFormat.format(expande));
				dailyHolder.tvExpande.setTextColor(Color.rgb(200, 0, 0));
				dailyHolder.transactionDateBalance.setText(moneyFormat
						.format(transactionBalance));
				dailyHolder.tvExpande.setPaintFlags(dailyHolder.tvExpande.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);
				
				dailyHolder.transactionDateBalance.setText(moneyFormat
						.format(transactionBalance));
				transactionBalance = 0d;

				if (dailyLayout.getTag().equals(
						tGroup.getTransactionCategory().getId()))
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
					.setText(activity.getResources().getString(
							R.string.noTransaction));

			dailyLayoutTransactionRoot.addView(dailyTransactionInflater);

			root.addView(dailyLayout);
		}
	}
}
