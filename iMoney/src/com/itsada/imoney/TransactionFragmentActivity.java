package com.itsada.imoney;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsada.framework.models.Account;
import com.itsada.framework.repository.SQLite.AccountRepository;
import com.itsada.imoney.fragment.GraphFragment;
import com.itsada.imoney.fragment.TransactionFragment;
import com.itsada.management.Format;

@SuppressLint("InflateParams")
public class TransactionFragmentActivity extends FragmentActivity implements
		TabListener, OnClickListener {

	private ViewPager viewPager;
	private TransactionTabPagerAdapter mAdapter;
	private int position = 0;
	private TextView tvTitle;
	private LinearLayout backLayout;

	// Share variable
	public ActionBar actionBar;
	public ImageView imgSort, imgGraph;
	public Account currentAccount;
	public FragmentManager fragmentManager;
	// public SupportMapFragment supportMapFragment;
	// public GoogleMap googleMap;

	// Tab titles
	private String[] tabs = { "Transactions", "Graph", "Map" };
	private int[] tabIcons = { R.drawable.ic_card_travel_black,
			R.drawable.ic_local_taxi_black, R.drawable.ic_map_black };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.transaction_fragment);

		fragmentManager = getSupportFragmentManager();

		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TransactionTabPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);

		AccountRepository accountRepository = new AccountRepository(
				getApplicationContext(), App.configuration.getLocale());
		currentAccount = accountRepository.getById(Integer.parseInt(getIntent()
				.getStringExtra(App.ACCOUNT_ID)));

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(
				R.layout.custom_actionbar_transaction, null);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setIcon(currentAccount.getIcon());

		TextView tvAccountName = (TextView) mCustomView
				.findViewById(R.id.tvAccountName);
		tvTitle = (TextView) mCustomView.findViewById(R.id.tvTitle);
		tvAccountName.setText(currentAccount.getName());
		TextView tvAccountBalance = (TextView) mCustomView
				.findViewById(R.id.tvAccountBalance);
		tvAccountBalance.setText(Format.getInstance(this).getMoneyFormat()
				.format(currentAccount.getBalance()));

		imgSort = (ImageView) mCustomView.findViewById(R.id.imgSort);
		imgSort.setOnClickListener(this);

		imgGraph = (ImageView) mCustomView.findViewById(R.id.imgGraph);
		imgGraph.setTag(R.drawable.ic_equalizer_white_24dp);
		imgGraph.setOnClickListener(this);

		backLayout = (LinearLayout) mCustomView
				.findViewById(R.id.linearLayoutAccount);
		backLayout.setOnClickListener(this);

		// Adding Tabs
		for (int i = 0; i < tabs.length; i++) {
			actionBar.addTab(actionBar.newTab()
			// .setText(tabs[i])
					.setIcon(tabIcons[i]).setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);

				supportInvalidateOptionsMenu();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		position = tab.getPosition();

		View v = actionBar.getCustomView();

		Log.d("position", String.valueOf(tab.getPosition()));

		switch (tab.getPosition()) {
		case 0:
			if (v != null) {
				// TextView tv = (TextView) v.findViewById(R.id.tvAccountName);
				// tvTitle.setText("Transactions");
				imgSort.setVisibility(View.VISIBLE);
				imgGraph.setVisibility(View.GONE);
			}
			break;
		case 1:
			if (v != null) {
				// TextView tv = (TextView) v.findViewById(R.id.tvAccountName);
				// tvTitle.setText("Graph");
				imgSort.setVisibility(View.GONE);
				imgGraph.setVisibility(View.VISIBLE);
			}
			break;
		case 2:
			if (v != null) {
				// TextView tv = (TextView) v.findViewById(R.id.tvAccountName);
				// tvTitle.setText("Map");
				imgSort.setVisibility(View.GONE);
				imgGraph.setVisibility(View.GONE);

				try {
					// com.itsada.imoney.fragment.MapFragment map =
					// (com.itsada.imoney.fragment.MapFragment) mAdapter
					// .getItem(2);
					// map.updateMap();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (actionBar != null) {
				if (actionBar.isShowing()) {
					actionBar.hide();
				} else {
					actionBar.show();
				}
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgSort:
			((TransactionFragment) mAdapter.getItem(position)).sort(imgSort);
			break;

		case R.id.imgGraph:
			((GraphFragment) mAdapter.getItem(position)).switchGraph(imgGraph);
			break;

		case R.id.linearLayoutAccount:
			finish();
			break;
		default:
			break;
		}
	}
}
