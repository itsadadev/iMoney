package com.itsada.imoney;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.AccountType;
import com.itsada.framework.models.Configuration;
import com.itsada.framework.models.Template;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.AccountTypeRepository;
import com.itsada.framework.repository.SQLite.ConfigurationRepository;
import com.itsada.framework.repository.SQLite.TemplateRepository;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.imoney.views.ConfirmDialog;
import com.itsada.imoney.views.DateFormatDialog;
import com.itsada.imoney.views.MainMenu;
import com.itsada.imoney.views.DateFormatDialog.DateFormatCallback;
import com.itsada.imoney.views.LanguageDialog;
import com.itsada.imoney.views.LanguageDialog.Callback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint({ "NewApi", "SimpleDateFormat", "InflateParams" })
public abstract class BaseActivity extends Activity {

	protected String TAG = BaseActivity.class.getName();
	protected TextView textTitle;
	protected ImageView imageMenu;
	protected ImageView imgMainMenuRight;
	protected SlidingMenu slidingMainMenuLeft;
	protected ActionBar actionBar;
	protected MainMenu mainMenu;

	protected UpdateView updateView;
	protected ReturnView returnView;

	// Format
	public static String ddMMyyyyEnFormat = "dd/MM/yyyy";
	public static String MMddyyyyEnFormat = "MM/dd/yyyy";
	public static String yyyyddMMEnFormat = "yyyy/dd/MM";

	public static String ddMMyyyyThFormat = "dd/MM/yyyy";
	public static String MMddyyyyThFormat = "MM/dd/yyyy";
	public static String yyyyddMMThFormat = "yyyy/dd/MM";

	public static SimpleDateFormat monthYearEnFormat = new SimpleDateFormat(
			"MMM yyyy", Locale.ENGLISH);
	public static SimpleDateFormat monthYearThFormat = new SimpleDateFormat(
			"MMM yyyy", new Locale("th"));

	public static DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	public static SimpleDateFormat monthyearEEEFormat = new SimpleDateFormat(
			"EEE MMM yyyy");
	public static SimpleDateFormat monthyearFormat = new SimpleDateFormat(
			"MMM yyyy", Locale.ENGLISH);
	public static SimpleDateFormat monthFormat = new SimpleDateFormat("MMM",
			new Locale("th"));
	public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat dateEEEFormat = new SimpleDateFormat(
			"EEE dd/MM/yyyy");

	protected Calendar calendarTh = Calendar.getInstance();
	protected GregorianCalendar gregorianCalendar = new GregorianCalendar(
			new Locale("th"));
	protected Calendar calendar = Calendar.getInstance();

	protected Runnable swapLocal;

	private LinearLayout accountRoot, budgetRoot, templateRoot;
	private LinearLayout categoryRootIncome;
	private LinearLayout categoryRootExpend;

	protected abstract void onInit();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// getApplication().setTheme(R.style.GreyTheme);

		overridePendingTransition(R.animator.activity_open_translate,
				R.animator.activity_close_scale);

		// Update resource on change local
		if (App.configuration != null)
			if (!getResources().getConfiguration().locale
					.equals(App.configuration.getLocale()))
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.d(TAG, "Update Local");
						getResources().updateConfiguration(
								App.resourceConfig,
								getBaseContext().getResources()
										.getDisplayMetrics());
					}
				});

		// setActionBar();
	}

	protected void hideKeyboard(View v) {

		try {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

		} catch (Exception e) {
			Log.e(TAG, "hideKeyboard : " + e.toString());
		}

	}

	protected Account getAccount(AccountType category, int accountId) {

		for (Account a : category.getAccounts()) {
			if (a.getId() == accountId) {
				return a;
			}
		}
		return null;
	}

	protected void clearChild(ViewGroup layoutLabel) {
		for (int i = 0; i < layoutLabel.getChildCount(); i++)
			layoutLabel.removeView(layoutLabel.getChildAt(i));

		if (layoutLabel.getChildCount() > 0)
			clearChild(layoutLabel);
	}

	protected void setActionBar() {

		try {

			// getActionBar().setDisplayHomeAsUpEnabled(true);

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(R.layout.custom_actionbar,
					null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			imageMenu = (ImageView) mCustomView.findViewById(R.id.menu);
			textTitle = (TextView) mCustomView.findViewById(R.id.tvTitle);

			((ImageView) findViewById(R.id.imgIcon))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									CreateTransactionEmptryActivity.class);
							startActivityForResult(intent,
									App.CREATE_TRANSACTION_REQUEST_CODE);
						}
					});

		} catch (Exception e) {
			Log.e(TAG, "setActionBar:" + e.toString());
		}

	}

	public void slidingMainMenuLeft() {

		try {

			slidingMainMenuLeft = new SlidingMenu(this);

			slidingMainMenuLeft.setShadowWidth(5);
			slidingMainMenuLeft.setFadeDegree(0.0f);
			slidingMainMenuLeft.setMode(SlidingMenu.LEFT);
			slidingMainMenuLeft.setBehindWidth(100);
			slidingMainMenuLeft.setShadowWidthRes(R.dimen.shadow_width);
			slidingMainMenuLeft.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			slidingMainMenuLeft
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// slidingMainMenuLeft.setMenu(R.layout.menu_left_frame);
			slidingMainMenuLeft.setMenu(R.layout.menu_frame);
			slidingMainMenuLeft.attachToActivity(this,
					SlidingMenu.SLIDING_WINDOW);

			mainMenu = new MainMenu(this, slidingMainMenuLeft);

			// final ImageView imgCollapesAccount = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesAccount);
			// final ImageView imgExpandsAccount = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgExpandAccount);
			//
			// final ImageView imgCollapesBudget = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesBudget);
			// final ImageView imgExpandsBudget = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgExpandBudget);
			//
			// final ImageView imgCollapesTemplate = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesTemplate);
			// final ImageView imgExpandsTemplate = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgExpandTemplate);
			//
			// final ImageView imgCollapesCategory = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesCategory);
			// final ImageView imgExpandsCategory = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgExpandCategory);
			//
			// final ImageView imgCollapesSetting = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesSetting);
			// final ImageView imgExpandsSetting = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgExpandSetting);
			//
			// final ImageView imgCollapesHelp = (ImageView) slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesHelp);
			// final ImageView imgExpandsHelp = (ImageView) slidingMainMenuLeft
			// .findViewById(R.id.imgExpandHelp);
			//
			// final ImageView imgCollapesRate = (ImageView) slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesRate);
			// final ImageView imgExpandsRate = (ImageView) slidingMainMenuLeft
			// .findViewById(R.id.imgExpandRate);
			//
			// final ImageView imgCollapesAbout = (ImageView)
			// slidingMainMenuLeft
			// .findViewById(R.id.imgCollapesAbout);
			// final ImageView imgExpandsAbout = (ImageView) slidingMainMenuLeft
			// .findViewById(R.id.imgExpandAbout);
			//
			// accountRoot = (LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.accountRoot);
			// budgetRoot = (LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.budgetRoot);
			// templateRoot = (LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.templateRoot);
			// categoryRootIncome = (LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.categoryRootIncome);
			// categoryRootExpend = (LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.categoryRootExpend);
			//
			// final LinearLayout layoutRootAccount = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootAccount);
			// final LinearLayout layoutRootBudget = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootBudget);
			// final LinearLayout layoutRootTemplate = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootTemplate);
			// final LinearLayout layoutRootCategory = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootCategory);
			// final LinearLayout layoutRootSetting = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootSetting);
			// final LinearLayout layoutRootHelp = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootHelp);
			// final LinearLayout layoutRootRate = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootRate);
			// final LinearLayout layoutRootAbout = (LinearLayout)
			// slidingMainMenuLeft
			// .findViewById(R.id.layoutRootAbout);
			//
			// ((LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.layoutAddAccount))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// Intent intent = new Intent(getApplicationContext(),
			// CreateAccountEmptryActivity.class);
			// startActivityForResult(intent,
			// App.CREATE_ACCOUNT_REQUEST_CODE);
			// }
			// });
			//
			// ((LinearLayout) slidingMainMenuLeft
			// .findViewById(R.id.layoutAddCategory))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// Intent intent = new Intent(getApplicationContext(),
			// CreateTransactionCategory.class);
			// startActivityForResult(
			// intent,
			// App.CREATE_TRANSACTION_CATEGORY_REQUEST_CODE);
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.accountLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesAccount.getVisibility() == View.VISIBLE) {
			// layoutRootAccount.setVisibility(View.VISIBLE);
			// imgExpandsAccount.setVisibility(View.VISIBLE);
			// imgCollapesAccount.setVisibility(View.GONE);
			// } else {
			// layoutRootAccount.setVisibility(View.GONE);
			// imgExpandsAccount.setVisibility(View.GONE);
			// imgCollapesAccount.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.budgetLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesBudget.getVisibility() == View.VISIBLE) {
			// layoutRootBudget.setVisibility(View.VISIBLE);
			// imgExpandsBudget.setVisibility(View.VISIBLE);
			// imgCollapesBudget.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootBudget.setVisibility(View.GONE);
			// imgExpandsBudget.setVisibility(View.GONE);
			// imgCollapesBudget.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.templateLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesTemplate.getVisibility() == View.VISIBLE) {
			// layoutRootTemplate.setVisibility(View.VISIBLE);
			// imgExpandsTemplate.setVisibility(View.VISIBLE);
			// imgCollapesTemplate.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootTemplate.setVisibility(View.GONE);
			// imgExpandsTemplate.setVisibility(View.GONE);
			// imgCollapesTemplate.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.categoryLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesCategory.getVisibility() == View.VISIBLE) {
			// layoutRootCategory.setVisibility(View.VISIBLE);
			// imgExpandsCategory.setVisibility(View.VISIBLE);
			// imgCollapesCategory.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootCategory.setVisibility(View.GONE);
			// imgExpandsCategory.setVisibility(View.GONE);
			// imgCollapesCategory.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.settingLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesSetting.getVisibility() == View.VISIBLE) {
			// layoutRootSetting.setVisibility(View.VISIBLE);
			// imgExpandsSetting.setVisibility(View.VISIBLE);
			// imgCollapesSetting.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootSetting.setVisibility(View.GONE);
			// imgExpandsSetting.setVisibility(View.GONE);
			// imgCollapesSetting.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout)
			// slidingMainMenuLeft.findViewById(R.id.helpLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesHelp.getVisibility() == View.VISIBLE) {
			// layoutRootHelp.setVisibility(View.VISIBLE);
			// imgExpandsHelp.setVisibility(View.VISIBLE);
			// imgCollapesHelp.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootHelp.setVisibility(View.GONE);
			// imgExpandsHelp.setVisibility(View.GONE);
			// imgCollapesHelp.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout)
			// slidingMainMenuLeft.findViewById(R.id.rateLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesRate.getVisibility() == View.VISIBLE) {
			// layoutRootRate.setVisibility(View.VISIBLE);
			// imgExpandsRate.setVisibility(View.VISIBLE);
			// imgCollapesRate.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootRate.setVisibility(View.GONE);
			// imgExpandsRate.setVisibility(View.GONE);
			// imgCollapesRate.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });
			//
			// ((RelativeLayout) slidingMainMenuLeft
			// .findViewById(R.id.aboutLayout))
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (imgCollapesAbout.getVisibility() == View.VISIBLE) {
			// layoutRootAbout.setVisibility(View.VISIBLE);
			// imgExpandsAbout.setVisibility(View.VISIBLE);
			// imgCollapesAbout.setVisibility(View.GONE);
			// } else {
			//
			// layoutRootAbout.setVisibility(View.GONE);
			// imgExpandsAbout.setVisibility(View.GONE);
			// imgCollapesAbout.setVisibility(View.VISIBLE);
			// }
			//
			// }
			// });

			// imgCollapes.setOnClickListener(new OnClickListener() {
			//
			// @SuppressLint("NewApi")
			// @Override
			// public void onClick(View v) {
			// layoutRoot.setVisibility(View.VISIBLE);
			// v.setVisibility(View.GONE);
			// imgExpands.setVisibility(View.VISIBLE);
			// }
			// });
			//
			// imgExpands.setOnClickListener(new OnClickListener() {
			//
			// @SuppressLint("NewApi")
			// @Override
			// public void onClick(View v) {
			// layoutRoot.setVisibility(View.GONE);
			// v.setVisibility(View.GONE);
			// imgCollapes.setVisibility(View.VISIBLE);
			// }
			// });

			// imgCollapesCat.setOnClickListener(new OnClickListener() {
			//
			// @SuppressLint("NewApi")
			// @Override
			// public void onClick(View v) {
			// layoutRootCat.setVisibility(View.VISIBLE);
			// v.setVisibility(View.GONE);
			// imgExpandsCat.setVisibility(View.VISIBLE);
			// }
			// });
			//
			// imgExpandsCat.setOnClickListener(new OnClickListener() {
			//
			// @SuppressLint("NewApi")
			// @Override
			// public void onClick(View v) {
			// layoutRootCat.setVisibility(View.GONE);
			// v.setVisibility(View.GONE);
			// imgCollapesCat.setVisibility(View.VISIBLE);
			// }
			// });

		} catch (Exception e) {
			Log.e(TAG, "slideMenu : " + e.toString());

		}
	}

	public void slidingMainMenuRight() {

		try {

			// if (App.configuration == null)
			// return;
			//
			// slidingMainMenuRight = new SlidingMenu(this);
			//
			// slidingMainMenuRight.setShadowWidth(5);
			// slidingMainMenuRight.setFadeDegree(0.0f);
			// slidingMainMenuRight.setMode(SlidingMenu.RIGHT);
			// slidingMainMenuRight.setBehindWidth(100);
			// slidingMainMenuRight.setShadowWidthRes(R.dimen.shadow_width);
			// slidingMainMenuRight.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			// slidingMainMenuRight
			// .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// slidingMainMenuRight.setMenu(R.layout.menu_right_frame);
			// slidingMainMenuRight.attachToActivity(this,
			// SlidingMenu.SLIDING_WINDOW);

		} catch (Exception e) {
			Log.e(TAG, "slideMenu : " + e.toString());

		}
	}

	protected void updateRightMenu() {

		// if (App.configuration == null)
		// return;
		//
		// ((TextView) slidingMainMenuRight.findViewById(R.id.tvContext))
		// .setText(getResources().getString(R.string.setting));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.tvDisplay))
		// .setText(getResources().getString(R.string.display));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.lbMoneyFormat))
		// .setText(getResources().getString(R.string.moneyFormat));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.lbDateFormat))
		// .setText(getResources().getString(R.string.dateFormat));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.lbTimeFormat))
		// .setText(getResources().getString(R.string.timeFormat));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.lbLanguage))
		// .setText(getResources().getString(R.string.language));
		// ((TextView) slidingMainMenuRight.findViewById(R.id.lbCurrency))
		// .setText(getResources().getString(R.string.currency));
		//
		// final ImageView imgCollapeSetting = (ImageView) slidingMainMenuRight
		// .findViewById(R.id.imgCollapesSetting);
		//
		// final ImageView imgExpandSetting = (ImageView) slidingMainMenuRight
		// .findViewById(R.id.imgExpandSetting);
		//
		// final LinearLayout layoutRootSetting = (LinearLayout)
		// slidingMainMenuRight
		// .findViewById(R.id.layoutRootSeting);
		//
		// ImageView imgLanguage = (ImageView) slidingMainMenuRight
		// .findViewById(R.id.imgLanguage);
		//
		// if (App.configuration.getLocale().equals(Locale.ENGLISH))
		// imgLanguage.setBackgroundResource(R.drawable.eng);
		// else
		// imgLanguage.setBackgroundResource(R.drawable.th);
		//
		// TextView tvDateFormat = (TextView) findViewById(R.id.tvDateFormat);
		// String format = App.configuration.getDateFormat();
		//
		// if (format.equals(BaseActivity.ddMMyyyyEnFormat)) {
		// tvDateFormat.setText("31/01/2015");
		// } else if (format.equals(BaseActivity.MMddyyyyEnFormat)) {
		// tvDateFormat.setText("01/31/2015");
		// } else if (format.equals(BaseActivity.yyyyddMMEnFormat)) {
		// tvDateFormat.setText("2015/01/31");
		// }
		// // } else if (format.equals(BaseActivity.ddMMyyyyThFormat)) {
		// // tvDateFormat.setText(App.configuration.getDateFormat());
		// // } else if (format.equals(BaseActivity.MMddyyyyThFormat)) {
		// // tvDateFormat.setText(App.configuration.getDateFormat());
		// // } else if (format.equals(BaseActivity.yyyyddMMThFormat)) {
		// // tvDateFormat.setText(App.configuration.getDateFormat());
		// // }
		//
		// imgCollapeSetting.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View v) {
		// layoutRootSetting.setVisibility(View.VISIBLE);
		// v.setVisibility(View.GONE);
		// imgExpandSetting.setVisibility(View.VISIBLE);
		// }
		// });
		//
		// imgExpandSetting.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View v) {
		// layoutRootSetting.setVisibility(View.GONE);
		// v.setVisibility(View.GONE);
		// imgCollapeSetting.setVisibility(View.VISIBLE);
		// }
		// });
		//
		// ((LinearLayout)
		// slidingMainMenuRight.findViewById(R.id.layoutLanguage))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// final LanguageDialog languageDialog = new LanguageDialog(
		// BaseActivity.this);
		// languageDialog.show(App.configuration.getLocale());
		// languageDialog.callback = new Callback() {
		//
		// @Override
		// public void callback() {
		// Configuration config = App.configuration;
		// config.setLocale(languageDialog.locale);
		// App.resourceConfig.locale = languageDialog.locale;
		// ConfigurationRepository configurationRepository = new
		// ConfigurationRepository(
		// getApplicationContext());
		// configurationRepository.update(config);
		// onCreate(null);
		// }
		// };
		// }
		// });
		//
		// ((LinearLayout) slidingMainMenuRight
		// .findViewById(R.id.layoutDateFormat))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// final Configuration config = App.configuration;
		// final DateFormatDialog dateFormatDialog = new DateFormatDialog(
		// BaseActivity.this);
		// dateFormatDialog.show();
		// dateFormatDialog.setFormat(config.getDateFormat());
		// dateFormatDialog.callback = new DateFormatCallback() {
		//
		// @Override
		// public void callback() {
		// ConfigurationRepository configurationRepository = new
		// ConfigurationRepository(
		// getApplicationContext());
		// config.setDateFormat(dateFormatDialog.format
		// .toString());
		//
		// Log.d("DateFormat",
		// dateFormatDialog.format.toString());
		// configurationRepository.update(config);
		// onCreate(null);
		// }
		// };
		// }
		//
		// });
	}

	protected void updateMenu() {

		TextView tvManagement = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvManagement);
		TextView tvAccountType = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvAccountType);
		TextView tvAccount = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvAccount);
		TextView tvTemplate = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvTemplate);
		TextView tvCategory = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvCategory);
		TextView tvSettingHeader = (TextView) slidingMainMenuLeft
				
				.findViewById(R.id.tvSettingHeader);
		TextView tvSetting = (TextView) slidingMainMenuLeft
				.findViewById(R.id.tvSetting);

		tvManagement.setText(getResources().getString(R.string.management));
		tvAccountType.setText(getResources().getString(R.string.accounTypeManage));
		tvAccount.setText(getResources().getString(R.string.accounManage));
		tvTemplate.setText(getResources().getString(R.string.templateManagement));
		tvCategory.setText(getResources().getString(R.string.categoryManagement));
		tvSettingHeader.setText(getResources().getString(R.string.setting));
		tvSetting.setText(getResources().getString(R.string.setting));

		if (imageMenu != null)
			imageMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Log.d(TAG, String.valueOf(slidingMainMenuLeft.isShown()));
					Log.d(TAG,
							String.valueOf(slidingMainMenuLeft.isMenuShowing()));

					if (slidingMainMenuLeft.isMenuShowing()) {
						slidingMainMenuLeft.showContent(true);
						slidingMainMenuLeft.showMenu(false);
					} else {
						slidingMainMenuLeft.showContent(true);
						slidingMainMenuLeft.showMenu(false);
					}
				}
			});
		//
		// ((TextView) slidingMainMenuLeft.findViewById(R.id.lbMoneyFormat))
		// .setText(getResources().getString(R.string.moneyFormat));
		// ((TextView) slidingMainMenuLeft.findViewById(R.id.lbDateFormat))
		// .setText(getResources().getString(R.string.dateFormat));
		// ((TextView) slidingMainMenuLeft.findViewById(R.id.lbTimeFormat))
		// .setText(getResources().getString(R.string.timeFormat));
		// ((TextView) slidingMainMenuLeft.findViewById(R.id.lbLanguage))
		// .setText(getResources().getString(R.string.language));
		// ((TextView) slidingMainMenuLeft.findViewById(R.id.lbCurrency))
		// .setText(getResources().getString(R.string.currency));
		//
		// final ImageView imgCollapeSetting = (ImageView) slidingMainMenuLeft
		// .findViewById(R.id.imgCollapesSetting);
		//
		// final ImageView imgExpandSetting = (ImageView) slidingMainMenuLeft
		// .findViewById(R.id.imgExpandSetting);
		//
		// final LinearLayout layoutRootSetting = (LinearLayout)
		// slidingMainMenuLeft
		// .findViewById(R.id.layoutRootSetting);
		//
		// ImageView imgLanguage = (ImageView) slidingMainMenuLeft
		// .findViewById(R.id.imgLanguage);
		//
		// if (App.configuration.getLocale().equals(Locale.ENGLISH))
		// imgLanguage.setBackgroundResource(R.drawable.eng);
		// else
		// imgLanguage.setBackgroundResource(R.drawable.th);
		//
		// TextView tvDateFormat = (TextView) findViewById(R.id.tvDateFormat);
		// String format = App.configuration.getDateFormat();
		//
		// if (format.equals(BaseActivity.ddMMyyyyEnFormat)) {
		// tvDateFormat.setText("31/01/2015");
		// } else if (format.equals(BaseActivity.MMddyyyyEnFormat)) {
		// tvDateFormat.setText("01/31/2015");
		// } else if (format.equals(BaseActivity.yyyyddMMEnFormat)) {
		// tvDateFormat.setText("2015/01/31");
		// }
		// } else if (format.equals(BaseActivity.ddMMyyyyThFormat)) {
		// tvDateFormat.setText(App.configuration.getDateFormat());
		// } else if (format.equals(BaseActivity.MMddyyyyThFormat)) {
		// tvDateFormat.setText(App.configuration.getDateFormat());
		// } else if (format.equals(BaseActivity.yyyyddMMThFormat)) {
		// tvDateFormat.setText(App.configuration.getDateFormat());
		// }

		// imgCollapeSetting.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View v) {
		// layoutRootSetting.setVisibility(View.VISIBLE);
		// v.setVisibility(View.GONE);
		// imgExpandSetting.setVisibility(View.VISIBLE);
		// }
		// });
		//
		// imgExpandSetting.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(View v) {
		// layoutRootSetting.setVisibility(View.GONE);
		// v.setVisibility(View.GONE);
		// imgCollapeSetting.setVisibility(View.VISIBLE);
		// }
		// });
		//
		// ((LinearLayout)
		// slidingMainMenuLeft.findViewById(R.id.layoutLanguage))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// final LanguageDialog languageDialog = new LanguageDialog(
		// BaseActivity.this);
		// languageDialog.show(App.configuration.getLocale());
		// languageDialog.callback = new Callback() {
		//
		// @Override
		// public void callback() {
		// Configuration config = App.configuration;
		// config.setLocale(languageDialog.locale);
		// App.resourceConfig.locale = languageDialog.locale;
		// ConfigurationRepository configurationRepository = new
		// ConfigurationRepository(
		// getApplicationContext());
		// configurationRepository.update(config);
		// onCreate(null);
		// }
		// };
		// }
		// });
		//
		// ((LinearLayout)
		// slidingMainMenuLeft.findViewById(R.id.layoutDateFormat))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// final Configuration config = App.configuration;
		// final DateFormatDialog dateFormatDialog = new DateFormatDialog(
		// BaseActivity.this);
		// dateFormatDialog.show();
		// dateFormatDialog.setFormat(config.getDateFormat());
		// dateFormatDialog.callback = new DateFormatCallback() {
		//
		// @Override
		// public void callback() {
		// ConfigurationRepository configurationRepository = new
		// ConfigurationRepository(
		// getApplicationContext());
		// config.setDateFormat(dateFormatDialog.format
		// .toString());
		//
		// Log.d("DateFormat",
		// dateFormatDialog.format.toString());
		// configurationRepository.update(config);
		// onCreate(null);
		// }
		// };
		// }
		//
		// });
		//
		// renderAccount();
		// renderTransactionCategory();
		// renderTemplate();
	}

	private void renderTemplate() {

		TemplateRepository templateRepository = new TemplateRepository(
				getApplicationContext(), App.configuration.getLocale());

		templateRoot.removeAllViews();

		LinearLayout template;

		for (Template templateTransaction : templateRepository.getAll()) {

			template = (LinearLayout) getLayoutInflater().inflate(
					R.layout.menu_item_template, null, false);

			ImageView imgIcon = (ImageView) template.findViewById(R.id.imgIcon);
			TextView tvTitle = (TextView) template.findViewById(R.id.tvTitle);
			ImageView imgEvent = (ImageView) template
					.findViewById(R.id.imgEvent);
			imgEvent.setTag(templateTransaction.getId());
			imgEvent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							TemplateDetail.class);

					intent.putExtra(App.TEMPLATE_ID,
							Integer.parseInt(String.valueOf(v.getTag())));

					startActivityForResult(intent,
							App.EDIT_TEMPLATE_REQUEST_CODE);
				}
			});

			ImageView imgEventDel = (ImageView) template
					.findViewById(R.id.imgEventDel);
			imgEventDel.setTag(templateTransaction.getId());
			imgEventDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {

					try {

						ConfirmDialog confirmDialog = new ConfirmDialog(
								BaseActivity.this);
						confirmDialog.show(getResources().getString(
								R.string.confirmDeleteTemplate));
						confirmDialog.callback = new ConfirmDialog.Callback() {

							@Override
							public void callback() {
								TemplateRepository templateRepository = new TemplateRepository(
										getApplicationContext(),
										App.configuration.getLocale());

								templateRepository.delete(Integer.parseInt(v
										.getTag().toString()));

								showDialog(
										"",
										getResources().getString(
												R.string.deleteTemplateSuccess));
								updateView = new UpdateView() {

									@Override
									public void update() {
										renderTemplate();
									}
								};
							}
						};
					} catch (Exception e) {
						showDialog(
								"",
								getResources().getString(
										R.string.deleteTemplateyFail));
					}
				}
			});

			imgIcon.setBackgroundResource(templateTransaction
					.getTransactionCategory().getIcon());
			tvTitle.setText(String.valueOf(templateTransaction.getAmount()));

			templateRoot.addView(template);
		}

		// tvAccount.setText(templateTransaction.get);

		//
	}

	protected void renderTransactionCategory() {
		renderTransactionCategoryIncome();
		renderTransactionCategoryExpend();
	}

	@SuppressWarnings("static-access")
	private void renderTransactionCategoryExpend() {
		LinearLayout template;
		TransactionCategoryRepository categoryRepository = new TransactionCategoryRepository(
				getApplicationContext(), App.configuration.getLocale());
		categoryRootExpend.removeAllViewsInLayout();

		for (TransactionCategory category : categoryRepository
				.getByType(TransactionCategory.Type.Expenses)) {

			template = (LinearLayout) slidingMainMenuLeft.inflate(
					getApplicationContext(), R.layout.menu_item_template, null);
			ImageView imgIcon = (ImageView) template.findViewById(R.id.imgIcon);
			TextView tvAccount = (TextView) template.findViewById(R.id.tvTitle);
			ImageView imgEvent = (ImageView) template
					.findViewById(R.id.imgEvent);
			imgEvent.setTag(category.getId());

			imgEvent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							EditTransactionCategory.class);
					intent.putExtra(App.TRANSACTION_CATEGORY_ID,
							Integer.parseInt(v.getTag().toString()));
					startActivityForResult(intent,
							App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE);
				}
			});

			ImageView imgEventDelete = (ImageView) template
					.findViewById(R.id.imgEventDel);
			imgEventDelete.setTag(category.getId());

			imgEventDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {

					try {

						ConfirmDialog confirmDialog = new ConfirmDialog(
								BaseActivity.this);
						confirmDialog.show(getResources().getString(
								R.string.confirmDeleteCategory));
						confirmDialog.callback = new ConfirmDialog.Callback() {

							@Override
							public void callback() {
								TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
										getApplicationContext(),
										App.configuration.getLocale());

								transactionCategoryRepository.delete(Integer
										.parseInt(v.getTag().toString()));

								showDialog(
										"",
										getResources().getString(
												R.string.deleteCategorySuccess));
								updateView = new UpdateView() {

									@Override
									public void update() {
										// renderTransactionCategory();
									}
								};
							}
						};

					} catch (Exception e) {
						showDialog(
								"",
								getResources().getString(
										R.string.deleteCategoryFail));
					}
				}
			});

			imgIcon.setBackgroundResource(category.getIcon());
			tvAccount.setText(category.getName());

			categoryRootExpend.addView(template);
		}
	}

	@SuppressWarnings("static-access")
	private void renderTransactionCategoryIncome() {
		// TransactionCategoryRepository categoryRepository = new
		// TransactionCategoryRepository(
		// getApplicationContext(), App.configuration.getLocale());
		// categoryRootIncome.removeAllViewsInLayout();
		//
		// LinearLayout template;
		//
		// for (TransactionCategory category : categoryRepository
		// .getByType(TransactionCategory.Type.Income)) {
		//
		// template = (LinearLayout) slidingMainMenuLeft.inflate(
		// getApplicationContext(), R.layout.menu_item_template, null);
		// ImageView imgIcon = (ImageView) template.findViewById(R.id.imgIcon);
		// TextView tvAccount = (TextView) template.findViewById(R.id.tvTitle);
		// ImageView imgEvent = (ImageView) template
		// .findViewById(R.id.imgEvent);
		// imgEvent.setTag(category.getId());
		//
		// imgEvent.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent intent = new Intent(getApplicationContext(),
		// EditTransactionCategory.class);
		// intent.putExtra(App.TRANSACTION_CATEGORY_ID,
		// Integer.parseInt(v.getTag().toString()));
		// startActivityForResult(intent,
		// App.EDIT_TRANSACTION_CATEGORY_REQUEST_CODE);
		// }
		// });
		//
		// ImageView imgEventDelete = (ImageView) template
		// .findViewById(R.id.imgEventDel);
		// imgEventDelete.setTag(category.getId());
		//
		// imgEventDelete.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(final View v) {
		//
		// try {
		//
		// ConfirmDialog confirmDialog = new ConfirmDialog(
		// BaseActivity.this);
		// confirmDialog.show(getResources().getString(
		// R.string.confirmDeleteCategory));
		// confirmDialog.callback = new ConfirmDialog.Callback() {
		//
		// @Override
		// public void callback() {
		// TransactionCategoryRepository transactionCategoryRepository = new
		// TransactionCategoryRepository(
		// getApplicationContext(),
		// App.configuration.getLocale());
		//
		// transactionCategoryRepository.delete(Integer
		// .parseInt(v.getTag().toString()));
		//
		// showDialog(
		// "",
		// getResources().getString(
		// R.string.deleteCategorySuccess));
		// updateView = new UpdateView() {
		//
		// @Override
		// public void update() {
		// renderTransactionCategory();
		// }
		// };
		// }
		// };
		//
		// } catch (Exception e) {
		// showDialog(
		// "",
		// getResources().getString(
		// R.string.deleteCategoryFail));
		// }
		// }
		// });
		//
		// imgIcon.setBackgroundResource(category.getIcon());
		// tvAccount.setText(category.getName());
		//
		// categoryRootIncome.addView(template);
		// }
	}

	@SuppressWarnings("static-access")
	private void renderAccount() {
		AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(), App.configuration.getLocale());

		accountRoot.removeAllViewsInLayout();

		LinearLayout template;

		for (Account account : accountRepository.getAll()) {

			template = (LinearLayout) slidingMainMenuLeft.inflate(
					getApplicationContext(), R.layout.menu_item_template, null);
			ImageView imgIcon = (ImageView) template.findViewById(R.id.imgIcon);
			TextView tvAccount = (TextView) template.findViewById(R.id.tvTitle);
			ImageView imgEvent = (ImageView) template
					.findViewById(R.id.imgEvent);
			imgEvent.setTag(account.getId());

			imgEvent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							EditAccountEmptryActivity.class);
					intent.putExtra(App.ACCOUNT_ID,
							Integer.parseInt(v.getTag().toString()));
					startActivityForResult(intent,
							App.EDIT_ACCOUNT_REQUEST_CODE);
				}
			});

			ImageView imgEventDelete = (ImageView) template
					.findViewById(R.id.imgEventDel);
			imgEventDelete.setTag(account.getId());

			imgEventDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {

					try {

						ConfirmDialog confirmDialog = new ConfirmDialog(
								BaseActivity.this);
						confirmDialog.show(getResources().getString(
								R.string.confirmDeleteAccount));
						confirmDialog.callback = new ConfirmDialog.Callback() {

							@Override
							public void callback() {
								AccountRepository accountRepository = new AccountRepository(
										getApplicationContext(),
										App.configuration.getLocale());

								accountRepository.delete(Integer.parseInt(v
										.getTag().toString()));

								showDialog(
										"",
										getResources().getString(
												R.string.deleteAccountSuccess));
								updateView = new UpdateView() {
									@Override
									public void update() {
										onCreate(null);
										slidingMainMenuLeft.showContent();
									}
								};
							}
						};

					} catch (Exception e) {
						showDialog(
								"",
								getResources().getString(
										R.string.deleteAccountFail));
					}
				}
			});

			imgIcon.setBackgroundResource(account.getIcon());
			tvAccount.setText(account.getName());

			accountRoot.addView(template);
		}
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		overridePendingTransition(R.animator.activity_open_scale,
				R.animator.activity_close_translate);

		finish();
	}

	@Override
	public void finish() {

		super.finish();

		overridePendingTransition(R.animator.activity_open_scale,
				R.animator.activity_close_translate);
	}

	protected void showDialog(String title, String message) {
		final Dialog dialog = new Dialog(this);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		dialog.setContentView(R.layout.custom_dialog_message);

		// TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		Button btOK = (Button) dialog.findViewById(R.id.btOk);
		LinearLayout layoutOK = (LinearLayout) dialog
				.findViewById(R.id.layoutOk);

		// tvTitle.setText(title);
		tvMessage.setText(message);

		layoutOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (updateView != null)
					updateView.update();
				// if (returnView != null)
				// returnView.redirect();
				dialog.dismiss();
			}
		});

		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (updateView != null)
					updateView.update();
				// if (returnView != null)
				// returnView.redirect();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	protected void showDialogConfirm(String title, String message,
			final ReturnView confirmView) {
		final Dialog dialog = new Dialog(this);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		dialog.setContentView(R.layout.custom_dialog_confirm);

		// TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		Button btOK = (Button) dialog.findViewById(R.id.btOk);
		Button btCalcel = (Button) dialog.findViewById(R.id.btCancel);

		// tvTitle.setText(title);
		tvMessage.setText(message);

		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					AccountRepository accountRepository = new AccountRepository(
							getApplicationContext(), App.configuration
									.getLocale());

					accountRepository.delete(Integer.parseInt(v.getTag()
							.toString()));

					showDialog("", "ลบบัญชี เรียบร้อย");
					// updateView = new UpdateView() {
					// @Override
					// public void update() {
					onCreate(null);
					slidingMainMenuLeft.showContent();
					// }
					// };
				} catch (Exception e) {
					showDialog("", "ลบบัญชี เกิดข้อผิดพลาด");
				}
			}
		});

		btCalcel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public interface UpdateView {
		void update();
	}

	public interface ReturnView {
		void redirect();
	}
}
