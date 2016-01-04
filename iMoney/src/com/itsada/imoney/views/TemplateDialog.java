package com.itsada.imoney.views;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.TemplateRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.R;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class TemplateDialog implements OnClickListener {

	 private Activity activity;
	private Dialog dialog;
	public Callback callback;

	private ImageView imgClose;
	private TextView tvContent;
	private LinearLayout root;
	private Context context;
	private View imgIcon;
	private TextView category;
	private TextView name;
	private TextView date;
	private TextView amount;
	private Transaction transaction;

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			App.configuration.getDateFormat());
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

	public TemplateDialog(Activity activity) {
		 this.activity = activity;
		this.context = activity.getApplicationContext();

		dialog = new Dialog(activity);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);

		dialog.setContentView(R.layout.custom_dialog_template);

		tvContent = (TextView) dialog.findViewById(R.id.tvEvent);
		imgClose = (ImageView) dialog.findViewById(R.id.imgBack);

		imgClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		root = null;
		root = (LinearLayout) dialog.findViewById(R.id.root);
		for (int i = 0; i < root.getChildCount(); i++) {
			root.removeView(root.getChildAt(i));
		}

	}

	public void show() {

		dialog.show();
	}

	public void show(String title) {

		tvContent.setText(title);

		TemplateRepository repository = new TemplateRepository(context,
				App.configuration.getLocale());

		ArrayList<Transaction> ts = repository.getTransactions();

		if (ts.size() > 0) {
			for (Transaction t : ts) {
				LinearLayout template = (LinearLayout) LayoutInflater.from(
						context).inflate(R.layout.transtion_template, null);
				template.setTag(t);

				category = (TextView) template.findViewById(R.id.tvCategory);
				name = (TextView) template.findViewById(R.id.tvName);
				date = (TextView) template.findViewById(R.id.tvDate);
				amount = (TextView) template.findViewById(R.id.tvAmount);
				imgIcon = (ImageView) template.findViewById(R.id.imgIcon);

				imgIcon.setBackgroundResource(t.getTransactionCategory()
						.getIcon());

				category.setText(dateFormat.format(t.getCreateDate()));

				name.setText(t.getShotName());

				date.setText(timeFormat.format(t.getCreateDate()));

				amount.setTextColor(t.getTransactionType().equals(
						TransactionType.Income.name()) ? Color.rgb(0, 100, 00)
						: Color.rgb(200, 0, 0));
				amount.setText(t.getTransactionType().equals(
						TransactionType.Income.name()) ? "+"
						+ moneyFormat.format(t.getAmount()) : "-"
						+ moneyFormat.format(t.getAmount()));

				template.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (callback != null) {

							Log.d("", "row click");
							transaction = (Transaction) v.getTag();
							callback.updateView();
							dialog.dismiss();
						}
					}
				});

				root.addView(template);
			}

		} else {
			LinearLayout template = (LinearLayout) LayoutInflater.from(
					context).inflate(R.layout.transtion_emptry_template, null);

			((TextView) template.findViewById(R.id.tvEmptry))
			.setText(activity.getResources().getString(R.string.noTransaction));
			
			root.addView(template);
		}
		dialog.show();

	}

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	public interface Callback {
		void updateView();
	}
}
