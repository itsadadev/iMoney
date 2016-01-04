package com.itsada.framework.models;

public class Template extends Transaction {

	private int transactionId;

	public Template() {

	}

	public Template(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public Template(Transaction transaction) {
		this.transactionId = transaction.getId();
		
		super.setAccount(transaction.getAccount());
		super.setName(transaction.getName());
		super.setTransactionType(transaction.getTransactionType());
		super.setAmount(transaction.getAmount());
		super.setCreateDate(transaction.getCreateDate());
		super.setTransactionCategory(transaction.getTransactionCategory());
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

}
