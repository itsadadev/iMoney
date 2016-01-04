package com.itsada.framework.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

import com.itsada.framework.R;

@SuppressLint("SimpleDateFormat")
public class Transaction extends Entity {

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Enum---------------------------------- */
	/* ------------------------------------------------------------------------- */
	public enum GroupBy {
		None, Category, Day, Type
	}

	public enum TransactionType {
		Income, Expenses, TransferForm, TransferTo
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Fields-------------------------------- */
	/* ------------------------------------------------------------------------- */
	private String name;
	private Date createDate;
	private double amount;

	private String transactionNo;
	private Account account;
	private String transactionType;
	private TransactionCategory transactionCategory;
	private String date;
	private String latitude;
	private String longitude;

	private int icon;

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Constructor--------------------------- */
	/* ------------------------------------------------------------------------- */
	public Transaction() {

	}

	public Transaction(Account account, String name, String type,
			double amount, Date date, TransactionCategory category) {
		this.account = account;
		this.name = name;
		this.transactionType = type;
		this.amount = amount;
		this.createDate = date;
		this.transactionCategory = category;
		this.date = new SimpleDateFormat("dd/MM/yyyy").format(createDate);
	}

	public Transaction(Account account, String name, String type,
			double amount, Date date, TransactionCategory category,
			double latitude, double longitude) {
		this.account = account;
		this.name = name;
		this.transactionType = type;
		this.amount = amount;
		this.createDate = date;
		this.transactionCategory = category;
		this.date = new SimpleDateFormat("dd/MM/yyyy").format(createDate);
		this.latitude = String.valueOf(latitude);
		this.longitude = String.valueOf(longitude);
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Getter and setter--------------------- */
	/* ------------------------------------------------------------------------- */
	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public TransactionType getTransactionType(String name) {

		TransactionType type = null;
		if (name.equals("Receipt")) {
			type = TransactionType.Income;
		} else if (name.equals("Expenditure")) {
			type = TransactionType.Expenses;
		} else if (name.equals("TransferForm")) {
			type = TransactionType.TransferForm;
		} else if (name.equals("TransferTo")) {
			type = TransactionType.TransferTo;
		}
		return type;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		// if(this.name.equals(""))
		// return "...";
		// if(this.name.length() > 20)
		// return this.name.substring(0, 20) + " ...";
		return name;
	}

	public String getShotName() {
		if (this.name.equals(""))
			return "...";
		if (this.name.length() > 20)
			return this.name.substring(0, 20) + " ...";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}

	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}

	public int getIcon() {

		try {

			if (this.transactionCategory == null) {
				if (this.transactionType.equals(TransactionType.Income))
					return R.drawable.ic_mood_black_36dp;
				else
					return R.drawable.ic_mood_bad_black_36dp;
			} else {
				if (this.transactionCategory.getIcon() < 0) {
					if (this.transactionType.equals(TransactionType.Income))
						return R.drawable.ic_mood_black_36dp;
					else
						return R.drawable.ic_mood_bad_black_36dp;
				}
				return this.transactionCategory.getIcon();
			}

		} catch (Exception e) {
			Log.d(Transaction.class.getName(), e.getMessage());
		}
		return icon;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	public String getLatitude() {
		if (this.latitude == null)
			return "0";

		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		
		if (this.longitude == null)
			return "0";

		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
