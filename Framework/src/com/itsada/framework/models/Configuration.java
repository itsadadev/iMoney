package com.itsada.framework.models;

import java.util.Locale;

public class Configuration extends Entity {

	private Locale locale;
	private String currency;
	private String dateFormat;
	private String timeFormat;
	private String moneyFormat;
	private String password;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getMoneyFormat() {
		return moneyFormat;
	}

	public void setMoneyFormat(String moneyFormat) {
		this.moneyFormat = moneyFormat;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Configuration() {
		this.locale = null;
		this.currency = "";
		this.dateFormat = "";
		this.timeFormat = "";
		this.moneyFormat = "";
		this.password = "";
	}

	

}
