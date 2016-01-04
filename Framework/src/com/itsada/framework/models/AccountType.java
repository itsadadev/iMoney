package com.itsada.framework.models;

import java.util.ArrayList;

public class AccountType extends Entity {

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Fields-------------------------------- */
	/* ------------------------------------------------------------------------- */
	private String name;
	private ArrayList<Account> accounts;
	private boolean isVisible;

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Constructors-------------------------- */
	/* ------------------------------------------------------------------------- */
	public AccountType() {
		this.name = "";
		this.accounts = new ArrayList<Account>();
		this.isVisible = true;
	}

	public AccountType(String name) {
		this.name = name;
		this.accounts = new ArrayList<Account>();
		this.isVisible = true;
	}

	public AccountType(String name, ArrayList<Account> accounts) {
		this.name = name;
		this.accounts = accounts;
		this.isVisible = true;
	}

	public AccountType(int id, String name, ArrayList<Account> accounts) {
		super(id);
		this.name = name;
		this.accounts = accounts;
		this.isVisible = true;
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Setter and getter--------------------- */
	/* ------------------------------------------------------------------------- */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Methods------------------------------- */
	/* ------------------------------------------------------------------------- */
	public double getBalances() {
		double b = 0d;
		for (Account ac : this.accounts) {
			b += ac.getBalance();
		}
		return b;
	}

	public static double getAssertPercent(ArrayList<AccountType> accountTypes) {

		double sum = 0;
		for (AccountType at : accountTypes) {
			if (at.getId() <= 3) {
				sum += at.getBalances();
			}
		}

		return sum;
	}

	public static double getLiabilitiesPercent(ArrayList<AccountType> accountTypes) {

		double sum = 0;
		for (AccountType at : accountTypes) {
			if (at.getId() > 3) {
				sum += at.getBalances();
			}
		}

		return sum;
	}

	public int getAccountSize() {
		if (this.accounts == null)
			return 0;
		return this.accounts.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
