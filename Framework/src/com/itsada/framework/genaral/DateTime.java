package com.itsada.framework.genaral;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {

	private Calendar calendar;

	public DateTime() {
		this.calendar = Calendar.getInstance();
	}

	public DateTime(Locale locale) {
		this.calendar = Calendar.getInstance(locale);
	}

	public void addDay(int day) {
		this.calendar.set(Calendar.DATE, 1);
	}

	public void addMonth(int month) {
		this.calendar.set(Calendar.MONTH, 1);
	}

	public void addNextMonth() {
		this.calendar.set(Calendar.MONTH, 1);
		this.calendar.set(Calendar.DATE, -1);
	}

	public void addPreviusMonth() {
		this.calendar.set(Calendar.MONTH, -1);
		this.calendar.set(Calendar.DATE, 1);
	}

	public void addYear(int tear) {
		this.calendar.set(Calendar.YEAR, 1);
	}

	public Date getDate() {
		return calendar.getTime();
	}

	public void setDate(Calendar calendar) {
		this.calendar = calendar;
	}

	public static Date now() {
		return Calendar.getInstance().getTime();
	}

	public static Date now(Locale locale) {
		return Calendar.getInstance(locale).getTime();
	}

	public static Date getFirstDateOfCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date getNextFirstDateOfCurrentMonth(int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}
	
	public static Date getNextLastDateOfCurrentMonth(int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, 1 + month);
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}
	
	public static Date getPreviousFirstDateOfCurrentMonth(int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, -1 * month);
		return calendar.getTime();
	}
	
	public static Date getPreviousLastDateOfCurrentMonth(int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, 1 - month);
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}


	public static Date getLastDateOfCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}

	
}
