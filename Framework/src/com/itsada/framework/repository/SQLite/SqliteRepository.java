package com.itsada.framework.repository.SQLite;

import java.util.Locale;

import com.itsada.framework.repository.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class SqliteRepository {
	
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;
	protected Context context;
	protected Locale locale;

	public SqliteRepository() {
		// TODO Auto-generated constructor stub
	}

}
