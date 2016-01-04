package com.itsada.imoney;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.itsada.framework.models.Account;
import com.itsada.framework.models.Template;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.models.TransactionCategory.Type;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.TemplateRepository;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.views.TemplateDialog;
import com.itsada.imoney.views.TemplateDialog.Callback;

@SuppressLint({ "NewApi", "InflateParams", "SimpleDateFormat" })
public class CreateTransactionEmptryActivity extends BaseActivity implements
		OnClickListener, LocationListener {

	private com.itsada.framework.models.Account currentAccount,
			currentAccountTo;
	private CalendarView calendarView;
	private TextView tvDate;
	private TextView tvHeaderDate;
	private TextView tvTime;
	private TextView tvHeaderTime;
	private TextView tvCalendarView;
	private TextView tvEvent;

	private LinearLayout amountMessageLayout, accountMessageLayout,
			tranferMessageLayout, transactionCategoryMessageLayout,
			tvCategoryLayout, layoutTroggle;

	private Button btOk;
	private Button btCancel;
	private Button btIncome, btExpand;
	private Button btIsTransfer, btIsNotTransfer;
	private ImageView btPreviousMonth;
	private ImageView btNextMonth;

	private Dialog dialogCalendar;
	private Dialog dialogTime;
	private Dialog dialogCalculate;
	private Dialog dialogCategory;
	private Dialog dialogAccount, dialogAccountTo;

	private EditText etAmount;
	private AutoCompleteTextView etNote;
	private ImageView imgCalculate;
	private ImageView imgCategory;
	private ImageView imgAccount, imgAccountTo;
	private ImageView imgBack;
	private ImageView imgDone;
	private ImageView imgFavorite;
	private TextView tvAccept;
	private TextView tvCancel;
	private TextView tvTemplate;

	private Date transactionDate;
	private TextView tvCategory;
	private TextView tvAccount, tvAccountTo;
	private TransactionCategory transactionCategory;
	private ArrayList<TransactionCategory> categories;
	private ArrayList<String> noteAutoComplease;
	private long transactionId = 0;
	private TemplateDialog templateDialog;
	private Transaction transaction;
	private RelativeLayout accountToLayout, categoryLayout;

	private LocationManager lm;
	private double latitude, longitude;

	// private AutoCompleteTextView autocompleteEditTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_transaction_emptry_layout);

		setActionBar();
		onInit();
	}

	public void initializeCalendar(Dialog v) {
		calendarView = (CalendarView) v.findViewById(R.id.calendarView1);

		// sets whether to show the week number.
		calendarView.setShowWeekNumber(false);

		ViewGroup vg = (ViewGroup) calendarView.getChildAt(0);
		View child = vg.getChildAt(0);

		if (child instanceof TextView)
			((TextView) child).setVisibility(View.GONE);

		// sets the first day of week according to Calendar.
		// here we set Monday as the first day of the Calendar
		calendarView.setFirstDayOfWeek(2);
		vg.getChildAt(1).setVisibility(View.GONE);

		// The background color for the selected week.
		// calendar.setSelectedWeekBackgroundColor(Color.GREEN);

		// sets the color for the dates of an unfocused month.
		// calendarView.setUnfocusedMonthDateColor(Color.GRAY);

		// sets the color for the separator line between weeks.
		// calendarView.setWeekSeparatorLineColor(getResources().getColor(
		// R.color.text_app));
		// calendar.setWeekNumberColor(Color.RED);
		// calendar.setBackgroundColor(Color.BLUE);
		// calendar.setScrollbarFadingEnabled(false);

		// calendar.setDate(Calendar.getInstance(new
		// Locale("th")).getTimeInMillis());

		// sets the color for the vertical bar shown at the beginning and at the
		// end of the selected date.
		// calendar.setSelectedDateVerticalBar(Color.DKGRAY);

		// sets the listener to be notified upon selected date change.
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			// show the selected date as a toast
			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int day) {
				tvDate.setText(new SimpleDateFormat(App.configuration
						.getDateFormat()).format(view.getDate()));

				tvHeaderDate.setText(new SimpleDateFormat(App.configuration
						.getDateFormat()).format(new Date(calendarView
						.getDate())));
				// Toast.makeText(getApplicationContext(),
				// day + "/" + month + "/" + year, Toast.LENGTH_LONG)
				// .show();
			}
		});

	}

	@Override
	protected void setActionBar() {
		try {

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(
					R.layout.custom_actionbar_event, null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			tvEvent = (TextView) mCustomView.findViewById(R.id.tvEvent);
			imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
			imgBack.setOnClickListener(this);
			imgDone = (ImageView) mCustomView.findViewById(R.id.imgDone);
			imgDone.setOnClickListener(this);

			imgFavorite = (ImageView) mCustomView.findViewById(R.id.imgCancel);
			imgFavorite.setVisibility(View.VISIBLE);
			imgFavorite
					.setBackgroundResource(R.drawable.ic_content_paste_white_24dp);
			// imgFavorite.setVisibility(View.GONE);
			imgDone.setVisibility(View.GONE);

			imgFavorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					templateDialog = new TemplateDialog(
							CreateTransactionEmptryActivity.this);

					templateDialog.callback = new Callback() {

						@Override
						public void updateView() {

							Log.d(TAG, "call");

							Transaction transaction = templateDialog
									.getTransaction();

							currentAccount = transaction.getAccount();
							transactionCategory = transaction
									.getTransactionCategory();

							etAmount.setText(String.valueOf(transaction
									.getAmount()));
							tvAccount.setText(transaction.getAccount()
									.getName());
							imgAccount.setBackgroundResource(transaction
									.getAccount().getIcon());

							tvCategory.setText(transaction
									.getTransactionCategory().getName());
							imgCategory.setBackgroundResource(transaction
									.getTransactionCategory().getIcon());
							etNote.setText(transaction.getName());
							tvDate.setText(new SimpleDateFormat(
									App.configuration.getDateFormat())
									.format(transaction.getCreateDate()));
							tvTime.setText(timeFormat.format(transaction
									.getCreateDate()));

							if (transaction.getTransactionType().equals(
									"Income")) {
								etAmount.setTextColor(Color.rgb(0, 100, 0));
								btIncome.setBackgroundResource(R.drawable.troggle_on_active_shape);
								btExpand.setBackgroundResource(R.drawable.troggle_off_normal_shape);
								btIncome.setTag(R.drawable.troggle_on_active_shape);
								btExpand.setTag(R.drawable.troggle_off_normal_shape);
							} else {
								etAmount.setTextColor(Color.rgb(200, 0, 0));
								btIncome.setBackgroundResource(R.drawable.troggle_on_normal_shape);
								btExpand.setBackgroundResource(R.drawable.troggle_off_active_shape);
								btIncome.setTag(R.drawable.troggle_on_normal_shape);
								btExpand.setTag(R.drawable.troggle_off_active_shape);
							}
						}
					};
					templateDialog.show(getResources().getString(
							R.string.template));
				}
			});

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void createViewDeposit() {

		tvEvent.setText(getResources().getString(
				R.string.createTransactionDeposit));
		this.imgCategory.setBackgroundResource(R.drawable.ic_mood_black);
		this.tvCategory.setText(getResources().getString(
				R.string.pleaseSelectTransactionCategory));
		this.etAmount.setTextColor(Color.rgb(0, 100, 00));
	}

	private void createViewWithdraw() {
		tvEvent.setText(getResources().getString(
				R.string.createTransactionWithdraw));
		this.imgCategory.setBackgroundResource(R.drawable.ic_mood_bad_black);
		this.tvCategory.setText(getResources().getString(
				R.string.pleaseSelectTransactionCategory));
		this.etAmount.setTextColor(Color.rgb(200, 0, 0));
	}

	protected void showDialogCalendar(Activity activity, String message) {
		dialogCalendar = new Dialog(activity);
		dialogCalendar.getWindow();
		dialogCalendar.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogCalendar.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogCalendar.setCancelable(false);

		dialogCalendar.setContentView(R.layout.custom_dialog_calendar);

		this.btOk = (Button) dialogCalendar.findViewById(R.id.btOk);
		this.btOk.setOnClickListener(this);
		this.btCancel = (Button) dialogCalendar.findViewById(R.id.btCancel);
		this.btCancel.setOnClickListener(this);
		this.tvHeaderDate = (TextView) dialogCalendar.findViewById(R.id.tvDate);

		this.tvCalendarView = (TextView) dialogCalendar
				.findViewById(R.id.tvCalendarView);

		this.btPreviousMonth = (ImageView) dialogCalendar
				.findViewById(R.id.btPreviousMonth);
		this.btPreviousMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				long date = calendarView.getDate();

				Calendar c = Calendar.getInstance();
				c.setTime(new Date(date));
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) - 1;
				int day = c.get(Calendar.DATE);

				GregorianCalendar gregorianCalendar = new GregorianCalendar(
						year, month, day);
				calendarView.setDate(gregorianCalendar.getTimeInMillis());

				if (App.configuration.getLocale().equals(Locale.ENGLISH))
					tvCalendarView.setText(monthYearEnFormat
							.format(gregorianCalendar.getTime()));
				else
					tvCalendarView.setText(monthYearThFormat
							.format(gregorianCalendar.getTime()));

			}
		});

		this.btNextMonth = (ImageView) dialogCalendar
				.findViewById(R.id.btNextMonth);
		this.btNextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				long date = calendarView.getDate();

				Calendar c = Calendar.getInstance();
				c.setTime(new Date(date));
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) + 1;
				int day = c.get(Calendar.DATE);

				GregorianCalendar gregorianCalendar = new GregorianCalendar(
						year, month, day);
				calendarView.setDate(gregorianCalendar.getTimeInMillis());
				// tvCalendarView.setText(monthyearFormat.format(gregorianCalendar
				// .getTime()));
				if (App.configuration.getLocale().equals(Locale.ENGLISH))
					tvCalendarView.setText(monthYearEnFormat
							.format(gregorianCalendar.getTime()));
				else
					tvCalendarView.setText(monthYearThFormat
							.format(gregorianCalendar.getTime()));
			}
		});

		initializeCalendar(dialogCalendar);

		if (App.configuration.getLocale().equals(Locale.ENGLISH)) {
			tvCalendarView.setText(monthYearEnFormat.format(new Date(
					calendarView.getDate())));

		} else {
			tvCalendarView.setText(monthYearThFormat.format(new Date(
					calendarView.getDate())));
		}

		this.tvHeaderDate.setText(new SimpleDateFormat(App.configuration
				.getDateFormat()).format(new Date(calendarView.getDate())));
		// this.tvCalendarView.setText(monthyearFormat.format(new Date(
		// calendarView.getDate())));

		// ((TextView) dialog.findViewById(R.id.message)).setText(message);

		// dialog.findViewById(R.id.submit).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });

		dialogCalendar.show();

	}

	@SuppressWarnings("deprecation")
	protected void showTimeDialog(Activity activity, String message) {
		dialogTime = new Dialog(activity);
		dialogTime.getWindow();
		dialogTime.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogTime.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogTime.setCancelable(false);

		dialogTime.setContentView(R.layout.custom_dialog_time);

		final TimePicker timePicker = (TimePicker) dialogTime
				.findViewById(R.id.timePicker1);
		// timePicker.setIs24HourView(true);

		Log.w(TAG,
				calendarTh.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendarTh.get(Calendar.MINUTE));

		timePicker.setCurrentHour(calendarTh.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendarTh.get(Calendar.MINUTE));

		tvHeaderTime = (TextView) dialogTime.findViewById(R.id.tvTime);
		tvHeaderTime.setText(timeFormat.format(new Date(2015, 1, 1, calendarTh
				.get(Calendar.HOUR_OF_DAY), calendarTh.get(Calendar.MINUTE))));

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				tvHeaderTime.setText(timeFormat.format(new Date(2015, 1, 1,
						hourOfDay, minute)));
			}
		});

		Button btOk = (Button) dialogTime.findViewById(R.id.btOk);
		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.w(TAG,
						timePicker.getCurrentHour() + ":"
								+ timePicker.getCurrentMinute());

				transactionDate.setHours(timePicker.getCurrentHour());
				transactionDate.setMinutes(timePicker.getCurrentMinute());

				tvTime.setText(timeFormat.format(transactionDate));

				Log.w(TAG, transactionDate.toString());
				dialogTime.dismiss();
			}
		});

		Button btCancel = (Button) dialogTime.findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogTime.dismiss();
			}
		});

		dialogTime.show();
	}

	protected void showCalculateDialog(Activity activity, String message) {
		dialogCalculate = new Dialog(activity);
		dialogCalculate.getWindow();
		dialogCalculate.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogCalculate.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogCalculate.setCancelable(false);

		dialogCalculate.setContentView(R.layout.custom_dialog_time);

		dialogCalculate.show();
	}

	protected void showCategoryDialog(Activity activity, String message) {
		dialogCategory = new Dialog(activity);
		dialogCategory.getWindow();
		dialogCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogCategory.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogCategory.setCancelable(true);

		dialogCategory
				.setContentView(R.layout.custom_dialog_transaction_category_icon);

		((ImageView) dialogCategory.findViewById(R.id.imgBack))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogCategory.dismiss();
					}
				});

		LinearLayout root = (LinearLayout) dialogCategory
				.findViewById(R.id.root);

		ImageView imgAddCategory = (ImageView) dialogCategory
				.findViewById(R.id.imgAddCategory);
		imgAddCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CreateTransactionCategory.class);

				startActivityForResult(intent,
						App.CREATE_TRANSACTION_CATEGORY_FROM_T_REQUEST_CODE);

			}
		});

		LinearLayout groupLayout;

		final TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
				getApplicationContext(), App.configuration.getLocale());		

		if (btIncome.getTag().equals(R.drawable.troggle_on_active_shape)) {
			((TextView) dialogCategory.findViewById(R.id.tvEvent))
					.setText(getResources().getString(
							R.string.transactionDeposit));
			categories = transactionCategoryRepository.getByType(Type.Income);
		} else {
			((TextView) dialogCategory.findViewById(R.id.tvEvent))
					.setText(getResources().getString(
							R.string.transactionWithdraw));
			categories = transactionCategoryRepository.getByType(Type.Expenses);
		}

		for (TransactionCategory category : categories) {
			groupLayout = (LinearLayout) dialogCategory.getLayoutInflater()
					.inflate(R.layout.category_transaction_template, null);

			groupLayout.setTag(category.getId());

			((ImageView) groupLayout.findViewById(R.id.imgIcon))
					.setBackgroundResource(category.getIcon());
			((TextView) groupLayout.findViewById(R.id.tvName)).setText(category
					.getName());

			groupLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (TransactionCategory category : categories) {
						if (category.getId() == Integer.parseInt(v.getTag()
								.toString())) {
							transactionCategory = category;
							break;
						}
					}

					tvCategory.setText(transactionCategory.getName());
					imgCategory.setBackgroundResource(transactionCategory
							.getIcon());
					// tvCategory.setTextColor(getResources().getColor(
					// R.color.message_valid));

					transactionCategoryMessageLayout.setVisibility(View.GONE);
					transactionCategoryMessageLayout
							.startAnimation(AnimationUtils.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat0));

					dialogCategory.dismiss();

				}
			});

			root.addView(groupLayout);
		}

		dialogCategory.show();
	}

	private void showAccountDialog(Activity activity, String string) {
		dialogAccount = new Dialog(activity);
		dialogAccount.getWindow();
		dialogAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogAccount.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogAccount.setCancelable(false);

		dialogAccount
				.setContentView(R.layout.custom_dialog_transaction_category_icon);

		((ImageView) dialogAccount.findViewById(R.id.imgAddCategory))
				.setVisibility(View.GONE);

		((TextView) dialogAccount.findViewById(R.id.tvEvent))
				.setText(getResources().getString(R.string.account));
		((ImageView) dialogAccount.findViewById(R.id.imgBack))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogAccount.dismiss();
					}
				});

		LinearLayout root = (LinearLayout) dialogAccount
				.findViewById(R.id.root);
		LinearLayout groupLayout;

		final AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(), App.configuration.getLocale());

		final ArrayList<Account> accounts = accountRepository.getAll();

		for (Account account : accounts) {
			groupLayout = (LinearLayout) dialogAccount.getLayoutInflater()
					.inflate(R.layout.category_transaction_template, null);

			groupLayout.setTag(account.getId());

			((ImageView) groupLayout.findViewById(R.id.imgIcon))
					.setBackgroundResource(account.getIcon());
			((TextView) groupLayout.findViewById(R.id.tvName)).setText(account
					.getName());

			groupLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (Account account : accounts) {
						if (account.getId() == Integer.parseInt(v.getTag()
								.toString())) {
							currentAccount = account;
							break;
						}
					}

					tvAccount.setText(currentAccount.getName());
					imgAccount.setBackgroundResource(currentAccount.getIcon());
					// tvAccount.setTextColor(getResources().getColor(
					// R.color.message_valid));

					accountMessageLayout.setVisibility(View.GONE);
					accountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat0));

					dialogAccount.dismiss();

				}
			});

			root.addView(groupLayout);
		}

		dialogAccount.show();

	}

	private void showAccountToDialog(Activity activity, String string) {
		dialogAccountTo = new Dialog(activity);
		dialogAccountTo.getWindow();
		dialogAccountTo.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogAccountTo.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogAccountTo.setCancelable(false);

		dialogAccountTo
				.setContentView(R.layout.custom_dialog_transaction_category_icon);

		((ImageView) dialogAccountTo.findViewById(R.id.imgAddCategory))
				.setVisibility(View.GONE);

		((TextView) dialogAccountTo.findViewById(R.id.tvEvent))
				.setText(getResources().getString(R.string.account));
		((ImageView) dialogAccountTo.findViewById(R.id.imgBack))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogAccountTo.dismiss();
					}
				});

		LinearLayout root = (LinearLayout) dialogAccountTo
				.findViewById(R.id.root);
		LinearLayout groupLayout;

		final AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(), App.configuration.getLocale());

		final ArrayList<Account> accounts = accountRepository.getAll();

		for (Account account : accounts) {
			groupLayout = (LinearLayout) dialogAccountTo.getLayoutInflater()
					.inflate(R.layout.category_transaction_template, null);

			groupLayout.setTag(account.getId());

			((ImageView) groupLayout.findViewById(R.id.imgIcon))
					.setBackgroundResource(account.getIcon());
			((TextView) groupLayout.findViewById(R.id.tvName)).setText(account
					.getName());

			groupLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (Account account : accounts) {
						if (account.getId() == Integer.parseInt(v.getTag()
								.toString())) {
							currentAccountTo = account;
							break;
						}
					}

					tvAccountTo.setText(currentAccountTo.getName());
					imgAccountTo.setBackgroundResource(currentAccountTo
							.getIcon());
					// tvAccount.setTextColor(getResources().getColor(
					// R.color.message_valid));

					tranferMessageLayout.setVisibility(View.GONE);
					tranferMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat0));

					dialogAccountTo.dismiss();

				}
			});

			root.addView(groupLayout);
		}

		dialogAccountTo.show();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvDate:
			showDialogCalendar(this, "Hi");
			break;
		case R.id.tvTime:
			showTimeDialog(this, "Hi");
			break;
		case R.id.imgCalculate:
			showCalculateDialog(this, "");
			break;
		case R.id.btOk:

			this.transactionDate = new Date(calendarView.getDate());
			dialogCalendar.dismiss();

			break;

		case R.id.btCancel:
			dialogCalendar.dismiss();
			break;

		case R.id.imgBack:
			finish();
			break;
		case R.id.categoryLayout:
			showCategoryDialog(this, "");
			break;
		case R.id.accountLayout:
			showAccountDialog(this, "");
			break;
		case R.id.accountToLayout:
			showAccountToDialog(this, "");
			break;
		default:
			break;
		}

	}

	private void createTransfer() {

		if (etAmount.getText().toString().equals("")

				|| tvAccount
						.getText()
						.toString()
						.equals(getResources().getString(
								R.string.pleaseSelectAccount))
				|| tvAccountTo
						.getText()
						.toString()
						.equals(getResources().getString(
								R.string.pleaseSelectAccount))) {

			if (etAmount.getText().toString().equals("")) {
				amountMessageLayout.setVisibility(View.VISIBLE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));
			} else {
				amountMessageLayout.setVisibility(View.GONE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}

			if (tvAccount
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectAccount))) {
				accountMessageLayout.setVisibility(View.VISIBLE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));

			} else {
				accountMessageLayout.setVisibility(View.GONE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}

			if (tvAccountTo
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectAccount))) {
				tranferMessageLayout.setVisibility(View.VISIBLE);
				tranferMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));

			} else {
				tranferMessageLayout.setVisibility(View.GONE);
				tranferMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}

		} else {

			if (currentAccountTo == null) {
				tranferMessageLayout.setVisibility(View.VISIBLE);
				tranferMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));
			} else {

				if (currentAccount.getId() == currentAccountTo.getId()) {
					accountMessageLayout.setVisibility(View.VISIBLE);
					accountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat500));
					tranferMessageLayout.setVisibility(View.VISIBLE);
					tranferMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat500));
				} else {
					accountMessageLayout.setVisibility(View.GONE);
					accountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat0));
					tranferMessageLayout.setVisibility(View.GONE);
					tranferMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat0));

					try {

						double amount = Double.parseDouble(this.etAmount
								.getText().toString());
						String note = this.etNote.getText().toString();
						if (note.equals("")) {
							note = currentAccount.getName() + " -> "
									+ currentAccountTo.getName();
						}
						Date date = transactionDate;

						AccountRepository accountRepository = new AccountRepository(
								getApplicationContext(),
								App.configuration.getLocale());
						accountRepository.transfer(currentAccount,
								currentAccountTo, amount, date, note);

						this.etAmount = null;
						this.etNote = null;

						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Success");
						setResult(RESULT_OK, returnIntent);

						finish();

					} catch (Exception e) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", "Fail");
						setResult(RESULT_OK, returnIntent);

						finish();
					}
				}
			}
		}

	}

	private void createTransaction() {

		if (etAmount.getText().toString().equals("")
				|| tvCategory
						.getText()
						.toString()
						.equals(getResources().getString(
								R.string.pleaseSelectTransactionCategory))
				|| tvAccount
						.getText()
						.toString()
						.equals(getResources().getString(
								R.string.pleaseSelectAccount))) {

			if (etAmount.getText().toString().equals("")) {
				amountMessageLayout.setVisibility(View.VISIBLE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));
			} else {
				amountMessageLayout.setVisibility(View.GONE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}

			if (tvAccount
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectAccount))) {
				accountMessageLayout.setVisibility(View.VISIBLE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));
				// tvAccount.setTextColor(getResources().getColor(
				// R.color.message_invalid));
			} else {
				accountMessageLayout.setVisibility(View.GONE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}
			// tvAccount.setTextColor(getResources().getColor(
			// R.color.message_valid));

			if (tvCategory
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectTransactionCategory))) {
				transactionCategoryMessageLayout.setVisibility(View.VISIBLE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat500));
				// tvCategory.setTextColor(getResources().getColor(
				// R.color.message_invalid));
			} else {
				transactionCategoryMessageLayout.setVisibility(View.GONE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));
			}
			// tvCategory.setTextColor(getResources().getColor(
			// R.color.message_valid));

		} else {

			try {

				transaction = new Transaction(currentAccount, this.etNote
						.getText().toString(), "",
						Double.parseDouble(this.etAmount.getText().toString()),
						this.transactionDate, transactionCategory, latitude,
						longitude);

				// transaction.setAmount(Double.parseDouble(this.etAmount
				// .getText().toString()));
				// transaction.setName(this.etNote.getText().toString());
				// transaction.setCreateDate(this.transactionDate);

				if (btIncome.getTag()
						.equals(R.drawable.troggle_on_active_shape))
					transaction.setTransactionType(TransactionType.Income
							.name());
				else
					transaction.setTransactionType(TransactionType.Expenses
							.name());

				// transaction.setAccount(currentAccount);
				// transaction.setTransactionCategory(transactionCategory);

				AccountRepository accountRepository = new AccountRepository(
						getApplicationContext(), App.configuration.getLocale());
				transactionId = accountRepository.addTransaction(
						currentAccount, transaction);

				// if (transactionId > 0) {
				//
				// try {
				//
				// TransactionCategoryRepository repository = new
				// TransactionCategoryRepository(
				// getApplicationContext());
				// TransactionCategory category = repository
				// .getById(transaction.getTransactionCategory()
				// .getId());
				// String month = new SimpleDateFormat("MM/yyyy")
				// .format(Calendar.getInstance().getTime());
				// // if(!month.equals(category.getHitDate())){
				// int h = category.getHit();
				// category.setHit(++h);
				// category.setHitDate(month);
				//
				// repository.update(category);
				// // }
				//
				// } catch (Exception e2) {
				//
				// }
				// }

				this.etAmount = null;
				this.etNote = null;

				// showDialog("", "เพิ่มธุรกรรม เรียบร้อย");

				// returnView = new ReturnView() {
				//
				// @Override
				// public void redirect() {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", "Success");
				setResult(RESULT_OK, returnIntent);

				finish();
				// }
				// };

			} catch (Exception e) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", "Fail");
				setResult(RESULT_OK, returnIntent);

				finish();
			}

		}
	}

	protected void createTransactionTemplate() {
		createTransaction();

		TemplateRepository templateRepository = new TemplateRepository(
				getApplicationContext(), App.configuration.getLocale());

		if (transaction != null)
			templateRepository.add(new Template(transaction));
	}

	@Override
	protected void onResume() {

		super.onResume();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isNetwork = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isNetwork) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
					10, this);
			Location loc = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				latitude = loc.getLatitude();
				longitude = loc.getLongitude();
			}
		}

		if (isGPS) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
					this);
			Location loc = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc != null) {
				latitude = loc.getLatitude();
				longitude = loc.getLongitude();
			}
		}
	}

	public void onPause() {
		super.onPause();
		lm.removeUpdates(this);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onInit() {

		TAG = CreateTransactionEmptryActivity.class.getName();
		((LinearLayout) findViewById(R.id.mainLayout))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {

						hideKeyboard(v);
						return false;
					}
				});

		noteAutoComplease = new TransactionRepository(getApplicationContext(),
				App.configuration.getLocale()).getTransactionHit();

		ArrayAdapter<String> noteAutoCompleaseAdapter = new ArrayAdapter<String>(
				this, android.R.layout.select_dialog_item, noteAutoComplease);

		this.transactionDate = Calendar.getInstance().getTime();

		this.tvDate = (TextView) findViewById(R.id.tvDate);

		this.tvDate.setText(new SimpleDateFormat(App.configuration
				.getDateFormat()).format(Calendar.getInstance().getTime()));
		this.tvDate.setOnClickListener(this);

		this.tvTime = (TextView) findViewById(R.id.tvTime);
		this.tvTime
				.setText(timeFormat.format(Calendar.getInstance().getTime()));
		this.tvTime.setOnClickListener(this);

		this.btIncome = (Button) findViewById(R.id.btIncome);
		this.btExpand = (Button) findViewById(R.id.btExpand);

		// initial button expend on
		this.btIncome.setTag(R.drawable.troggle_on_normal_shape);
		this.btExpand.setTag(R.drawable.troggle_off_active_shape);
		btIncome.setBackgroundResource(R.drawable.troggle_on_normal_shape);
		btExpand.setBackgroundResource(R.drawable.troggle_off_active_shape);

		this.etAmount = (EditText) findViewById(R.id.etAmount);

		btIncome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIncome.setBackgroundResource(R.drawable.troggle_on_active_shape);
				btExpand.setBackgroundResource(R.drawable.troggle_off_normal_shape);
				btIncome.setTag(R.drawable.troggle_on_active_shape);
				btExpand.setTag(R.drawable.troggle_off_normal_shape);
				createViewDeposit();
			}
		});
		btExpand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIncome.setBackgroundResource(R.drawable.troggle_on_normal_shape);
				btExpand.setBackgroundResource(R.drawable.troggle_off_active_shape);
				btIncome.setTag(R.drawable.troggle_on_normal_shape);
				btExpand.setTag(R.drawable.troggle_off_active_shape);
				createViewWithdraw();
			}
		});

		accountToLayout = (RelativeLayout) findViewById(R.id.accountToLayout);
		tvCategoryLayout = (LinearLayout) findViewById(R.id.tvCategoryLayout);
		categoryLayout = (RelativeLayout) findViewById(R.id.categoryLayout);
		layoutTroggle = (LinearLayout) findViewById(R.id.layoutTroggle);

		this.btIsTransfer = (Button) findViewById(R.id.btIsTranfer);
		this.btIsNotTransfer = (Button) findViewById(R.id.btIsNotTranfer);

		this.btIsTransfer.setTag(R.drawable.troggle_on_active_shape);
		this.btIsNotTransfer.setTag(R.drawable.troggle_off_normal_shape);

		btIsTransfer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIsTransfer
						.setBackgroundResource(R.drawable.troggle_on_active_shape);
				btIsNotTransfer
						.setBackgroundResource(R.drawable.troggle_off_normal_shape);
				btIsTransfer.setTag(R.drawable.troggle_on_active_shape);
				btIsNotTransfer.setTag(R.drawable.troggle_off_normal_shape);

				etAmount.setText(null);
				imgAccount
						.setBackgroundResource(R.drawable.ic_account_balance_black);
				imgAccountTo
						.setBackgroundResource(R.drawable.ic_account_balance_black);
				tvAccount.setText(getResources().getString(
						R.string.pleaseSelectAccount));
				tvAccountTo.setText(getResources().getString(
						R.string.pleaseSelectAccount));
				etNote.setText("");
				btIncome.setBackgroundResource(R.drawable.troggle_on_active_shape);
				btExpand.setBackgroundResource(R.drawable.troggle_off_normal_shape);
				createViewDeposit();

				accountToLayout.setVisibility(View.GONE);
				tvCategoryLayout.setVisibility(View.VISIBLE);
				categoryLayout.setVisibility(View.VISIBLE);
				layoutTroggle.setVisibility(View.VISIBLE);

				amountMessageLayout.setVisibility(View.GONE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				accountMessageLayout.setVisibility(View.GONE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				transactionCategoryMessageLayout.setVisibility(View.GONE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				tranferMessageLayout.setVisibility(View.GONE);
				tranferMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				((TextView) findViewById(R.id.accountForm))
						.setVisibility(View.GONE);
				((TextView) findViewById(R.id.accountTo))
						.setVisibility(View.GONE);
				imgFavorite.setVisibility(View.VISIBLE);
				tvTemplate.setVisibility(View.VISIBLE);
			}
		});
		btIsNotTransfer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btIsTransfer
						.setBackgroundResource(R.drawable.troggle_on_normal_shape);
				btIsNotTransfer
						.setBackgroundResource(R.drawable.troggle_off_active_shape);
				btIsTransfer.setTag(R.drawable.troggle_on_normal_shape);
				btIsNotTransfer.setTag(R.drawable.troggle_off_active_shape);

				etAmount.setText(null);
				etAmount.setTextColor(Color.BLACK);
				imgAccount
						.setBackgroundResource(R.drawable.ic_account_balance_black);
				imgAccountTo
						.setBackgroundResource(R.drawable.ic_account_balance_black);
				tvAccount.setText(getResources().getString(
						R.string.pleaseSelectAccount));
				tvAccountTo.setText(getResources().getString(
						R.string.pleaseSelectAccount));
				etNote.setText("");

				accountToLayout.setVisibility(View.VISIBLE);
				tvCategoryLayout.setVisibility(View.GONE);
				categoryLayout.setVisibility(View.GONE);
				layoutTroggle.setVisibility(View.GONE);

				amountMessageLayout.setVisibility(View.GONE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				accountMessageLayout.setVisibility(View.GONE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				transactionCategoryMessageLayout.setVisibility(View.GONE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				tranferMessageLayout.setVisibility(View.GONE);
				tranferMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(CreateTransactionEmptryActivity.this,
								R.anim.alpha_repeat0));

				((TextView) findViewById(R.id.accountForm))
						.setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.accountTo))
						.setVisibility(View.VISIBLE);

				imgFavorite.setVisibility(View.GONE);
				tvTemplate.setVisibility(View.GONE);
			}
		});

		this.amountMessageLayout = (LinearLayout) findViewById(R.id.amountMessageLayout);
		this.tranferMessageLayout = (LinearLayout) findViewById(R.id.tranferMessageLayout);
		this.accountMessageLayout = (LinearLayout) findViewById(R.id.accountMessageLayout);
		this.transactionCategoryMessageLayout = (LinearLayout) findViewById(R.id.transactionCategoryMessageLayout);

		this.etAmount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					amountMessageLayout.setVisibility(View.GONE);
					amountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(getApplicationContext(),
									R.anim.alpha_repeat0));
				} else {
					amountMessageLayout.setVisibility(View.VISIBLE);
					amountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(
									CreateTransactionEmptryActivity.this,
									R.anim.alpha_repeat500));
				}
			}
		});

		this.etNote = (AutoCompleteTextView) findViewById(R.id.etNote);
		this.etNote.setAdapter(noteAutoCompleaseAdapter);

		this.imgCalculate = (ImageView) findViewById(R.id.imgCalculate);
		this.imgCalculate.setOnClickListener(this);
		this.imgCategory = (ImageView) findViewById(R.id.imgCategory);
		this.imgAccount = (ImageView) findViewById(R.id.imgAccount);
		this.imgAccount
				.setBackgroundResource(R.drawable.ic_account_balance_black);
		this.imgAccountTo = (ImageView) findViewById(R.id.imgAccountTo);
		this.imgAccountTo
				.setBackgroundResource(R.drawable.ic_account_balance_black);
		this.tvCategory = (TextView) findViewById(R.id.tvCategory);
		this.tvAccept = (TextView) findViewById(R.id.tvAccept);
		this.tvCancel = (TextView) findViewById(R.id.tvCancel);
		this.tvTemplate = (TextView) findViewById(R.id.tvTemplate);

		// tvAccept.setEnabled(false);
		// tvAccept.setTextColor(Color.GRAY);

		if (btIncome.getTag().equals(R.drawable.troggle_on_active_shape))
			createViewDeposit();
		else
			createViewWithdraw();

		((RelativeLayout) findViewById(R.id.categoryLayout))
				.setOnClickListener(this);

		this.tvAccount = (TextView) findViewById(R.id.tvAccount);
		this.tvAccountTo = (TextView) findViewById(R.id.tvAccountTo);

		((RelativeLayout) findViewById(R.id.accountLayout))
				.setOnClickListener(this);

		((RelativeLayout) findViewById(R.id.accountToLayout))
				.setOnClickListener(this);

		this.tvAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("btIsNotTransfer",
						String.valueOf(btIsNotTransfer.getTag()));
				Log.d("troggle_off_active_shape",
						String.valueOf(R.drawable.troggle_off_active_shape));

				if (btIsNotTransfer.getTag().equals(
						R.drawable.troggle_off_active_shape)) {
					createTransfer();
				} else {
					createTransaction();
				}
			}
		});

		this.tvTemplate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createTransactionTemplate();
			}
		});

		this.tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);

				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		// if (resultCode == RESULT_OK) {
		// String result = data.getStringExtra("result");
		//
		// if (result.equals("Success")) {
		//
		// App.isUpdateTransactionCatrgory = true;
		// dialogCategory.dismiss();
		// showCategoryDialog(this, "");
		// }
		// }
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		Log.d(TAG, "Destroy");
		clearVariables();
		System.gc();
	}

	private void clearVariables() {
		currentAccount = null;
		calendarView = null;
		tvDate = null;
		tvHeaderDate = null;
		tvTime = null;
		tvHeaderTime = null;
		tvCalendarView = null;
		tvEvent = null;

		amountMessageLayout = null;
		accountMessageLayout = null;
		transactionCategoryMessageLayout = null;

		btOk = null;
		btCancel = null;
		btIncome = null;
		btExpand = null;
		btPreviousMonth = null;
		btNextMonth = null;

		dialogCalendar = null;
		dialogTime = null;
		dialogCalculate = null;
		dialogCategory = null;
		dialogAccount = null;

		etAmount = null;
		etNote = null;
		imgCalculate = null;
		imgCategory = null;
		imgAccount = null;
		imgBack = null;
		imgDone = null;
		tvAccept = null;
		tvCancel = null;
		tvTemplate = null;

		transactionDate = null;
		tvCategory = null;
		tvAccount = null;
		transactionCategory = null;
		categories = null;
	}

	@Override
	public void onLocationChanged(Location location) {

		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
