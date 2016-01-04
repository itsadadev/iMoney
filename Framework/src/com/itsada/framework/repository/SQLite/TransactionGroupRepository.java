package com.itsada.framework.repository.SQLite;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.itsada.framework.models.TransactionGroup;
import com.itsada.framework.repository.DatabaseHelper;
import com.itsada.framework.repository.IRepository;

@SuppressLint("SimpleDateFormat")
public class TransactionGroupRepository extends SqliteRepository implements
		IRepository<TransactionGroup> {

	
	public TransactionGroupRepository(Context context) {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public long add(TransactionGroup entity) {

		return 0;

	}

	@Override
	public void update(TransactionGroup entity) {

		

	}

	@Override
	public void delete(int id) {

		

	}

	@Override
	public ArrayList<TransactionGroup> getAll() {

		ArrayList<TransactionGroup> list = new ArrayList<TransactionGroup>();

		try {

			// Select All Query
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TRANSACTION_GROUP_TABLE;

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TransactionGroup group = new TransactionGroup();
					group.setId(cursor.getInt(0));
					group.setName(cursor.getString(1));
					
					list.add(group);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return list;
	}

	@Override
	public TransactionGroup getById(int id) {

		TransactionGroup group = new TransactionGroup();

		try {
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TRANSACTION_GROUP_TABLE
					+ " WHERE " + DatabaseHelper.TRANSACTION_GROUP_ID + "=" + id;

			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				do {
					group.setId(cursor.getInt(0));
					group.setName(cursor.getString(1));

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e(TransactionGroupRepository.class.getName(), e.getMessage());
		} finally {
			db.close();
		}

		return group;
	}	
}
