package com.itsada.framework.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.itsada.framework.R;

@SuppressLint("SimpleDateFormat")
public class DatabaseHelper extends SQLiteOpenHelper {
	// Detail database
	private static final String DATABASE_NAME = "iMoney.db";
	private static final int DATABASE_VERSION = 1;
	private static String DB_FILEPATH;

	// -----------------------------------------------------------------
	// ----------------------Configuration Table------------------------
	// -----------------------------------------------------------------
	public static final String CONFIGURATION_TABLE = "tb_configuration";
	public static final String CONFIGURATION_ID = "id";
	public static final String CONFIGURATION_LOCALE = "locale";
	public static final String CONFIGURATION_CURRENCY = "currency";
	public static final String CONFIGURATION_DATE_FORMAT = "dateFormat";
	public static final String CONFIGURATION_TIME_FORMAT = "timeFormat";
	public static final String CONFIGURATION_MONEY_FORMAT = "moneyFormat";
	public static final String CONFIGURATION_PASSWORD = "password";

	private static final String DATABASE_CREATE_CONFIGURATION_TABLE = "create table tb_configuration ("
			+ "id Integer primary key AUTOINCREMENT NOT NULL,"
			+ "locale text null,"
			+ "currency text null,"
			+ "dateFormat text null,"
			+ "timeFormat text null,"
			+ "moneyFormat text null," + "password text null" + ");";

	// ------------------------------------------------
	// ----------------------Account type Table-------------
	// ------------------------------------------------
	public static final String ACCOUNT_TYPE_TABLE = "tb_account_type";
	public static final String ACCOUNT_TYPE_ID = "id";
	public static final String ACCOUNT_TYPE_NAME_Th = "nameTh";
	public static final String ACCOUNT_TYPE_NAME_En = "nameEn";
	public static final String ACCOUNT_TYPE_VISIBLE = "visible";

	private static final String DATABASE_CREATE_ACCOUNT_TYPE_TABLE = "create table tb_account_type ("
			+ "id Integer primary key AUTOINCREMENT NOT NULL,"
			+ "nameTh text not null,"
			+ "nameEn text not null,"
			+ "visible INTEGER not null);";

	// ------------------------------------------------
	// ----------------------Account Table-------------
	// ------------------------------------------------
	public static final String ACCOUNT_TABLE = "tb_account";
	public static final String ACCOUNT_ID = "id";
	public static final String ACCOUNT_NAME = "name";
	public static final String ACCOUNT_BALANCE = "balance";
	public static final String ACCOUNT_CREATE_DATE = "createDate";
	public static final String ACCOUNT_UPDATE_DATE = "updateDate";
	public static final String ACCOUNT_ICON = "icon";
	public static final String ACCOUNT_ACCOUNT_TYPE_ID = "accountType_id";
	public static final String ACCOUNT_IS_HIDE = "isHide";
	public static final String ACCOUNT_COLOR = "color";
	private static final String DATABASE_CREATE_ACCOUNT_TABLE = "create table tb_account ("
			+ "id INTEGER primary key AUTOINCREMENT NOT NULL,"
			+ "name TEXT not null,"
			// + "nameEn TEXT not null,"
			+ "balance REAL not null,"
			+ "createDate INTEGER not null,"
			+ "updateDate TEXT,"
			+ "icon INTEGER, "
			+ "accountType_id INTEGER not null,"
			+ "isHide INTEGER not null,"
			+ "color TEXT);"; // integers
								// 0
								// (false)
								// and
								// 1
								// (true).

	// ------------------------------------------------
	// ----------------------Transaction Table---------
	// ------------------------------------------------
	public static final String TRANSACTION_TABLE = "tb_transaction";
	public static final String TRANSACTION_ID = "id";
	public static final String TRANSACTION_NAME = "name";
	public static final String TRANSACTION_AMOUNT = "amount";
	public static final String TRANSACTION_CREATE_DATE = "createDate";
	public static final String TRANSACTION_ACCOUNT_ID = "account_id";
	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String TRANSACTION_CATEGORY_ID = "transactionCategory_id";
	public static final String TRANSACTION_DATE = "date";
	public static final String TRANSACTION_LATITUDE = "latitude";
	public static final String TRANSACTION_LONGITUDE = "longitude";

	private static final String DATABASE_CREATE_TRANSACTION_TABLE = "create table tb_transaction ("
			+ "id INTEGER primary key AUTOINCREMENT NOT NULL,"
			+ "name TEXT not null,"
			+ "amount REAL not null,"
			+ "createDate INTEGER not null,"
			+ "account_id INTEGER not null, "
			+ "transactionType TEXT not null,"
			+ "transactionCategory_id INTEGER not null,"
			+ "date TEXT null,"
			+ "latitude TEXT null," + "longitude TEXT null);";

	// ------------------------------------------------
	// ----------------------Transaction Template Table---------
	// ------------------------------------------------
	public static final String TEMPLATE_TABLE = "tb_template";
	public static final String TEMPLATE_ID = "id";
	public static final String TEMPLATE_TRANSACTION_ID = "transaction_id";
	public static final String TEMPLATE_NAME = "name";
	public static final String TEMPLATE_AMOUNT = "amount";
	public static final String TEMPLATE_CREATE_DATE = "createDate";
	public static final String TEMPLATE_ACCOUNT_ID = "account_id";
	public static final String TEMPLATE_TYPE = "transactionType";
	public static final String TEMPLATE_CATEGORY_ID = "transactionCategory_id";
	public static final String TEMPLATEN_DATE = "date";
	public static final String TEMPLATE_LATITUDE = "latitude";
	public static final String TEMPLATE_LONGITUDE = "longitude";

	private static final String DATABASE_CREATE_TEMPLATE_TABLE = "create table tb_template ("
			+ "id INTEGER primary key AUTOINCREMENT NOT NULL,"
			+ "transaction_id INTEGER not null,"
			+ "name TEXT not null,"
			+ "amount REAL not null,"
			+ "createDate INTEGER not null,"
			+ "account_id INTEGER not null, "
			+ "transactionType TEXT not null,"
			+ "transactionCategory_id INTEGER not null,"
			+ "date TEXT null,"
			+ "latitude TEXT null," + "longitude TEXT null);";

	// ------------------------------------------------
	// ----------------------Transaction category Table---
	// ------------------------------------------------
	public static final String TRANSACTION_CATEGORY_TABLE = "tb_transactionCategory";
	public static final String CATEGORY_ID = "id";
	public static final String TRANSACTION_CATEGORY_NAME_TH = "nameTh";
	public static final String TRANSACTION_CATEGORY_NAME_EN = "nameEn";
	public static final String TRANSACTION_CATEGORY_ICON = "icon";
	public static final String TRANSACTION_CATEGORY_TYPE = "type";
	public static final String TRANSACTION_CATEGORY_COLOR = "color";
	public static final String TRANSACTION_CATEGORY_HIT = "hit";
	public static final String TRANSACTION_CATEGORY_HITDATE = "hitDate";
	public static final String TRANSACTION_CATEGORY_IS_NEED = "isNeed";
	public static final String GROUP_ID = "transactionGroup_id";

	private static final String DATABASE_CREATE_TRANSACTION_CATEGORY_TABLE = "create table tb_transactionCategory ("
			+ "id INTEGER primary key AUTOINCREMENT NOT NULL,"
			+ "nameTh TEXT not null,"
			+ "nameEn TEXT not null,"
			+ "icon INTEGER not null,"
			+ "type TEXT not null,"
			+ "color TEXT null,"
			+ "hit int null,"
			+ "hitDate TEXT null,"
			+ "isNeed int null," // 0 is not need then 1 is need
			+ "transactionGroup_id INTEGER not null);";

	// ------------------------------------------------
	// ----------------------Transaction Group Table-------------
	// ------------------------------------------------
	public static final String TRANSACTION_GROUP_TABLE = "tb_transaction_group";
	public static final String TRANSACTION_GROUP_ID = "id";
	public static final String TRANSACTION_GROUP_NAME_TH = "nameTh";
	public static final String TRANSACTION_GROUP_NAME_EN = "nameEn";

	private static final String DATABASE_CREATE_TRANSACTION_GROUP_TABLE = "create table tb_transaction_group ("
			+ "id Integer primary key AUTOINCREMENT NOT NULL,"
			+ "nameTh text not null," + "nameEn text not null" + ");";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		final String packageName = context.getPackageName();
		DB_FILEPATH = "/data/data/" + packageName + "/databases/iMoney.db";
	}

	// Create Table
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_CONFIGURATION_TABLE);
		db.execSQL(DATABASE_CREATE_ACCOUNT_TYPE_TABLE);
		db.execSQL(DATABASE_CREATE_ACCOUNT_TABLE);
		db.execSQL(DATABASE_CREATE_TRANSACTION_TABLE);
		db.execSQL(DATABASE_CREATE_TRANSACTION_CATEGORY_TABLE);
		db.execSQL(DATABASE_CREATE_TEMPLATE_TABLE);
		db.execSQL(DATABASE_CREATE_TRANSACTION_GROUP_TABLE);

		// Initial
		// db.execSQL("insert into tb_configuration " +
		// "(currency, dateFormat, timeFormat, moneyFormat, password) " +
		// "values('', '', '', '', '')");

		// db.execSQL("insert into tb_account_type (nameTh, nameEn) values('บัญชีเงินฝาก', 'Deposit Account')");
		// db.execSQL("insert into tb_account_type (nameTh, nameEn) values('บัญชีเงินสด', 'Cash Account')");
		// db.execSQL("insert into tb_account_type (nameTh, nameEn) values('บัญชีหนี้สิน', 'Debt Account')");

		// Assets
		db.execSQL("insert into tb_account_type (nameTh, nameEn, visible) values('สินทรัพย์สภาพคล่อง', 'Liquid Asset', '1')");
		db.execSQL("insert into tb_account_type (nameTh, nameEn, visible) values('สินทรัพย์เพื่อการลงทุน', 'Invertment Asset', '0')");
		db.execSQL("insert into tb_account_type (nameTh, nameEn, visible) values('สินทรัพย์ส่วนตัว', 'Personal Asset', '0')");
		// Liabilities
		db.execSQL("insert into tb_account_type (nameTh, nameEn, visible) values('หนี้สินระยะสั้น', 'Shot-term Liabilities', '1')");
		db.execSQL("insert into tb_account_type (nameTh, nameEn, visible) values('หนี้ระยะยาว', 'Long-term Liabilities', '0')");

		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายรับจากงานประจำ', 'Occupation Income')");
		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายรับจากสินทรัพย์', 'Assets Income')");
		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายรับอื่นๆ', 'Other Income')");

		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายจ่ายคงที่', 'Fixed Expense')");
		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายจ่ายเพื่อออม/ลงทุน', 'Saving Expense')");
		db.execSQL("insert into tb_transaction_group (nameTh, nameEn) values('รายจ่ายผันแปร','Variable Expense')");

		long now = GregorianCalendar.getInstance().getTimeInMillis();

		// -----------------------------------------------------------------------------------------------------
		// ---------------------------------------Account
		// table-----------------------------------------------
		// -----------------------------------------------------------------------------------------------------
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('Saving', '9000', '"
				+ now + "', '1', '1', '#FF3300FF')");
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('Wallet', '500', '"
				+ now + "', '1', '1', '#FFFF0033')");
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('กรมธรรม์ประกันชีวิต', '0', '"
				+ now + "', '2', '0', '#FF00FF33')");
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('บ้าน', '350000', '"
				+ now + "', '3', '0', '#FF00FF33')");
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('Credit Card', '0', '"
				+ now + "', '4', '1', '#FF00FF33')");
		db.execSQL("insert into tb_account (name, balance, createDate, accountType_id, isHide, color) values('เงินกู้ซื้อบ้าน', '300000', '"
				+ now + "', '5', '0', '#FF00FF33')");

		// -----------------------------------------------------------------------------------------------------
		// ---------------------------------------TransactionCategory-------------------------------------------
		// -----------------------------------------------------------------------------------------------------
		// Incomes
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('TransferForm', 'TransferForm',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'TransferForm', '#FFFF0033', '0', '0')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('TransferTo', 'TransferTo',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'TransferTo', '#FFFF0033', '0', '0')");

		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('เงินเดือน', 'Salary',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '1')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('เบี้ยเลี้ยง', 'Allowance',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '1')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('โบนัส', 'Bonus',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '1')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ดอกเบี้ย', 'Interase',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '2')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('เงินปันผล', 'Benefits',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '2')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าเช่า', 'Rent',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '2')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ถูกรางวัล', 'Out of luck',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Income', '#FFFF0033', '0', '3')");

		// Expends
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าน้ำประปา', 'Water',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าไฟฟ้า', 'Electricity',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าเน็ต', 'Internet',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าโทรศัพท์', 'Phone',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าอาหาร', 'Food',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าเดินทาง', 'Travelling',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าสันทนาการ', 'Recreation',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าใช้จ่ายสุขภาพ', 'Health',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('บริจาคเพื่อสังคม', 'Donation',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่าใช้จ่ายอื่นๆ', 'Other',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '6')");

		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ออม', 'Saving',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '5')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ลงทุน', 'Invest',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '5')");

		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('เบี้ยประกัน', 'Insurance premium',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '4')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่างวดบ้าน', 'House',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '4')");
		db.execSQL("insert into tb_transactionCategory (nameTh, nameEn, icon, type, color, hit, transactionGroup_id)"
				+ "values('ค่างวดรถ', 'Car',  '"
				+ R.drawable.ic_local_atm_black
				+ "', 'Expenses', '#FFFF0033', '0', '4')");

		// -----------------------------------------------------------------------------------------------------
		// ---------------------------------------Transaction---------------------------------------------------
		// -----------------------------------------------------------------------------------------------------
		// db.execSQL("insert into tb_transaction (name, amount, createDate, account_id, transactionType, transactionCategory_id) "
		// + "values('test1', '20', '" + now + "', '2', 'Expenses', '8')");
		// db.execSQL("insert into tb_transaction (name, amount, createDate, account_id, transactionType, transactionCategory_id) "
		// + "values('test2', '40', '" + now + "', '2', 'Expenses', '9')");
		// db.execSQL("insert into tb_transaction (name, amount, createDate, account_id, transactionType, transactionCategory_id) "
		// + "values('test3', '50', '" + now + "', '2', 'Expenses', '5')");
	}

	// Upgrade version
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgread database version from version" + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CONFIGURATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TYPE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_CATEGORY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TEMPLATE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_GROUP_TABLE);

		onUpgrade(db, oldVersion, newVersion);
	}

	public static void backupDatabase() throws IOException {

		if (isSDCardWriteable()) {
			// Open your local db as the input stream
			String inFileName = DB_FILEPATH;
			File dbFile = new File(inFileName);
			FileInputStream fis = new FileInputStream(dbFile);

			String outFileName = Environment.getExternalStorageDirectory()
					+ "/Download/imoney.db";
			// Open the empty db as the output stream
			OutputStream output = new FileOutputStream(outFileName);
			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			// Close the streams
			output.flush();
			output.close();
			fis.close();
		}
	}

	public boolean importDatabase(String dbPath) throws IOException {

		// Close the SQLiteOpenHelper so it will
		// commit the created empty database to internal storage.
		close();
		File newDb = new File(dbPath);
		File oldDb = new File(DB_FILEPATH);
		if (newDb.exists()) {
			copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
			// Access the copied database so SQLiteHelper
			// will cache it and mark it as created.
			getWritableDatabase().close();
			return true;
		}
		return false;
	}

	private void copyFile(FileInputStream fromFile, FileOutputStream toFile)
			throws IOException {
		FileChannel fromChannel = null;
		FileChannel toChannel = null;
		try {
			fromChannel = fromFile.getChannel();
			toChannel = toFile.getChannel();
			fromChannel.transferTo(0, fromChannel.size(), toChannel);
		} finally {
			try {
				if (fromChannel != null) {
					fromChannel.close();
				}
			} finally {
				if (toChannel != null) {
					toChannel.close();
				}
			}
		}
	}

	private static boolean isSDCardWriteable() {
		boolean rc = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			rc = true;
		}
		return rc;
	}

}
