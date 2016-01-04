package com.itsada.framework.repository.SQLite;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.itsada.framework.models.AccountType;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

public class AccountTypeRepository extends SqliteRepository implements
		IRepository<AccountType> {

	public AccountTypeRepository(Context context, Locale locale) {
		super.locale = locale;
		super.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(AccountType entity) {

		try {

		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}

		return 0;

	}

	@Override
	public void update(AccountType entity) {

		try {
			
			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.ACCOUNT_TYPE_NAME_En, entity.getName());
			values.put(DatabaseHelper.ACCOUNT_TYPE_NAME_Th, entity.getName());
			values.put(DatabaseHelper.ACCOUNT_TYPE_VISIBLE, entity.isVisible());			

			db.update(DatabaseHelper.ACCOUNT_TYPE_TABLE, values,
					DatabaseHelper.ACCOUNT_TYPE_ID + "=?",
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

		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}

	}

	@Override
	public ArrayList<AccountType> getAll() {

		ArrayList<AccountType> accountTypes = new ArrayList<AccountType>();

		try {

			String selectQuery = "SELECT  * FROM "
					+ DatabaseHelper.ACCOUNT_TYPE_TABLE + " ORDER BY "
					+ DatabaseHelper.ACCOUNT_TYPE_ID;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					AccountType accountType = new AccountType();
					accountType.setId(cursor.getInt(0));
					accountType.setName(cursor.getString(1));
					accountType.setAccounts(new AccountRepository(context,
							locale).getAccountByType(cursor.getInt(0)));
					accountType.setVisible(cursor.getInt(3) == 1);

					accountTypes.add(accountType);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
		return accountTypes;
	}

	public ArrayList<AccountType> getAll(Locale locale) {

		ArrayList<AccountType> accountTypes = new ArrayList<AccountType>();

		try {

			String selectQuery = "SELECT  * FROM "
					+ DatabaseHelper.ACCOUNT_TYPE_TABLE + " ORDER BY "
					+ DatabaseHelper.ACCOUNT_TYPE_ID;

			Cursor cursor = db.rawQuery(selectQuery, null);

			Log.w("", locale.getLanguage());

			if (cursor.moveToFirst()) {
				do {

					AccountType accountType = new AccountType();
					accountType.setId(cursor.getInt(0));
					if (locale.getLanguage().equals("en")) {
						accountType.setName(cursor.getString(2));
					} else {
						accountType.setName(cursor.getString(1));
					}
					accountType.setAccounts(new AccountRepository(context,
							locale).getAccountByType(cursor.getInt(0)));

					accountType.setVisible(cursor.getInt(3) == 1);
					accountTypes.add(accountType);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
		return accountTypes;
	}

	@Override
	public AccountType getById(int id) {

		AccountType accountType = new AccountType();

		try {

			String selectQuery = "SELECT * FROM "
					+ DatabaseHelper.ACCOUNT_TYPE_TABLE + " where id = " + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					accountType.setId(cursor.getInt(0));
					accountType.setName(cursor.getString(1));
					accountType.setName(cursor.getString(2));
					accountType.setAccounts(new AccountRepository(context,
							locale).getAccountByType(cursor.getInt(0)));
					accountType.setVisible(cursor.getInt(3) == 1);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
		return accountType;
	}

	public AccountType getById(Locale locale, int id) {

		AccountType accountType = new AccountType();

		try {

			String selectQuery = "SELECT * FROM "
					+ DatabaseHelper.ACCOUNT_TYPE_TABLE + " where id = " + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {

					accountType.setId(cursor.getInt(0));
					if (locale.getLanguage().equals("en")) {
						accountType.setName(cursor.getString(2));
					} else {
						accountType.setName(cursor.getString(1));
					}
					accountType.setAccounts(new AccountRepository(context,
							locale).getAccountByType(cursor.getInt(0)));
					accountType.setVisible(cursor.getInt(3) == 1);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
		return accountType;
	}
	
	

}
