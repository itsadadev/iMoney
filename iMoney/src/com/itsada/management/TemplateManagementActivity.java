package com.itsada.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Template;
import com.itsada.framework.repository.SQLite.TemplateRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.TemplateDetail;
import com.itsada.imoney.views.ConfirmDialog;

@SuppressLint("InflateParams")
public class TemplateManagementActivity extends BaseActivity implements
		OnClickListener {

	private TextView tvCancel, tvAccept;

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
		tvEvent.setText(getResources().getString(R.string.templateManagement));
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

		TemplateRepository templateRepository = new TemplateRepository(
				getApplicationContext(), App.configuration.getLocale());

		LinearLayout template;

		ArrayList<Template> templates = templateRepository.getAll();
		if (templates.size() > 0) {

			for (Template templateTransaction : templates) {

				template = (LinearLayout) getLayoutInflater().inflate(
						R.layout.menu_item_template, null, false);

				ImageView imgIcon = (ImageView) template
						.findViewById(R.id.imgIcon);
				imgIcon.setBackgroundResource(templateTransaction.getIcon());
				TextView tvTitle = (TextView) template
						.findViewById(R.id.tvTitle);
				ImageView imgEvent = (ImageView) template
						.findViewById(R.id.imgEvent);
				imgEvent.setTag(templateTransaction.getId());
				imgEvent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(getApplicationContext(),
								TemplateDetail.class);

						intent.putExtra(App.TEMPLATE_ID,
								Integer.parseInt(String.valueOf(v.getTag())));

						startActivityForResult(intent,
								App.EDIT_TEMPLATE_REQUEST_CODE);
					}
				});

				ImageView imgEventDel = (ImageView) template
						.findViewById(R.id.imgEventDel);
				imgEventDel.setTag(templateTransaction.getId());
				imgEventDel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						try {

							ConfirmDialog confirmDialog = new ConfirmDialog(
									TemplateManagementActivity.this);
							confirmDialog.show(getResources().getString(
									R.string.confirmDeleteTemplate));
							confirmDialog.callback = new ConfirmDialog.Callback() {

								@Override
								public void callback() {
									TemplateRepository templateRepository = new TemplateRepository(
											getApplicationContext(),
											App.configuration.getLocale());

									templateRepository.delete(Integer
											.parseInt(v.getTag().toString()));

									showDialog(
											"",
											getResources()
													.getString(
															R.string.deleteTemplateSuccess));
									updateView = new UpdateView() {

										@Override
										public void update() {

										}
									};
								}
							};
						} catch (Exception e) {
							showDialog(
									"",
									getResources().getString(
											R.string.deleteTemplateyFail));
						}
					}
				});

				imgIcon.setBackgroundResource(templateTransaction
						.getTransactionCategory().getIcon());
				tvTitle.setText(templateTransaction.getTransactionCategory().getName() + " " + String.valueOf(templateTransaction.getAmount()));

				root.addView(template);
			}
		} else {
			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.transtion_emptry_template, null, false);

			TextView tvTitle = (TextView) template.findViewById(R.id.tvEmptry);
			tvTitle.setText(getResources().getString(R.string.templateEmptry));

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
