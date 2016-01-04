package com.itsada.imoney;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Application;
import android.util.Log;

import com.itsada.framework.models.AccountType;
import com.itsada.framework.models.Configuration;
import com.itsada.framework.models.Transaction.GroupBy;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;
import com.itsada.framework.services.AccountService;

public class App extends Application {

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Fields-------------------------------- */
	/* ------------------------------------------------------------------------- */
	public boolean isCategoryGroup = false;
	public GroupBy groupBy = GroupBy.Day;
	public Date toDay = Calendar.getInstance().getTime();
	public Date from;
	public Date to;
	public static Configuration configuration;
	public static android.content.res.Configuration resourceConfig;

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Static Fields------------------------- */
	/* ------------------------------------------------------------------------- */
//	public static ArrayList<AccountType> accountTypes;
	public static boolean isCreateTransaction = false;
	public static boolean isUpdateTransaction = false;
	public static boolean isUpdateTransactionCatrgory = false;
	public static boolean isUpdateAccount = false;
	public static boolean isUpdateLangage = false;

	/* ------------------------------------------------------------------------- */
	/* -----------------------------------Cons Fields--------------------------- */
	/* ------------------------------------------------------------------------- */
	public static final String APP_NAME = "iMoney";
	public static final String ACCOUNT_ID = "accountId";
	public static final String TRANSACTION_CATEGORY_ID = "transaction_categoryId";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String TEMPLATE_ID = "templateId";
	public static final String ACCOUNT_NAME = "accountName";
	public static final String ACCOUNT_BALANCE = "accountBalance";
	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String ACCOUNT_TYPE_ID = "accountTypeId";
	public static final String IS_FIRST_LOAD = "isFirstLoad";
	public static final String CATEGORY_TYPE = "categoryType";

	public static final int UPDATE_ACCOUNT_TYPE_REQUEST_CODE = 99;

	public static final int CREATE_ACCOUNT_REQUEST_CODE = 100;
	public static final int EDIT_ACCOUNT_REQUEST_CODE = 101;

	public static final int CREATE_TRANSACTION_REQUEST_CODE = 200;
	public static final int EDIT_TRANSACTION_REQUEST_CODE = 201;
	public static final int EDIT_TEMPLATE_REQUEST_CODE = 202;

	public static final int CREATE_TRANSACTION_CATEGORY_REQUEST_CODE = 300;
	public static final int EDIT_TRANSACTION_CATEGORY_REQUEST_CODE = 301;
	public static final int CREATE_TRANSACTION_CATEGORY_FROM_T_REQUEST_CODE = 302;

	public static AccountService accountService;

	@Override
	public void onCreate() {

		// AccountTypeRepository accountTypeRepository = null;

		try {

			super.onCreate();

			// accountTypeRepository = new AccountTypeRepository(
			// getApplicationContext());

			resourceConfig = new android.content.res.Configuration();

			ConfigurationRepository configurationRepository = new ConfigurationRepository(
					getApplicationContext());
			configuration = configurationRepository.getCurrentConfig();

			if (configuration != null)
				resourceConfig.locale = configuration.getLocale();

			Calendar c = Calendar.getInstance();
			c.set(Calendar.DATE, 1);
			from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			to = c.getTime();

			accountService = new AccountService(getApplicationContext(),
					configuration.getLocale());

//			accountTypes = accountService.getAccountTypeRepo().getAll();

		} catch (Exception e) {

		} finally {
			// accountTypeRepository.dispose();
		}
	}

	public void resetDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		from = c.getTime();
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		to = c.getTime();
	}

}
