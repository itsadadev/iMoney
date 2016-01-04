package com.itsada.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.BaseActivity;
import com.itsada.imoney.R;
import com.itsada.imoney.views.DateFormatDialog;
import com.itsada.imoney.views.DateFormatDialog.DateFormatCallback;
import com.itsada.imoney.views.LanguageDialog;
import com.itsada.imoney.views.LanguageDialog.Callback;

@SuppressLint("InflateParams")
public class SettingManagementActivity extends BaseActivity implements
		OnClickListener {

	private TextView tvCancel, tvAccept;
	private ArrayList<LinearLayout> categoryGroup = new ArrayList<LinearLayout>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting_layout);
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

		ImageView imgLanguage = (ImageView) findViewById(R.id.imgLanguage);

		TextView lbExport = (TextView) findViewById(R.id.lbExport);
		lbExport.setOnClickListener(this);

		if (App.configuration.getLocale().equals(Locale.ENGLISH))
			imgLanguage.setBackgroundResource(R.drawable.eng);
		else
			imgLanguage.setBackgroundResource(R.drawable.th);

		TextView tvDateFormat = (TextView) findViewById(R.id.tvDateFormat);
		String format = App.configuration.getDateFormat();

		if (format.equals(BaseActivity.ddMMyyyyEnFormat)) {
			tvDateFormat.setText("31/01/2015");
		} else if (format.equals(BaseActivity.MMddyyyyEnFormat)) {
			tvDateFormat.setText("01/31/2015");
		} else if (format.equals(BaseActivity.yyyyddMMEnFormat)) {
			tvDateFormat.setText("2015/01/31");
		}

		((LinearLayout) findViewById(R.id.layoutLanguage))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final LanguageDialog languageDialog = new LanguageDialog(
								SettingManagementActivity.this);
						languageDialog.show(App.configuration.getLocale());
						languageDialog.callback = new Callback() {

							@Override
							public void callback() {
								Configuration config = App.configuration;
								config.setLocale(languageDialog.locale);
								App.resourceConfig.locale = languageDialog.locale;
								ConfigurationRepository configurationRepository = new ConfigurationRepository(
										getApplicationContext());
								configurationRepository.update(config);
								App.isUpdateLangage = true;
								onCreate(null);
							}
						};
					}
				});

		((LinearLayout) findViewById(R.id.layoutDateFormat))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						final Configuration config = App.configuration;
						final DateFormatDialog dateFormatDialog = new DateFormatDialog(
								SettingManagementActivity.this);
						dateFormatDialog.show();
						dateFormatDialog.setFormat(config.getDateFormat());
						dateFormatDialog.callback = new DateFormatCallback() {

							@Override
							public void callback() {
								ConfigurationRepository configurationRepository = new ConfigurationRepository(
										getApplicationContext());
								config.setDateFormat(dateFormatDialog.format
										.toString());

								configurationRepository.update(config);
								onCreate(null);
							}
						};
					}

				});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.imgBack:
			finish();
			break;

		case R.id.lbExport:
			export();
			break;

		default:
			break;
		}

	}

	private void export() {
		try {
			DatabaseHelper.backupDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
