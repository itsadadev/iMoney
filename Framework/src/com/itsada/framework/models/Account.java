package com.itsada.framework.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.itsada.framework.R;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;

public class Account extends Entity {

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Fields-------------------------------- */
	/* ------------------------------------------------------------------------- */
	private String name;
	private double balance;
	private Date createDate;
	private Date updateDate;
	private int icon;
	private boolean isHide = false;
	private int accountTypeId;
	private String color = "#FFFF0000";

	private ArrayList<Transaction> transactions;

	public Account() {
		this.transactions = new ArrayList<Transaction>();
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Getter and Setter--------------------- */
	/* ------------------------------------------------------------------------- */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getIcon() {
		if (this.icon <= 0)
			return R.drawable.ic_account_balance_black;
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(int accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int color() {
		return Color.parseColor(this.color);
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Methods------------------------------- */
	/* ------------------------------------------------------------------------- */
	public double getReceipt() {
		double receipt = 0d;
		for (Transaction t : this.transactions) {
			if (t.getTransactionType().equals(TransactionType.Income.name()))
				receipt += t.getAmount();
		}

		return receipt;
	}

	public double getExpenditure() {
		double receipt = 0d;
		for (Transaction t : this.transactions) {
			if (t.getTransactionType().equals(TransactionType.Expenses.name()))
				receipt += t.getAmount();
		}

		return receipt;
	}

	public Transaction getTransactionById(int id) {

		Transaction t = null;
		for (Transaction transaction : this.transactions) {
			if (transaction.getId() == id) {
				t = transaction;
				break;
			}
		}

		return t;
	}

	public ArrayList<Transaction> getTransactionBy(Date form, Date to) {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		Log.e("From", form.toString());
		Log.e("to", to.toString());
		for (Transaction t : this.transactions) {

			Log.e(Account.class.getName(), t.getCreateDate().toString());
			if (t.getCreateDate().getTime() >= form.getTime()
					&& t.getCreateDate().getTime() <= to.getTime()) {
				Log.e(Account.class.getName() + "for", t.getCreateDate()
						.toString());
				transactions.add(t);
			}
		}

		this.createDate = form;
		// this.transactionGroupBy = transactions;

		return transactions;

	}

	public String getAccountType(Context context) {

		ConfigurationRepository configurationRepository = new ConfigurationRepository(
				context);

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				context, configurationRepository.getById(1).getLocale());

		return accountTypeRepository.getById(this.getAccountTypeId()).getName();

	}

	public String getAccountType(Context context, Locale locale) {

		AccountTypeRepository accountTypeRepository = new AccountTypeRepository(
				context, locale);

		return accountTypeRepository.getById(locale, this.getAccountTypeId())
				.getName();

	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

}
