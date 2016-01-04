package com.itsada.imoney.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.AccountType;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.CreateAccountEmptryActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionFragmentActivity;
import com.itsada.imoney.TransactionListAcitvity;
import com.itsada.management.Format;

@SuppressLint("InflateParams")
public class AccountTypeListView {

	private Context context;
	private Activity activity;
	private List<AccountType> accountTypes = Collections.emptyList();
	private LinearLayout category;
	private ImageView imgAddAccount;

	private TextView tvCategoryName;
	private TextView tvAccountCount;
	private TextView tvSum;
	private LinearLayout account_layout;
	private RelativeLayout layoutExpand;

	private LinearLayout accountLayout;
	private ImageView iconCategory;
	private TextView tvAccountName;
	private TextView tvBalance;
	public double assetSum = 0d;
	public double liabilitiesSum = 0d;
	public double sum = 0d;

	private ArrayList<LinearLayout> categoryGroup = new ArrayList<LinearLayout>();

	public AccountTypeListView(Context context, Activity activity,
			List<AccountType> accountTypes) {
		this.context = context;
		this.activity = activity;
		this.accountTypes = accountTypes;
	}

	protected void clearChild(ViewGroup layoutLabel) {
		for (int i = 0; i < layoutLabel.getChildCount(); i++)
			layoutLabel.removeView(layoutLabel.getChildAt(i));

		if (layoutLabel.getChildCount() > 0)
			clearChild(layoutLabel);
	}

	public void onBindView(LinearLayout root) {
		double sumCategory = 0d;

		// clearChild(root);

		for (final AccountType accountType : accountTypes) {
			// if (accountType.isVisible()) {

			sumCategory += accountType.getBalances();
			category = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.account_header_list_template, null);

			((TextView) category.findViewById(R.id.tvSumL)).setText(activity
					.getResources().getString(R.string.sum));

			layoutExpand = (RelativeLayout) category
					.findViewById(R.id.layoutExpand);
			layoutExpand.setTag(accountType.getId());

			imgAddAccount = (ImageView) category.findViewById(R.id.imgAdd);
			imgAddAccount.setTag(accountType.getId());

			tvCategoryName = (TextView) category.findViewById(R.id.tvTitle);
			tvCategoryName.setText(accountType.getName());

			tvAccountCount = (TextView) category.findViewById(R.id.tvCount);
			tvAccountCount
					.setText(String.valueOf(accountType.getAccountSize()));

			tvSum = (TextView) category.findViewById(R.id.tvSum);
			tvSum.setText(Format.getInstance(activity).getMoneyFormat()
					.format(sumCategory));

			account_layout = (LinearLayout) category
					.findViewById(R.id.account_layout);
			account_layout.setTag(accountType.getId());
			categoryGroup.add(account_layout);

			// Event hide and show account of group
			layoutExpand.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int id = Integer.parseInt(v.getTag().toString());
					for (final LinearLayout layout : categoryGroup) {
						if (Integer.parseInt(layout.getTag().toString()) == id) {

							LinearLayout parent = (LinearLayout) layout
									.getParent();
							if (layout.getVisibility() == View.VISIBLE) {
								layout.setVisibility(View.GONE);
								layout.startAnimation(AnimationUtils
										.loadAnimation(context,
												R.anim.push_up_in));

								parent.findViewById(R.id.imgExpand)
										.setVisibility(View.VISIBLE);
								parent.findViewById(R.id.imgCollapes)
										.setVisibility(View.GONE);
							} else {
								layout.setVisibility(View.VISIBLE);
								// layout.startAnimation(AnimationUtils.loadAnimation(context,
								// R.anim.push_up_in));
								parent.findViewById(R.id.imgExpand)
										.setVisibility(View.GONE);
								parent.findViewById(R.id.imgCollapes)
										.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			});

			imgAddAccount.setOnClickListener(new OnClickListener() {

				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {

					int id = Integer.parseInt(v.getTag().toString());

					for (final LinearLayout layout : categoryGroup) {
						if (Integer.parseInt(layout.getTag().toString()) == id) {
							Intent intent = new Intent(context,
									CreateAccountEmptryActivity.class);
							intent.putExtra(App.ACCOUNT_TYPE_ID, id);

							activity.startActivityForResult(intent,
									App.CREATE_ACCOUNT_REQUEST_CODE);
							// startActivity(intent);
						}
					}
				}
			});

			if (accountType.getAccounts().size() == 0) {
				accountLayout = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.account_template, null);

				((ImageView) accountLayout.findViewById(R.id.imgAccountDetail))
						.setVisibility(View.GONE);
				account_layout.addView(accountLayout);

			} else {
				for (Account account : accountType.getAccounts()) {

					sum += account.getBalance();
					if (accountType.isVisible()) {
						if (account.getAccountTypeId() <= 3) {
							assetSum += account.getBalance();
						} else {
							liabilitiesSum += account.getBalance();
						}
					}

					accountLayout = (LinearLayout) LayoutInflater.from(context)
							.inflate(R.layout.account_template, null);

					accountLayout.setTag(accountType.getId() + ","
							+ account.getId());

					accountLayout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// Intent intent = new Intent(context,
							// TransactionListAcitvity.class);
							Intent intent = new Intent(context,
									TransactionFragmentActivity.class);

							String[] param = String.valueOf(v.getTag()).split(
									",");

							intent.putExtra(App.ACCOUNT_TYPE_ID, param[0]);
							intent.putExtra(App.ACCOUNT_ID, param[1]);

							activity.startActivity(intent);
						}
					});

					iconCategory = (ImageView) accountLayout
							.findViewById(R.id.imageAccount);

					iconCategory.setBackgroundResource(account.getIcon());

					tvAccountName = (TextView) accountLayout
							.findViewById(R.id.tvTitle);
					tvAccountName.setText(account.getName());

					tvBalance = (TextView) accountLayout
							.findViewById(R.id.tvBalance);
					tvBalance.setText(Format.getInstance(activity)
							.getMoneyFormat().format(account.getBalance()));

					if (account.isHide())
						account_layout.addView(accountLayout);
				}
				// }
			}

			if (accountType.isVisible())
				root.addView(category);
			sumCategory = 0d;
		}
	}

	public static class TypeViewHolder {

	}
}
