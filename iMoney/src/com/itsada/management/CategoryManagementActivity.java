package com.itsada.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.EditTransactionCategory;
import com.itsada.imoney.R;
import com.itsada.imoney.views.ConfirmDialog;

@SuppressLint("InflateParams")
public class CategoryManagementActivity extends BaseActivity implements
		OnClickListener {

	private TextView tvCancel, tvAccept;
	private ArrayList<LinearLayout> categoryGroup = new ArrayList<LinearLayout>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.account_type_list_layout);
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
		tvEvent.setText(getResources().getString(R.string.categoryManagement));
		ImageView imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);
		((ImageView) mCustomView.findViewById(R.id.imgDone))
				.setVisibility(View.GONE);
	}

	@Override
	protected void onInit() {

		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setVisibility(View.GONE);
		tvAccept = (TextView) findViewById(R.id.tvAccept);
		tvAccept.setVisibility(View.GONE);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		LinearLayout categoryLayout;
		LinearLayout typeTemplate;
		RelativeLayout layoutExpand;
		TextView tvHeader;

		for (int i = 0; i < 2; i++) {

			typeTemplate = (LinearLayout) LayoutInflater.from(this).inflate(
					R.layout.account_header_list_template, null);

			tvHeader = (TextView) typeTemplate.findViewById(R.id.tvTitle);
			categoryLayout = (LinearLayout) typeTemplate
					.findViewById(R.id.account_layout);
			categoryLayout.setTag(i);
			categoryGroup.add(categoryLayout);
			
			typeTemplate.findViewById(R.id.tvSumL).setVisibility(View.GONE);
			layoutExpand = (RelativeLayout) typeTemplate
					.findViewById(R.id.layoutExpand);
			layoutExpand.setTag(i);

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
										.loadAnimation(getApplicationContext(),
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

			if (i == 0) {
				tvHeader.setText(getResources().getString(R.string.income));
				createIncome(categoryLayout);
			} else {
				tvHeader.setText(getResources().getString(R.string.expand));
				createExpend(categoryLayout);
			}

			root.addView(typeTemplate);
		}
	}

	private void createExpend(LinearLayout root) {
		TransactionCategoryRepository categoryRepository = new TransactionCategoryRepository(
				getApplicationContext(), App.configuration.getLocale());

		ArrayList<TransactionCategory> categories = categoryRepository
				.getByType(TransactionCategory.Type.Expenses);
		LinearLayout template;

		if (categories.size() > 0) {

			for (TransactionCategory transactionCategory : categories) {

				template = (LinearLayout) getLayoutInflater().inflate(
						R.layout.menu_item_template, null, false);

				ImageView imgIcon = (ImageView) template
						.findViewById(R.id.imgIcon);
				TextView tvAccount = (TextView) template
						.findViewById(R.id.tvTitle);
				ImageView imgEvent = (ImageView) template
						.findViewById(R.id.imgEvent);
				imgEvent.setTag(transactionCategory.getId());

				imgEvent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(getApplicationContext(),
								EditTransactionCategory.class);
						intent.putExtra(App.TRANSACTION_CATEGORY_ID,
								Integer.parseInt(v.getTag().toString()));
						startActivityForResult(intent,
								App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE);
					}
				});

				ImageView imgEventDelete = (ImageView) template
						.findViewById(R.id.imgEventDel);
				imgEventDelete.setTag(transactionCategory.getId());

				imgEventDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						try {

							ConfirmDialog confirmDialog = new ConfirmDialog(
									CategoryManagementActivity.this);
							confirmDialog.show(getResources().getString(
									R.string.confirmDeleteCategory));
							confirmDialog.callback = new ConfirmDialog.Callback() {

								@Override
								public void callback() {
									TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
											getApplicationContext(),
											App.configuration.getLocale());

									transactionCategoryRepository
											.delete(Integer.parseInt(v.getTag()
													.toString()));

									showDialog(
											"",
											getResources()
													.getString(
															R.string.deleteCategorySuccess));
									updateView = new UpdateView() {

										@Override
										public void update() {
											// renderTransactionCategory();
										}
									};
								}
							};

						} catch (Exception e) {
							showDialog(
									"",
									getResources().getString(
											R.string.deleteCategoryFail));
						}
					}
				});

				imgIcon.setBackgroundResource(transactionCategory.getIcon());
				tvAccount.setText(transactionCategory.getName());

				root.addView(template);
			}
		} else {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.transtion_emptry_template, null, false);

			TextView tvTitle = (TextView) template.findViewById(R.id.tvEmptry);
			tvTitle.setText(getResources().getString(R.string.categoryEmptry));

			root.addView(template);
		}
	}

	private void createIncome(LinearLayout root) {
		TransactionCategoryRepository categoryRepository = new TransactionCategoryRepository(
				getApplicationContext(), App.configuration.getLocale());

		ArrayList<TransactionCategory> categories = categoryRepository
				.getByType(TransactionCategory.Type.Income);
		LinearLayout template;

		if (categories.size() > 0) {

			for (TransactionCategory transactionCategory : categories) {

				template = (LinearLayout) getLayoutInflater().inflate(
						R.layout.menu_item_template, null, false);

				ImageView imgIcon = (ImageView) template
						.findViewById(R.id.imgIcon);
				TextView tvAccount = (TextView) template
						.findViewById(R.id.tvTitle);
				ImageView imgEvent = (ImageView) template
						.findViewById(R.id.imgEvent);
				imgEvent.setTag(transactionCategory.getId());

				imgEvent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(getApplicationContext(),
								EditTransactionCategory.class);
						intent.putExtra(App.TRANSACTION_CATEGORY_ID,
								Integer.parseInt(v.getTag().toString()));
						startActivityForResult(intent,
								App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE);
					}
				});

				ImageView imgEventDelete = (ImageView) template
						.findViewById(R.id.imgEventDel);
				imgEventDelete.setTag(transactionCategory.getId());

				imgEventDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						try {

							ConfirmDialog confirmDialog = new ConfirmDialog(
									CategoryManagementActivity.this);
							confirmDialog.show(getResources().getString(
									R.string.confirmDeleteCategory));
							confirmDialog.callback = new ConfirmDialog.Callback() {

								@Override
								public void callback() {
									TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
											getApplicationContext(),
											App.configuration.getLocale());

									transactionCategoryRepository
											.delete(Integer.parseInt(v.getTag()
													.toString()));

									showDialog(
											"",
											getResources()
													.getString(
															R.string.deleteCategorySuccess));
									updateView = new UpdateView() {

										@Override
										public void update() {
											// renderTransactionCategory();
										}
									};
								}
							};

						} catch (Exception e) {
							showDialog(
									"",
									getResources().getString(
											R.string.deleteCategoryFail));
						}
					}
				});

				imgIcon.setBackgroundResource(transactionCategory.getIcon());
				tvAccount.setText(transactionCategory.getName());

				root.addView(template);
			}
		} else {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.transtion_emptry_template, null, false);

			TextView tvTitle = (TextView) template.findViewById(R.id.tvEmptry);
			tvTitle.setText(getResources().getString(R.string.categoryEmptry));

			root.addView(template);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.imgBack:
			finish();
			break;
		case R.id.tvAccept:
			updateTemplate();
			break;

		default:
			break;
		}

	}

	private void updateTemplate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == App.EDIT_TEMPLATE_REQUEST_CODE) {

			String messageSuccess = getResources().getString(
					R.string.editTemplateSuccess);
			String messageFail = getResources().getString(
					R.string.editTemplateFail);

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

}
