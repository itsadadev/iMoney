package com.itsada.framework.repository.SQLite;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.itsada.framework.models.Configuration;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

public class ConfigurationRepository extends SqliteRepository implements
		IRepository<Configuration> {

	public ConfigurationRepository(Context context) {

		super.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(Configuration entity) {

		long id = 0;
		
		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.CONFIGURATION_LOCALE, entity.getLocale()
					.getLanguage());
			values.put(DatabaseHelper.CONFIGURATION_CURRENCY,
					entity.getCurrency());
			values.put(DatabaseHelper.CONFIGURATION_DATE_FORMAT,
					entity.getDateFormat());
			values.put(DatabaseHelper.CONFIGURATION_TIME_FORMAT,
					entity.getTimeFormat());
			values.put(DatabaseHelper.CONFIGURATION_MONEY_FORMAT,
					entity.getMoneyFormat());
			values.put(DatabaseHelper.CONFIGURATION_PASSWORD,
					entity.getPassword());

			if (db != null)
				id = db.insert(DatabaseHelper.CONFIGURATION_TABLE, null, values);

		} catch (Exception e) {
			Log.e("Add config", e.getMessage());
		} finally {
			db.close();
		}

		return id;
	}

	@Override
	public void update(Configuration entity) {

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.CONFIGURATION_LOCALE, entity.getLocale()
					.getLanguage());
			values.put(DatabaseHelper.CONFIGURATION_CURRENCY,
					entity.getCurrency());
			values.put(DatabaseHelper.CONFIGURATION_DATE_FORMAT,
					entity.getDateFormat());
			values.put(DatabaseHelper.CONFIGURATION_TIME_FORMAT,
					entity.getTimeFormat());
			values.put(DatabaseHelper.CONFIGURATION_MONEY_FORMAT,
					entity.getMoneyFormat());
			values.put(DatabaseHelper.CONFIGURATION_PASSWORD,
					entity.getPassword());

			db.update(DatabaseHelper.CONFIGURATION_TABLE, values,
					DatabaseHelper.CONFIGURATION_ID + "=?",
					new String[] { String.valueOf(entity.getId()) });

		} catch (Exception e) {
			Log.e("Update config", e.getMessage());
		} finally {
			db.close();
		}

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Configuration> getAll() {

		return null;
	}

	@Override
	public Configuration getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Configuration getCurrentConfig(){

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.CONFIGURATION_TABLE + " WHERE "
					+ DatabaseHelper.CONFIGURATION_ID + "= 1";

			Cursor cursor = db.rawQuery(selectQuery, null);
			Configuration configuration = null;
			if (cursor.moveToFirst()) {

				do {
					configuration = new Configuration();
					configuration.setId(cursor.getInt(0));
					configuration.setLocale(new Locale(cursor.getString(1)));
					configuration.setCurrency(cursor.getString(2));
					configuration.setDateFormat(cursor.getString(3));
					configuration.setTimeFormat(cursor.getString(4));
					configuration.setMoneyFormat(cursor.getString(5));
					configuration.setPassword(cursor.getString(6));
				} while (cursor.moveToNext());
				
				return configuration;
			}			

		} catch (Exception e) {
			Log.e("Get current config", e.getMessage());
		} finally {
			db.close();
		}

		return null;
	}
}
