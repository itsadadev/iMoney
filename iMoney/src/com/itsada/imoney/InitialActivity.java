package com.itsada.imoney;

import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;
import com.itsada.imoney.views.LanguageDialog;
import com.itsada.imoney.views.LanguageDialog.Callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InitialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.initial_layout);
		
		final LanguageDialog languageDialog = new LanguageDialog(this);
		languageDialog.setVisibleBtCancel(View.GONE);
		languageDialog.show(getResources().getString(
				R.string.pleaseSelectLanguage));

		languageDialog.callback = new Callback() {

			@Override
			public void callback() {

				ConfigurationRepository configurationRepository = new ConfigurationRepository(
						getApplicationContext());

				Configuration configuration = new Configuration();
				configuration.setLocale(languageDialog.locale);
				configuration.setDateFormat(BaseActivity.ddMMyyyyEnFormat);
				configuration.setTimeFormat(BaseActivity.timeFormat.toString());
				configuration.setMoneyFormat(BaseActivity.moneyFormat.toString());

				configurationRepository.add(configuration);
				
				App.configuration = configuration;
				App.resourceConfig.locale = configuration.getLocale();
				
				startActivity(new Intent(getApplicationContext(), AccountListActivity.class));
				
				finish();
			}
		};
	}

}
