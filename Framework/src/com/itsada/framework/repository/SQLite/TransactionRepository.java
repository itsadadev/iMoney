package com.itsada.framework.repository.SQLite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

@SuppressLint("SimpleDateFormat")
public class TransactionRepository extends SqliteRepository implements
		IRepository<Transaction> {

	public TransactionRepository(Context context, Locale locale) {
		super.locale = locale;
		super.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(Transaction entity) {

		long id = 0;

		try {

			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TRANSACTION_NAME, entity.getName());
			values.put(DatabaseHelper.TRANSACTION_AMOUNT, entity.getAmount());
			values.put(DatabaseHelper.TRANSACTION_CREATE_DATE, entity
					.getCreateDate().getTime());
			values.put(DatabaseHelper.TRANSACTION_ACCOUNT_ID, entity
					.getAccount().getId());
			values.put(DatabaseHelper.TRANSACTION_TYPE,
					entity.getTransactionType());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_ID, entity
					.getTransactionCategory().getId());
			values.put(DatabaseHelper.TRANSACTION_DATE, entity.getDate());

			values.put(DatabaseHelper.TRANSACTION_LATITUDE,
					entity.getLatitude());
			values.put(DatabaseHelper.TRANSACTION_LONGITUDE,
					entity.getLongitude());

			id = db.insert(DatabaseHelper.TRANSACTION_TABLE, null, values);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return id;
	}

	@Override
	public void update(Transaction entity) {

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.TRANSACTION_NAME, entity.getName());
			values.put(DatabaseHelper.TRANSACTION_AMOUNT, entity.getAmount());
			values.put(DatabaseHelper.TRANSACTION_CREATE_DATE, entity
					.getCreateDate().getTime());
			values.put(DatabaseHelper.TRANSACTION_ACCOUNT_ID, entity
					.getAccount().getId());
			values.put(DatabaseHelper.TRANSACTION_TYPE,
					entity.getTransactionType());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_ID, entity
					.getTransactionCategory().getId());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_ID, entity
					.getTransactionCategory().getId());

			values.put(DatabaseHelper.TRANSACTION_LATITUDE,
					entity.getLatitude());
			values.put(DatabaseHelper.TRANSACTION_LONGITUDE,
					entity.getLongitude());

			db.update(DatabaseHelper.TRANSACTION_TABLE, values,
					DatabaseHelper.TRANSACTION_ID + "=?",
					new String[] { String.valueOf(entity.getId()) });

		} catch (Exception e) {

		} finally {
			db.close();
		}

	}

	@Override
	public void delete(int id) {

		try {

			db.delete(DatabaseHelper.TRANSACTION_TABLE,
					DatabaseHelper.TRANSACTION_ID + " = " + id, null);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
	}

	@Override
	public ArrayList<Transaction> getAll() {

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM "
					+ DatabaseHelper.TRANSACTION_TABLE + " ORDER BY "
					+ DatabaseHelper.TRANSACTION_CREATE_DATE + " desc";
			Cursor cursor = db.rawQuery(selectQuery, null);

			AccountRepository accountRepository = new AccountRepository(
					this.context, locale);
			TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
					this.context, locale);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setAccount(accountRepository.getById(cursor
							.getInt(4)));
					transaction.setTransactionType(cursor.getString(5));
					transaction
							.setTransactionCategory(transactionCategoryRepository
									.getById(cursor.getInt(6)));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					transaction.setLatitude(cursor.getString(8));
					transaction.setLongitude(cursor.getString(9));

					transactions.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("", e.getMessage());
		} finally {
			db.close();
		}
		return transactions;
	}

	@Override
	public Transaction getById(int id) {

		Transaction transaction = new Transaction();
		String selectQuery = "";

		try {

			if (super.locale.getLanguage().equals("en"))

				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, a.id, a.icon, a.name, t.latitude, t.longitude, a.accountType_id FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " INNER JOIN tb_account a"
						+ " ON t.account_id = a.id"
						+ " where t.id = "
						+ id
						+ " ORDER BY t.createDate desc";
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, a.id, a.icon, a.name, t.latitude, t.longitude, a.accountType_id FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " INNER JOIN tb_account a"
						+ " ON t.account_id = a.id"
						+ " where t.id = "
						+ id
						+ " ORDER BY t.createDate desc";

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					Account account = new Account();
					account.setId(cursor.getInt(11));
					account.setIcon(cursor.getInt(12));
					account.setName(cursor.getString(13));
					account.setAccountTypeId(cursor.getInt(14));
					transaction.setAccount(account);

					transaction.setLatitude(cursor.getString(17));
					transaction.setLongitude(cursor.getString(18));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("Transaction getById", e.getMessage());
		} finally {
			db.close();
		}
		return transaction;
	}

	// public ArrayList<Transaction> getTransactionGroupByDateInterval(
	// int accountId, long form, long to) {
	//
	// ArrayList<Transaction> list = new ArrayList<Transaction>();
	//
	// try {
	//
	// String selectQuery = "SELECT * FROM tb_transaction t"
	// + " where  t.createDate >= '"
	// + form
	// + "'"
	// + " and t.createDate <= '"
	// + to
	// + "'"
	// + " and t.account_id = '"
	// + accountId
	// + "'"
	// +
	// " GROUP BY date ((t.createDate-(ROUND(1969*(365+1/4-1/100+1/400),0)-2)*24*60*60*1E9/100)*100/1E9, 'unixepoch');";
	//
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// Transaction transaction = new Transaction();
	// transaction.setId(cursor.getInt(0));
	// transaction.setName(cursor.getString(1));
	// transaction.setAmount(cursor.getDouble(2));
	// transaction.setCreateDate(new Date(cursor.getLong(3)));
	// transaction.setTransactionType(cursor.getString(5));
	//
	// list.add(transaction);
	// } while (cursor.moveToNext());
	// }
	//
	// } catch (Exception e) {
	// Log.e("", e.getMessage());
	// } finally {
	// db.close();
	// }
	//
	// return list;
	// }

	public ArrayList<Transaction> getTransactionByInterval(int accountId,
			long form, long to) {

		ArrayList<Transaction> list = new ArrayList<Transaction>();
		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, t.latitude, t.longitude FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						+ " ORDER BY t.createDate desc";
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, t.latitude, t.longitude FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						+ " ORDER BY t.createDate desc";

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					transaction.setLatitude(cursor.getString(11));
					transaction.setLongitude(cursor.getString(12));

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<Transaction> getTransactionByAccountId(int id) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				// Select All Query
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " WHERE t.account_id =" + id;
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " WHERE t.account_id =" + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setType(cursor.getString(9));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return list;
	}

	public ArrayList<Transaction> getTransactionGroupByDate(int accountId,
			long form, long to, boolean asc) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		try {

			String selectQuery = "select t.id, t.name, t.amount, t.createDate, t.transactionType, Sum(t.amount) As Amount from tb_transaction t"
					+ " where  t.createDate >= '"
					+ form
					+ "'"
					+ " and t.createDate <= '"
					+ to
					+ "'"
					+ " and t.account_id = '"
					+ accountId
					+ "'"
					+ (asc ? " group by t.date ORDER BY t.createDate asc"
							: " group by t.date ORDER BY t.createDate desc");

			Log.d("sql getTransactionGroupByDate", selectQuery);

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(5));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(4));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionGroupByDate", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	@SuppressLint("SimpleDateFormat")
	public ArrayList<Transaction> getTransactionGroupByCateogory(int accountId,
			long form, long to, boolean asc) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameEn asc"
								: " group by t.transactionCategory_id ORDER BY c.nameEn desc");
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
								: " group by t.transactionCategory_id ORDER BY c.nameTh desc");
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(11));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionGroupByCateogory", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<Transaction> getTransactionGroupByCateogory(int accountId,
			long form, long to, TransactionType type, boolean asc) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  type = '"
						+ type.name()
						+ "'"
						+ " and t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameEn asc"
								: " group by t.transactionCategory_id ORDER BY c.nameEn desc");
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  type = '"
						+ type.name()
						+ "'"
						+ " and t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
								: " group by t.transactionCategory_id ORDER BY c.nameTh desc");

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(11));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionGroupByCateogory", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<Transaction> getTransactionGroupByExpends(int accountId,
			long form, long to, boolean asc) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where (c.type = 'Expenses'"// +
														// TransactionCategory.Type.Expenses.name()
														// + "'"
						+ " OR c.type = 'TransferForm')"// +
														// TransactionCategory.Type.TransferForm.name()
														// + "'"
						+ " and (t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "')"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameEn asc"
								: " group by t.transactionCategory_id ORDER BY c.nameEn desc");
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where (c.type = 'Expenses'"// +
														// TransactionCategory.Type.Expenses.name()
														// + "'"
						+ " OR c.type = 'TransferForm')"// +
														// TransactionCategory.Type.TransferForm.name()
														// + "'"
						+ " and (t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "')"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
								: " group by t.transactionCategory_id ORDER BY c.nameTh desc");

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(11));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionGroupByExpends", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public double getAmountGroupByExpends(long form, long to, boolean asc) {

		double amount = 0;
		try {

			String selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
					+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
					+ " INNER JOIN tb_transactionCategory c"
					+ " ON t.transactionCategory_id = c.id"
					+ " where (c.type = 'Expenses'"// +
													// TransactionCategory.Type.Expenses.name()
													// + "'"
					+ " OR c.type = 'TransferForm')"// +
													// TransactionCategory.Type.TransferForm.name()
													// + "'"
					+ " and (t.createDate >= '"
					+ form
					+ "'"
					+ " and t.createDate <= '"
					+ to
					+ "')"
					// + " group by t.transactionCategory_id ORDER BY c.name";
					+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
							: " group by t.transactionCategory_id ORDER BY c.nameTh desc");

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					amount += cursor.getDouble(11);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getAmountGroupByExpends", e.getMessage());
		} finally {
			db.close();
		}

		return amount;
	}

	public ArrayList<Transaction> getTransactionGroupByIncomes(int accountId,
			long form, long to, boolean asc) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where (c.type = 'Income'"// +
														// TransactionCategory.Type.Income.name()
														// + "'"
						+ " OR c.type = 'TransferTo')"// +
														// TransactionCategory.Type.TransferTo.name()
														// + "'"
						+ " and (t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "')"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameEn asc"
								: " group by t.transactionCategory_id ORDER BY c.nameEn desc");
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where (c.type = 'Income'"// +
														// TransactionCategory.Type.Income.name()
														// + "'"
						+ " OR c.type = 'TransferTo')"// +
														// TransactionCategory.Type.TransferTo.name()
														// + "'"
						+ " and (t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "')"
						// +
						// " group by t.transactionCategory_id ORDER BY c.name";
						+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
								: " group by t.transactionCategory_id ORDER BY c.nameTh desc");

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(11));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionGroupByCateogory", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public Double getAmountGroupByIncomes(long form, long to, boolean asc) {

		double amount = 0;

		try {

			String selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
					+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
					+ " INNER JOIN tb_transactionCategory c"
					+ " ON t.transactionCategory_id = c.id"
					+ " where (c.type = 'Income'"// +
													// TransactionCategory.Type.Income.name()
													// + "'"
					+ " OR c.type = 'TransferTo')"// +
													// TransactionCategory.Type.TransferTo.name()
													// + "'"
					+ " and (t.createDate >= '"
					+ form
					+ "'"
					+ " and t.createDate <= '"
					+ to
					+ "')"
					// + " group by t.transactionCategory_id ORDER BY c.name";
					+ (asc ? " group by t.transactionCategory_id ORDER BY c.nameTh asc"
							: " group by t.transactionCategory_id ORDER BY c.nameTh desc");

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					amount += cursor.getDouble(11);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getAmountGroupByCateogory", e.getMessage());
		} finally {
			db.close();
		}

		return amount;
	}

	public ArrayList<Transaction> getTransactionByDate(int accountId,
			String date) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.date = '"
						+ date
						+ "' and t.account_id = '"
						+ accountId
						+ "' ORDER BY t.createDate desc";
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.date = '"
						+ date
						+ "' and t.account_id = '"
						+ accountId
						+ "' ORDER BY t.createDate desc";

			Log.d("getTransactionByDate", selectQuery);
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionByDate", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<Transaction> getTransactionByCategoryInterval(
			int accountId, int categoryId, long form, long to) {

		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						+ " and t.transactionCategory_id = '"
						+ categoryId
						+ "'" + " ORDER BY t.createDate desc";
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " where  t.createDate >= '"
						+ form
						+ "'"
						+ " and t.createDate <= '"
						+ to
						+ "'"
						+ " and t.account_id = '"
						+ accountId
						+ "'"
						+ " and t.transactionCategory_id = '"
						+ categoryId
						+ "'" + " ORDER BY t.createDate desc";

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					transaction.setAmount(cursor.getDouble(2));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<Transaction> getTransactionSixMonth() {

		ArrayList<Transaction> list = new ArrayList<Transaction>();

		String selectQuery = "";
		try {

			if (super.locale.getLanguage().equals("en"))
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameEn, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " group by strftime('%m-%Y', createDate)";
			else
				selectQuery = "SELECT t.id, t.name, t.amount, t.createDate, t.account_id, t.transactionType, t.transactionCategory_id,"
						+ " c.id, c.nameTh, c.icon, c.type, Sum(t.amount) As Amount FROM tb_transaction t"
						+ " INNER JOIN tb_transactionCategory c"
						+ " ON t.transactionCategory_id = c.id"
						+ " group by strftime('%m-%Y', createDate)";

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Transaction transaction = new Transaction();
					transaction.setId(cursor.getInt(0));
					transaction.setName(cursor.getString(1));
					// transaction.setAmount(cursor.getDouble(2));
					transaction.setAmount(cursor.getDouble(11));
					transaction.setCreateDate(new Date(cursor.getLong(3)));
					transaction.setTransactionType(cursor.getString(5));
					transaction.setDate(new SimpleDateFormat("dd/MM/yyyy")
							.format(transaction.getCreateDate()));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(6));
					category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(9));
					category.setType(cursor.getString(10));
					transaction.setTransactionCategory(category);

					list.add(transaction);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionSixMonth", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

	public ArrayList<String> getTransactionHit() {
		ArrayList<String> list = new ArrayList<String>();

		try {

			String selectQuery = "select t.name from tb_transaction t group by t.name ORDER BY t.createDate asc";

			Log.d("", selectQuery);

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("getTransactionHit", e.getMessage());
		} finally {
			db.close();
		}

		return list;
	}

}
