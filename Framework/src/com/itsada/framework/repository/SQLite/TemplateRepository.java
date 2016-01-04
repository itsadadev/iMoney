package com.itsada.framework.repository.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.itsada.framework.models.Template;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

public class TemplateRepository extends SqliteRepository implements
		IRepository<Template> {

	public TemplateRepository(Context context, Locale locale) {
		super.locale = locale;
		super.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(Template entity) {

		long id = 0;

		try {

			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TEMPLATE_TRANSACTION_ID,
					entity.getTransactionId());

			values.put(DatabaseHelper.TEMPLATE_NAME, entity.getName());
			values.put(DatabaseHelper.TEMPLATE_AMOUNT, entity.getAmount());
			values.put(DatabaseHelper.TEMPLATE_CREATE_DATE, entity
					.getCreateDate().getTime());
			values.put(DatabaseHelper.TEMPLATE_ACCOUNT_ID, entity.getAccount()
					.getId());
			values.put(DatabaseHelper.TEMPLATE_TYPE,
					entity.getTransactionType());
			values.put(DatabaseHelper.TEMPLATE_CATEGORY_ID, entity
					.getTransactionCategory().getId());
			values.put(DatabaseHelper.TRANSACTION_DATE, entity.getDate());

			id = db.insert(DatabaseHelper.TEMPLATE_TABLE, null, values);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return id;
	}

	@Override
	public void update(Template entity) {

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.TEMPLATE_NAME, entity.getName());
			values.put(DatabaseHelper.TEMPLATE_AMOUNT, entity.getAmount());
			values.put(DatabaseHelper.TEMPLATE_CREATE_DATE, entity
					.getCreateDate().getTime());
			values.put(DatabaseHelper.TEMPLATE_ACCOUNT_ID, entity.getAccount()
					.getId());
			values.put(DatabaseHelper.TEMPLATE_TYPE,
					entity.getTransactionType());
			values.put(DatabaseHelper.TEMPLATE_CATEGORY_ID, entity
					.getTransactionCategory().getId());
			values.put(DatabaseHelper.TEMPLATE_CATEGORY_ID, entity
					.getTransactionCategory().getId());

			db.update(DatabaseHelper.TEMPLATE_TABLE, values,
					DatabaseHelper.TEMPLATE_ID + "=?",
					new String[] { String.valueOf(entity.getId()) });

		} catch (Exception e) {

		} finally {
			db.close();
		}

	}

	@Override
	public void delete(int id) {
		try {

			db.delete(DatabaseHelper.TEMPLATE_TABLE, DatabaseHelper.TEMPLATE_ID
					+ " = " + id, null);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}

	}

	@Override
	public ArrayList<Template> getAll() {
		ArrayList<Template> templates = new ArrayList<Template>();

		try {

			// Select All Query
			String selectQuery = "SELECT t.id, t.transaction_id, t.name, t.amount, t.createDate, t.account_id, "
					+ " t.transactionType, t.transactionCategory_id, c.nameTh, c.nameEn, c.icon FROM "
					+ DatabaseHelper.TEMPLATE_TABLE
					+ " t "
					+ "inner join tb_account a on t.account_id = a.id "
					+ "inner join tb_transactionCategory c on t.transactionCategory_id = c.id order by t.createDate desc";
			Cursor cursor = db.rawQuery(selectQuery, null);

			AccountRepository accountRepository = new AccountRepository(
					this.context, locale);
			// TransactionCategoryRepository transactionCategoryRepository = new
			// TransactionCategoryRepository(
			// this.context, locale);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Template template = new Template();
					template.setId(cursor.getInt(0));
					template.setTransactionId(cursor.getInt(1));

					template.setName(cursor.getString(2));
					template.setAmount(cursor.getDouble(3));
					template.setCreateDate(new Date(cursor.getLong(4)));
					template.setAccount(accountRepository.getById(cursor
							.getInt(5)));
					template.setTransactionType(cursor.getString(6));

					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(7));

					if (super.locale.getLanguage().equals("en"))
						category.setName(cursor.getString(9));
					else
						category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(10));

					template.setTransactionCategory(category);

					templates.add(template);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.d("", e.getMessage());
		} finally {
			db.close();
		}
		return templates;
	}

	public ArrayList<Transaction> getTransactions() {

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM "
					+ DatabaseHelper.TEMPLATE_TABLE;
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					TransactionRepository transactionRepository = new TransactionRepository(
							context, locale);
					transactions.add(transactionRepository.getById(cursor
							.getInt(1)));

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
	public Template getById(int id) {
		Template template = new Template();

		try {
			String selectQuery = "SELECT t.id, t.transaction_id, t.name, t.amount, t.createDate, t.account_id, "
					+ " t.transactionType, t.transactionCategory_id, c.nameTh, c.nameEn, c.icon FROM "
					+ DatabaseHelper.TEMPLATE_TABLE
					+ " t "
					+ "inner join tb_account a on t.account_id = a.id "
					+ "inner join tb_transactionCategory c on t.transactionCategory_id = c.id "
					+ "where t.id=" + id;

			// String selectQuery = "SELECT * FROM "
			// + DatabaseHelper.TEMPLATE_TABLE + " WHERE "
			// + DatabaseHelper.TEMPLATE_ID + "=" + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			AccountRepository accountRepository = new AccountRepository(
					this.context, locale);
			// TransactionCategoryRepository transactionCategoryRepository = new
			// TransactionCategoryRepository(
			// this.context, locale);

			if (cursor.moveToFirst()) {

				do {
					template.setId(cursor.getInt(0));
					template.setTransactionId(cursor.getInt(1));

					template.setName(cursor.getString(2));
					template.setAmount(cursor.getDouble(3));
					template.setCreateDate(new Date(cursor.getLong(4)));
					template.setAccount(accountRepository.getById(cursor
							.getInt(5)));
					template.setTransactionType(cursor.getString(6));
					// template.setTransactionCategory(transactionCategoryRepository
					// .getById(cursor.getInt(7)));
					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(7));

					if (super.locale.getLanguage().equals("en"))
						category.setName(cursor.getString(9));
					else
						category.setName(cursor.getString(8));
					category.setIcon(cursor.getInt(10));

					template.setTransactionCategory(category);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {

		} finally {
			db.close();
		}

		return template;
	}

}
