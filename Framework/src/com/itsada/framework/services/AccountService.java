package com.itsada.framework.services;

import java.util.Locale;

import android.content.Context;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;

public class AccountService {

	private static AccountTypeRepository accountTypeRepository;
	private static AccountRepository accountRepository;
	private static TransactionCategoryRepository transactionCategoryRepository;
	private static TransactionRepository transactionRepository;
	private Context context;
	private Locale locale;

	public AccountService(Context context, Locale locale) {
		this.context = context;
		this.locale = locale;
	}

	public AccountTypeRepository getAccountTypeRepo() {
		if (accountTypeRepository == null) {
			accountTypeRepository = new AccountTypeRepository(context, locale);
		}
		return accountTypeRepository;
	}

	public AccountRepository getAccountRepo() {
		if (accountRepository == null) {
			accountRepository = new AccountRepository(context, locale);
		}
		return accountRepository;
	}

	public TransactionCategoryRepository getTransactionCategoryRepo() {
		if (transactionCategoryRepository == null) {
			transactionCategoryRepository = new TransactionCategoryRepository(
					context, locale);
		}
		return transactionCategoryRepository;
	}

	public TransactionRepository getTransactionRepo() {
		if (transactionRepository == null) {
			transactionRepository = new TransactionRepository(context, locale);
		}
		return transactionRepository;
	}

	public void addTransaction(Account currentAccount, Transaction transaction) {

		// Update current account
		if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Income.name())) {
			currentAccount.setBalance(currentAccount.getBalance()
					+ transaction.getAmount());
		} else if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Expenses.name())) {
			currentAccount.setBalance(currentAccount.getBalance()
					- transaction.getAmount());
		}

		// Save transaction
		try {

			transactionRepository.add(transaction);

		} catch (Exception e) {

		} finally {

			// Save change account
			accountRepository.update(currentAccount);
		}

	}

}
