package com.itsada.framework.repository.SQLite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.models.TransactionGroup;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

@SuppressLint("SimpleDateFormat")
public class TransactionCategoryRepository extends SqliteRepository implements
		IRepository<TransactionCategory> {

	public TransactionCategoryRepository(Context context, Locale locale) {
		super.locale = locale;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(TransactionCategory entity) {

		long id = 0;
		try {

			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_NAME_TH,
					entity.getName());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_NAME_EN,
					entity.getName());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_ICON,
					entity.getIcon());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_TYPE,
					entity.getType());

			values.put(DatabaseHelper.TRANSACTION_CATEGORY_HIT, 0);
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_HITDATE,
					new SimpleDateFormat("MM/yyyy").format(Calendar
							.getInstance().getTime()));

			values.put(DatabaseHelper.GROUP_ID, entity.getTransactionGroup()
					.getId());

			id = db.insert(DatabaseHelper.TRANSACTION_CATEGORY_TABLE, null,
					values);

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return id;
	}

	@Override
	public void update(TransactionCategory entity) {

		try {

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.TRANSACTION_CATEGORY_NAME_TH,
					entity.getName());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_NAME_EN,
					entity.getName());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_ICON,
					entity.getIcon());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_TYPE,
					entity.getType());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_IS_NEED,
					entity.isNeed() ? 1 : 0);

			values.put(DatabaseHelper.TRANSACTION_CATEGORY_HIT, entity.getHit());
			values.put(DatabaseHelper.TRANSACTION_CATEGORY_HITDATE,
					entity.getHitDate());
			values.put(DatabaseHelper.GROUP_ID, entity.getTransactionGroup()
					.getId());

			db.update(DatabaseHelper.TRANSACTION_CATEGORY_TABLE, values,
					DatabaseHelper.TRANSACTION_CATEGORY_ID + "=?",
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

			db.delete(DatabaseHelper.TRANSACTION_CATEGORY_TABLE,
					DatabaseHelper.TRANSACTION_CATEGORY_ID + " = " + id, null);

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}
	}

	@Override
	public ArrayList<TransactionCategory> getAll() {

		ArrayList<TransactionCategory> list = new ArrayList<TransactionCategory>();

		try {

			// Select All Query
			String selectQuery = "SELECT tc.id, tc.nameTh, tc.nameEn, tc.icon, tc.type, tc.color, tc.hit, tc.hitDate, tc.transactionGroup_id, tg.nameTh, tg.nameEn FROM "
					+ DatabaseHelper.TRANSACTION_CATEGORY_TABLE
					+ " tc"
					+ " inner join tb_transaction_group tg on tc.transactionGroup_id = tg.id order by tc.type desc";

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(0));
					category.setName(getNameByLocale(cursor));
					category.setIcon(cursor.getInt(3));
					category.setType(cursor.getString(4));
					category.setColor(Color.parseColor(cursor.getString(5)));

					category.setHit(cursor.getInt(6));
					category.setHitDate(cursor.getString(7));

					TransactionGroup group = new TransactionGroup();
					group.setName(getGroupNameByLocale(cursor));
					category.setTransactionGroup(group);

					list.add(category);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e("get :", e.getMessage());
		} finally {
			db.close();
		}
		return list;
	}

	@Override
	public TransactionCategory getById(int id) {

		TransactionCategory category = new TransactionCategory();

		try {
			String selectQuery = "SELECT tc.id, tc.nameTh, tc.nameEn, tc.icon, tc.type, tc.color, tc.hit, tc.hitDate, tc.transactionGroup_id, tg.nameTh, tg.nameEn FROM "
					+ DatabaseHelper.TRANSACTION_CATEGORY_TABLE
					+ " tc inner join tb_transaction_group tg on tc.transactionGroup_id = tg.id"
					+ " WHERE tc.id="
					+ id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				do {
					category.setId(cursor.getInt(0));
					category.setName(getNameByLocale(cursor));
					category.setIcon(cursor.getInt(3));
					category.setType(cursor.getString(4));
					category.setColor(Color.parseColor(cursor.getString(5)));
					category.setHit(cursor.getInt(6));
					category.setHitDate(cursor.getString(7));

					TransactionGroup group = new TransactionGroup();
					group.setName(getGroupNameByLocale(cursor));
					category.setTransactionGroup(group);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return category;
	}

	public ArrayList<TransactionCategory> getByType(
			TransactionCategory.Type type) {

		ArrayList<TransactionCategory> list = new ArrayList<TransactionCategory>();

		try {

			// Select All Query
			String selectQuery = "SELECT * FROM "
					+ DatabaseHelper.TRANSACTION_CATEGORY_TABLE + " tc "
					+ " inner join " + DatabaseHelper.TRANSACTION_GROUP_TABLE
					+ " tg on tc.transactionGroup_id = tg.id"
					+ " WHERE type = '" + type.name() + "' order by hit desc";

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TransactionCategory category = new TransactionCategory();
					category.setId(cursor.getInt(0));
					category.setName(getNameByLocale(cursor));
					category.setIcon(cursor.getInt(2));
					category.setType(cursor.getString(3));

					category.setHit(cursor.getInt(5));
					category.setHitDate(cursor.getString(6));

					TransactionGroup group = new TransactionGroup();
					group.setName(getGroupNameByLocale(cursor));
					category.setTransactionGroup(group);

					list.add(category);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			db.close();
		} finally {
			db.close();
		}

		return list;
	}

	private String getNameByLocale(Cursor cursor) {
		if (super.locale.getLanguage().equals("en"))
			return cursor.getString(2);
		else
			return cursor.getString(1);
	}

	private String getGroupNameByLocale(Cursor cursor) {
		if (super.locale.getLanguage().equals("en"))
			return cursor.getString(9);
		else
			return cursor.getString(8);
	}

}
