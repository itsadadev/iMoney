package com.itsada.framework.repository.SQLite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

public class AccountRepository extends SqliteRepository implements
		IRepository<Account> {

	public AccountRepository(Context context, Locale locale) {
		super.locale = locale;
		super.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(Account entity) {

		long id = 0;

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.ACCOUNT_NAME, entity.getName());
			values.put(DatabaseHelper.ACCOUNT_BALANCE, entity.getBalance());
			values.put(DatabaseHelper.ACCOUNT_CREATE_DATE, entity
					.getCreateDate().getTime());
			values.put(DatabaseHelper.ACCOUNT_ICON, entity.getIcon());
			values.put(DatabaseHelper.ACCOUNT_ACCOUNT_TYPE_ID,
					entity.getAccountTypeId());
			values.put(DatabaseHelper.ACCOUNT_IS_HIDE, entity.isHide());
			values.put(DatabaseHelper.ACCOUNT_COLOR, entity.getColor());

			if (db != null)
				id = db.insert(DatabaseHelper.ACCOUNT_TABLE, null, values);

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return id;
	}

	@Override
	public void update(Account entity) {

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.ACCOUNT_NAME, entity.getName());
			values.put(DatabaseHelper.ACCOUNT_BALANCE, entity.getBalance());
			values.put(DatabaseHelper.ACCOUNT_ACCOUNT_TYPE_ID,
					entity.getAccountTypeId());
			values.put(DatabaseHelper.ACCOUNT_ICON, entity.getIcon());
			values.put(DatabaseHelper.ACCOUNT_IS_HIDE, entity.isHide());
			values.put(DatabaseHelper.ACCOUNT_COLOR, entity.getColor());

			db.update(DatabaseHelper.ACCOUNT_TABLE, values,
					DatabaseHelper.ACCOUNT_ID + "=?",
					new String[] { String.valueOf(entity.getId()) });

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
	}

	@Override
	public void delete(int id) {

		try {

			db.delete(DatabaseHelper.ACCOUNT_TABLE, DatabaseHelper.ACCOUNT_ID
					+ " = " + id, null);

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
	}

	@Override
	public ArrayList<Account> getAll() {

		ArrayList<Account> list = new ArrayList<Account>();

		try {

			// Select All Query
			String selectQuery = "SELECT * FROM  tb_account ORDER BY createDate";
			Cursor cursor = db.rawQuery(selectQuery, null);

			TransactionRepository transactionRepository = new TransactionRepository(
					context, locale);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Account account = new Account();
					account.setId(cursor.getInt(0));
					account.setName(cursor.getString(1));
					account.setBalance(cursor.getDouble(2));
					account.setCreateDate(new Date(cursor.getLong(3)));
					account.setIcon(cursor.getInt(5));
					account.setAccountTypeId(cursor.getInt(6));
					account.setHide(cursor.getInt(7) == 1);
					// account.setColor(Integer.parseInt(cursor.getString(8)));
					account.setColor(cursor.getString(8));
					// account.setColor(Integer.parseInt(cursor.getString(8).replaceFirst("^#",""),
					// 16));

					account.setTransactions(transactionRepository
							.getTransactionByAccountId(cursor.getInt(0)));

					list.add(account);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return list;
	}

	@Override
	public Account getById(int id) {

		Account account = new Account();

		try {
			String selectQuery = "SELECT * FROM "
					+ DatabaseHelper.ACCOUNT_TABLE + " WHERE "
					+ DatabaseHelper.ACCOUNT_ID + "=" + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				do {
					account.setId(cursor.getInt(0));
					account.setName(cursor.getString(1));
					account.setBalance(cursor.getDouble(2));
					account.setCreateDate(new Date(cursor.getLong(3)));
					account.setIcon(cursor.getInt(5));
					account.setAccountTypeId(cursor.getInt(6));
					account.setHide(cursor.getInt(7) == 1);
					// account.setColor(Integer.parseInt(cursor.getString(8).replaceFirst("^#",""),
					// 16));

					TransactionRepository transactionRepository = new TransactionRepository(
							context, locale);
					account.setTransactions(transactionRepository
							.getTransactionByAccountId(cursor.getInt(0)));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return account;
	}

	public ArrayList<Account> getAccountByType(int id) {

		ArrayList<Account> accounts = new ArrayList<Account>();

		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM "
					+ DatabaseHelper.ACCOUNT_TABLE + " WHERE "
					+ DatabaseHelper.ACCOUNT_ACCOUNT_TYPE_ID + "=" + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			TransactionRepository transactionRepository = new TransactionRepository(
					context, locale);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					Account account = new Account();
					account.setId(cursor.getInt(0));
					account.setName(cursor.getString(1));
					account.setBalance(cursor.getDouble(2));
					account.setCreateDate(new Date(cursor.getLong(3)));
					account.setIcon(cursor.getInt(5));
					account.setAccountTypeId(cursor.getInt(6));
					account.setHide(cursor.getInt(7) == 1);
					account.setColor(cursor.getString(8));
					// account.setColor(Integer.parseInt(cursor.getString(8).replaceFirst("^#",""),
					// 16));

					account.setTransactions(transactionRepository
							.getTransactionByAccountId(cursor.getInt(0)));

					accounts.add(account);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return accounts;
	}

	public long addTransaction(Account currentAccount, Transaction transaction) {

		long tId = 0;
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

			TransactionRepository transactionRepository = new TransactionRepository(
					context, locale);
			tId = transactionRepository.add(transaction);

		} catch (Exception e) {
			db.close();
		} finally {
			// Save change account
			this.update(currentAccount);

		}

		return tId;
	}

	public void updateTransaction(int accountId, Transaction transaction) {

		Account account = null;

		AccountRepository accountRepository = new AccountRepository(context,
				locale);
		account = accountRepository.getById(accountId);
		double accBalance = account.getBalance();
		double newBalance = 0d;
		Transaction oldTransaction = account.getTransactionById(transaction
				.getId());

		if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Income.name())) {
			newBalance = accBalance
					- (oldTransaction.getAmount() - transaction.getAmount());
		} else if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Expenses.name())) {
			newBalance = accBalance
					+ (oldTransaction.getAmount() - transaction.getAmount());
		}

		account.setBalance(newBalance);

		// Save transaction
		try {

			TransactionRepository transactionRepository = new TransactionRepository(
					context, locale);
			transactionRepository.update(transaction);

			account.setUpdateDate(Calendar.getInstance().getTime());

		} catch (Exception e) {
			db.close();
		} finally {
			// Save change account
			this.update(account);
		}
	}

	public void transfer(Account accountForm, Account accountTo, double amount,
			Date date, String note) {

		TransactionRepository transactionFormRepository = null;
		TransactionRepository transactionToRepository = null;

		TransactionCategory categoryForm = new TransactionCategoryRepository(
				context, locale).getByType(
				TransactionCategory.Type.TransferForm).get(0);
		TransactionCategory categoryTo = new TransactionCategoryRepository(
				context, locale).getByType(TransactionCategory.Type.TransferTo)
				.get(0);

		Transaction transactionForm = new Transaction();
		transactionForm.setAmount(amount);
		transactionForm.setName(note);
		transactionForm.setCreateDate(date);
		transactionForm.setTransactionType(TransactionType.TransferForm.name());
		transactionForm.setAccount(accountForm);
		transactionForm.setTransactionCategory(categoryForm);

		Transaction transactionTo = new Transaction();
		transactionTo.setAmount(amount);
		transactionTo.setName(note);
		transactionTo.setCreateDate(date);
		transactionTo.setTransactionType(TransactionType.TransferTo.name());
		transactionTo.setAccount(accountTo);
		transactionTo.setTransactionCategory(categoryTo);

		accountForm.setBalance(accountForm.getBalance() - amount);
		accountTo.setBalance(accountTo.getBalance() + amount);

		// Save transaction
		try {

			transactionFormRepository = new TransactionRepository(context,
					locale);
			transactionToRepository = new TransactionRepository(context, locale);

			transactionFormRepository.add(transactionForm);
			transactionToRepository.add(transactionTo);

			accountForm.setUpdateDate(Calendar.getInstance().getTime());
			accountTo.setUpdateDate(Calendar.getInstance().getTime());

		} catch (Exception e) {
			db.close();
		} finally {

			transactionFormRepository = null;
			transactionToRepository = null;

			// Save change account
			try {
				this.update(accountForm);

				AccountRepository repository = new AccountRepository(context,
						locale);
				repository.update(accountTo);
				// this.update(accountTo);
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}

	}

	public void deleteTransaction(int accountId, Transaction transaction) {

		Account account = null;

		AccountRepository accountRepository = new AccountRepository(context,
				locale);
		account = accountRepository.getById(accountId);
		double accBalance = account.getBalance();
		double newBalance = 0d;

		if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Income.name())) {
			newBalance = accBalance - transaction.getAmount();
		} else if (transaction.getTransactionType().equals(
				TransactionCategory.Type.Expenses.name())) {
			newBalance = accBalance + transaction.getAmount();
		}

		account.setBalance(newBalance);
		// Save transaction
		try {

			TransactionRepository transactionRepository = new TransactionRepository(
					context, locale);
			transactionRepository.delete(transaction.getId());

		} catch (Exception e) {
			db.close();
		} finally {
			// Save change account
			this.update(account);
		}

	}
}
