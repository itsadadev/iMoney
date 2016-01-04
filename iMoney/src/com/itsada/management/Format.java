package com.itsada.management;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;
import com.itsada.imoney.App;

@SuppressLint("SimpleDateFormat")
public class Format {

	private static Format formate;
	private static SimpleDateFormat dateFormat;
	private static SimpleDateFormat monthFormat;
	private static DecimalFormat moneyFormat;

	public static Format getInstance(Activity activity) {

		if (formate == null) {

			formate = new Format();

			ConfigurationRepository configurationRepository = new ConfigurationRepository(
					activity);
			Configuration configuration = configurationRepository
					.getCurrentConfig();

			if (!configuration.getDateFormat().equals("")) {
				monthFormat = new SimpleDateFormat("MMM yyyy",
						App.configuration.getLocale());
				dateFormat = new SimpleDateFormat(
						configuration.getDateFormat(),
						App.configuration.getLocale());
			}

			// if (!configuration.getMoneyFormat().equals(""))
			// moneyFormat = new DecimalFormat(configuration.getMoneyFormat());
			// else
			moneyFormat = new DecimalFormat("#,##0.00");
		}

		return formate;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
	public SimpleDateFormat getMonthFormat() {
		return monthFormat;
	}

	public DecimalFormat getMoneyFormat() {
		return moneyFormat;
	}
}
