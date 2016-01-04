package com.itsada.framework.models;



public class TransactionCategory extends Entity {

	public enum Type {
		Income, Expenses, TransferForm, TransferTo
	}

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Fields-------------------------------- */
	/* ------------------------------------------------------------------------- */
	private String name;
	private int icon;
	private boolean isNeed;
	private String type;
	private int color;
	private int hit;
	private String hitDate;
	private TransactionGroup transactionGroup;

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Constructor--------------------------- */
	/* ------------------------------------------------------------------------- */
	public TransactionCategory() {

	}


	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Getter and setter--------------------- */
	/* ------------------------------------------------------------------------- */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}
	
	public int getHit() {
		return hit;
	}


	public void setHit(int hit) {
		this.hit = hit;
	}


	public String getHitDate() {
		return hitDate;
	}


	public void setHitDate(String hitDate) {
		this.hitDate = hitDate;
	}


	public boolean isNeed() {
		return isNeed;
	}


	public void setNeed(boolean isNeed) {
		this.isNeed = isNeed;
	}


	public TransactionGroup getTransactionGroup() {
		return transactionGroup;
	}


	public void setTransactionGroup(TransactionGroup transactionGroup) {
		this.transactionGroup = transactionGroup;
	}
}
