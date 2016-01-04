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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.itsada.framework.models.Transaction.TransactionType;
import com.itsada.framework.models.TransactionCategory;
import com.itsada.framework.models.TransactionCategory.Type;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.framework.repository.SQLite.TemplateRepository;
import com.itsada.framework.repository.SQLite.TransactionCategoryRepository;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.views.ConfirmDialog;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class TemplateDetail extends BaseActivity implements OnClickListener {

	private Account currentAccount;
	private CalendarView calendarView;
	private TextView tvDate;
	private TextView tvHeaderDate;
	private TextView tvTime;
	private TextView tvHeaderTime;
	private TextView tvCalendarView;
	private TextView tvEvent;
	private TextView tvAccount;
	private TextView tvAccept;
	private TextView tvCancel;

	private LinearLayout amountMessageLayout, accountMessageLayout,
			transactionCategoryMessageLayout;
	private Button btOk;
	private Button btCancel;
	private Button btIncome, btExpand;
	private ImageView btPreviousMonth;
	private ImageView btNextMonth;

	private EditText etAmount;
	private AutoCompleteTextView etNote;
	private TextView tvAmount;
	private TextView tvNote;
	// private ImageView imgCalculate;
	private ImageView imgCategory;
	private ImageView imgAccount;
	private ImageView imgBack;
	private ImageView imgEdit;
	private ImageView imgDelete;

	private Date transactionDate;
	private TextView tvCategory;
	private TransactionCategory transactionCategory;

	private TemplateRepository templateRepository;
	private ArrayList<TransactionCategory> categories;
	private ArrayList<String> noteAutoComplease;

	private Dialog dialogCalendar;
	private Dialog dialogTime;
	private Dialog dialogCategory;
	private Dialog dialogAccount;
	private Template template;
	private boolean isUpdateTransaction = false;
	private ArrayAdapter<String> noteAutoCompleaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.transaction_detail_layout);

		setActionBar();
		onInit();
	}

	@Override
	protected void onInit() {

		templateRepository = new TemplateRepository(
				getApplicationContext(),
				App.configuration.getLocale());
		template = templateRepository.getById(getIntent().getIntExtra(
				App.TEMPLATE_ID, 0));
				

		currentAccount = template.getAccount();
		// currentAccount = getAccount(
		// getIntent().getIntExtra(App.ACCOUNT_TYPE_ID, 0), getIntent()
		// .getIntExtra(App.ACCOUNT_ID, 0));

		noteAutoComplease = new TransactionRepository(getApplicationContext(),
				App.configuration.getLocale())
				.getTransactionHit();

		noteAutoCompleaseAdapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, noteAutoComplease);

		transactionDetail(template);
	}

	private void transactionDetail(Template template) {
		this.transactionDate = Calendar.getInstance().getTime();

		transactionCategory = template.getTransactionCategory();

		this.tvDate = (TextView) findViewById(R.id.tvDate);
		this.tvDate.setText(new SimpleDateFormat(App.configuration
				.getDateFormat()).format(template.getCreateDate()));

		this.tvTime = (TextView) findViewById(R.id.tvTime);
		this.tvTime.setText(timeFormat.format(template.getCreateDate()));

		this.btIncome = (Button) findViewById(R.id.btIncome);
		this.btExpand = (Button) findViewById(R.id.btExpand);

		if (template.getTransactionType().equals(
				TransactionType.Expenses.name())) {
			btIncome.setBackgroundResource(R.drawable.troggle_on_normal_shape);
			btExpand.setBackgroundResource(R.drawable.troggle_off_active_shape);
			btIncome.setTag(R.drawable.troggle_on_normal_shape);
			btExpand.setTag(R.drawable.troggle_off_active_shape);
		} else {
			btIncome.setBackgroundResource(R.drawable.troggle_on_active_shape);
			btExpand.setBackgroundResource(R.drawable.troggle_off_normal_shape);
			btIncome.setTag(R.drawable.troggle_on_active_shape);
			btExpand.setTag(R.drawable.troggle_off_normal_shape);
		}

		this.tvAmount = (TextView) findViewById(R.id.tvAmount);
		this.tvNote = (TextView) findViewById(R.id.tvNote);
		this.etAmount = (EditText) findViewById(R.id.etAmount);
		this.tvAmount.setText(String.valueOf(template.getAmount()));
		this.etAmount.setText(String.valueOf(template.getAmount()));

		this.etNote = (AutoCompleteTextView) findViewById(R.id.etNote);
		this.etNote.setAdapter(noteAutoCompleaseAdapter);

		this.tvNote.setText(template.getName());
		this.etNote.setText(template.getName());
		// this.imgCalculate = (ImageView) findViewById(R.id.imgCalculate);
		this.imgCategory = (ImageView) findViewById(R.id.imgCategory);
		this.imgAccount = (ImageView) findViewById(R.id.imgAccount);
		this.imgCategory.setBackgroundResource(template
				.getTransactionCategory().getIcon());
		this.imgAccount.setBackgroundResource(template.getAccount()
				.getIcon());
		this.tvCategory = (TextView) findViewById(R.id.tvCategory);
		this.tvCategory.setText(template.getTransactionCategory().getName());
		this.tvAccount = (TextView) findViewById(R.id.tvAccount);
		this.tvAccount.setText(template.getAccount().getName());
		this.tvAccept = (TextView) findViewById(R.id.tvAccept);
		this.tvCancel = (TextView) findViewById(R.id.tvCancel);
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
		case R.id.btOk:
			this.transactionDate = new Date(calendarView.getDate());
			dialogCalendar.dismiss();
			break;
		case R.id.btCancel:
			dialogCalendar.dismiss();
			break;
		case R.id.imgBack:
			if (isUpdateTransaction) {
				isUpdateTransaction = false;

				editMode();
			} else {
				finish();
			}
			break;
		case R.id.categoryLayout:
			showCategoryDialog(this, "");
			break;
		case R.id.accountLayout:
			showAccountDialog(this, "");
			break;
		default:
			break;
		}

	}

	@Override
	protected void setActionBar() {
		try {

			ActionBar actionBar = (ActionBar) getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(
					R.layout.custom_actionbar_detail, null);

			actionBar.setCustomView(mCustomView);
			actionBar.setDisplayShowCustomEnabled(true);

			tvEvent = (TextView) mCustomView.findViewById(R.id.tvEvent);
			imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
			imgBack.setOnClickListener(this);

			imgEdit = (ImageView) mCustomView.findViewById(R.id.imgEdit);
			imgEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					transactionEdit();
				}
			});

			imgDelete = (ImageView) mCustomView.findViewById(R.id.imgDelete);
			imgDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						ConfirmDialog confirmDialog = new ConfirmDialog(
								TemplateDetail.this);
						confirmDialog.show(getResources().getString(
								R.string.confirmDeleteTransaction));
						confirmDialog.callback = new ConfirmDialog.Callback() {

							@Override
							public void callback() {

								TemplateRepository templateRepository = new TemplateRepository(getApplicationContext(),
										App.configuration.getLocale());
								templateRepository.delete(template.getId());
								AccountRepository accountRepository = new AccountRepository(
										getApplicationContext(),
										App.configuration.getLocale());
								accountRepository.deleteTransaction(
										currentAccount.getId(), template);

								Intent returnIntent = new Intent();
								returnIntent.putExtra("result", "Success");
								returnIntent.putExtra("action", getResources()
										.getString(R.string.deleteTransaction));
								// returnIntent.putExtra(App.ACCOUNT_ID,
								// transaction.getAccount().getId());
								setResult(RESULT_OK, returnIntent);

								finish();
							}
						};
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});

			tvEvent.setText(getResources().getString(R.string.templateDetail));

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	protected void transactionEdit() {

		isUpdateTransaction = true;

		editMode();

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

		this.amountMessageLayout = (LinearLayout) findViewById(R.id.amountMessageLayout);
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
							.loadAnimation(getApplicationContext(),
									R.anim.alpha_repeat500));
				}
			}
		});

		if (btIncome.getTag().equals(R.drawable.troggle_on_active_shape))
			createViewDeposit();
		else
			createViewWithdraw();

		tvCategory = (TextView) findViewById(R.id.tvCategory);
		((RelativeLayout) findViewById(R.id.categoryLayout))
				.setOnClickListener(this);

		tvAccount = (TextView) findViewById(R.id.tvAccount);
		((RelativeLayout) findViewById(R.id.accountLayout))
				.setOnClickListener(this);

		tvAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateTransaction();
			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent returnIntent = new Intent();
				// setResult(RESULT_CANCELED, returnIntent);
				// finish();
				isUpdateTransaction = false;
				editMode();
			}
		});

	}

	private void updateTransaction() {

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
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat500));
			} else {
				amountMessageLayout.setVisibility(View.GONE);
				amountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat0));
			}

			if (tvAccount
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectAccount))) {
				accountMessageLayout.setVisibility(View.VISIBLE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat500));
			} else {
				accountMessageLayout.setVisibility(View.GONE);
				accountMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat0));
			}

			if (tvCategory
					.getText()
					.toString()
					.equals(getResources().getString(
							R.string.pleaseSelectTransactionCategory))) {
				transactionCategoryMessageLayout.setVisibility(View.VISIBLE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat500));
			} else {
				transactionCategoryMessageLayout.setVisibility(View.GONE);
				transactionCategoryMessageLayout.startAnimation(AnimationUtils
						.loadAnimation(getApplicationContext(),
								R.anim.alpha_repeat0));

			}
		} else {

			template.setAmount(Double.parseDouble(this.etAmount.getText()
					.toString()));
			template.setName(this.etNote.getText().toString());
			template.setCreateDate(this.transactionDate);

			if (btIncome.getTag().equals(R.drawable.troggle_on_active_shape))
				template.setTransactionType(TransactionType.Income.name());
			else
				template.setTransactionType(TransactionType.Expenses.name());

			template.setAccount(currentAccount);
			template.setTransactionCategory(transactionCategory);

			// TransactionRepository transactionRepository = new
			// TransactionRepository(
			// getApplicationContext());
			// transactionRepository.update(transaction);
			
			TemplateRepository templateRepository = new TemplateRepository(getApplicationContext(),
					App.configuration.getLocale());
			templateRepository.update(template);

			this.etAmount = null;
			this.etNote = null;

			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", "Success");
			returnIntent.putExtra("action",
					getResources().getString(R.string.editTemplate));
			// returnIntent.putExtra(App.ACCOUNT_ID,
			// transaction.getAccount().getId());
			setResult(RESULT_OK, returnIntent);

			finish();
		}
	}

	private void createViewDeposit() {

		tvEvent.setText(getResources().getString(
				R.string.editTransactionDeposit));

		// this.imgCategory.setBackgroundResource(R.drawable.ic_mood_black);
		this.etAmount.setTextColor(Color.rgb(0, 100, 0));
	}

	private void createViewWithdraw() {

		tvEvent.setText(getResources().getString(
				R.string.editTransactionWithdraw));
		// this.imgCategory.setBackgroundResource(R.drawable.ic_mood_bad_black);
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

		calendarView = (CalendarView) dialogCalendar
				.findViewById(R.id.calendarView1);

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
			}
		});

		this.tvCalendarView.setText(monthyearFormat.format(new Date(
				calendarView.getDate())));

		this.tvHeaderDate.setText(dateFormat.format(new Date(calendarView
				.getDate())));
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
		LinearLayout groupLayout;

		final TransactionCategoryRepository transactionCategoryRepository = new TransactionCategoryRepository(
				getApplicationContext(),
				App.configuration.getLocale());

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
					transactionCategoryMessageLayout.setVisibility(View.GONE);
					transactionCategoryMessageLayout
							.startAnimation(AnimationUtils.loadAnimation(
									getApplicationContext(),
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

		((TextView) dialogAccount.findViewById(R.id.tvEvent)).setText("บัญชี");
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
				getApplicationContext(),
				App.configuration.getLocale());

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
					accountMessageLayout.setVisibility(View.GONE);
					accountMessageLayout.startAnimation(AnimationUtils
							.loadAnimation(getApplicationContext(),
									R.anim.alpha_repeat0));

					dialogAccount.dismiss();

				}
			});

			root.addView(groupLayout);
		}

		dialogAccount.show();

	}

	@Override
	public void onBackPressed() {
		if (isUpdateTransaction) {
			isUpdateTransaction = false;

			editMode();

		} else {
			super.onBackPressed();
		}
	}

	private void editMode() {
		if (!isUpdateTransaction) {

			tvEvent.setText(getResources().getString(R.string.editTransaction));
			btIncome.setEnabled(false);
			btExpand.setEnabled(false);

			tvAmount.setVisibility(View.VISIBLE);
			tvNote.setVisibility(View.VISIBLE);
			imgEdit.setVisibility(View.VISIBLE);
			imgDelete.setVisibility(View.VISIBLE);

			tvAccept.setVisibility(View.GONE);
			tvCancel.setVisibility(View.GONE);

			etAmount.setVisibility(View.GONE);
			etNote.setVisibility(View.GONE);

			tvDate.setOnClickListener(null);
			tvTime.setOnClickListener(null);
		} else {
			btIncome.setEnabled(true);
			btExpand.setEnabled(true);

			tvAmount.setVisibility(View.GONE);
			tvNote.setVisibility(View.GONE);
			imgEdit.setVisibility(View.GONE);
			imgDelete.setVisibility(View.GONE);

			tvAccept.setVisibility(View.VISIBLE);
			tvCancel.setVisibility(View.VISIBLE);

			etAmount.setVisibility(View.VISIBLE);
			etNote.setVisibility(View.VISIBLE);

			tvDate.setOnClickListener(this);
			tvTime.setOnClickListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		clearVariables();
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
		dialogCategory = null;
		dialogAccount = null;

		etAmount = null;
		etNote = null;
		imgCategory = null;
		imgAccount = null;
		imgBack = null;
		tvAccept = null;
		tvCancel = null;

		transactionDate = null;
		tvCategory = null;
		tvAccount = null;
		transactionCategory = null;
		categories = null;
	}
}
