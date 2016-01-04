package com.itsada.imoney.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itsada.framework.models.Account;
import com.itsada.framework.models.Transaction;
import com.itsada.framework.repository.SQLite.TransactionRepository;
import com.itsada.imoney.App;
import com.itsada.imoney.R;
import com.itsada.imoney.TransactionFragmentActivity;
import com.itsada.imoney.views.MonthDialog;
import com.itsada.imoney.views.MonthDialog.CallBack;

public class MapFragment extends BaseFragemnt implements OnClickListener {

	private Account currentAccount;
	private ImageView imgBtPreviousMonth, imgBtNextMonth;
	private TextView tvMonthHeader;
	private TransactionRepository transactionRepository;
	private ArrayList<Transaction> transactions;

	private static GoogleMap mMap;
	private Marker mMarker;
	private LatLng coordinate;
	private double lat, lon;
	private View root;
	private int page;
	private String title;

	private static MapFragment mapFragment;

	public static MapFragment newInstance(int page, String title) {

		if (mapFragment == null) {
			mapFragment = new MapFragment();
			Bundle args = new Bundle();
			args.putInt("page", page);
			args.putString("title", title);
			mapFragment.setArguments(args);
		}
		return mapFragment;
	}

	public MapFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("page", 0);
		title = getArguments().getString("title");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;
		
		try {

			rootView = inflater
					.inflate(R.layout.fragment_map, container, false);
			currentAccount = ((TransactionFragmentActivity) getActivity()).currentAccount;
			root = rootView;
			onInit();

		} catch (Exception e) {

		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setUpMapIfNeeded();
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	public void updateMap() {
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {

			FragmentManager myFragmentManager = getFragmentManager();
			// Try to obtain the map from the SupportMapFragment.
			SupportMapFragment supportMapFragment = ((SupportMapFragment) myFragmentManager
					.findFragmentById(R.id.map));
			mMap = supportMapFragment.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {

				mMap.getUiSettings().setZoomControlsEnabled(true);
				// Enabling MyLocation Layer of Google Map
				mMap.setMyLocationEnabled(true);
				drawMap();
			} else {
				drawMap();
			}
		}
	}

	private void drawMap() {

		// if (mMarker != null)
		// mMarker.remove();

		transactionRepository = new TransactionRepository(getActivity()
				.getApplicationContext(), App.configuration.getLocale());

		transactions = transactionRepository.getTransactionByInterval(
				Integer.parseInt(getActivity().getIntent().getStringExtra(
						App.ACCOUNT_ID)), app.from.getTime(), app.to.getTime());

		TransactionGroupFactory();

		if (mMap != null) {

			for (Transaction transaction : transactions) {

				Log.w("Lat lon",
						String.valueOf(transaction.getLatitude() + ":"
								+ String.valueOf(transaction.getLongitude())));

				if (!transaction.getLatitude().equals("0")
						&& !transaction.getLongitude().equals("0")) {

					Log.d("", transaction.getLatitude());

					coordinate = new LatLng(Double.parseDouble(transaction
							.getLatitude()), Double.parseDouble(transaction
							.getLongitude()));

					mMarker = mMap.addMarker(new MarkerOptions()
							.position(coordinate)
							.title(transaction.getTransactionCategory()
									.getName()).snippet(transaction.getDate()));

					BitmapDescriptor image = BitmapDescriptorFactory
							.fromResource(transaction.getIcon());

					GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
					groundOverlayOptions.image(image).position(coordinate, 20);

					mMarker.showInfoWindow();
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							coordinate, 16));
					mMap.addGroundOverlay(groundOverlayOptions);
				}
			}
		}
	}

	@Override
	protected void onInit() {

		imgBtPreviousMonth = (ImageView) root
				.findViewById(R.id.imgBtPreviousMonth);
		imgBtPreviousMonth.setOnClickListener(this);
		imgBtNextMonth = (ImageView) root.findViewById(R.id.imgBtNextMonth);
		imgBtNextMonth.setOnClickListener(this);

		transactionRepository = new TransactionRepository(getActivity()
				.getApplicationContext(), App.configuration.getLocale());

		transactions = transactionRepository.getTransactionByInterval(
				Integer.parseInt(getActivity().getIntent().getStringExtra(
						App.ACCOUNT_ID)), app.from.getTime(), app.to.getTime());

		TransactionGroupFactory();

		// Set month and year to header of Transactions group
		tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);

		tvMonthHeader.setText(monthFormat.format(app.from));

		tvMonthHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Date f = (Date) app.from.clone();
				final Date t = (Date) app.to.clone();
				MonthDialog m = new MonthDialog(getActivity());
				m.show(app.from);

				m.callback = new CallBack() {

					@Override
					public void updateView() {

						if (!(f.equals(app.from) && t.equals(app.to)))
							setUpMapIfNeeded();
					}
				};
			}
		});
	}

	@Override
	public void onClick(View v) {

		Calendar c = Calendar.getInstance();

		switch (v.getId()) {
		// button left month
		case R.id.imgBtPreviousMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, -1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();

			setUpMapIfNeeded();
			break;

		// button right month
		case R.id.imgBtNextMonth:

			c.setTime(app.from);
			c.add(Calendar.MONTH, 1);

			app.from = c.getTime();
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			app.to = c.getTime();
			setUpMapIfNeeded();
			break;
		}
	}

	public void TransactionGroupFactory() {
		if (tvMonthHeader == null)
			tvMonthHeader = (TextView) root.findViewById(R.id.tvMonthHeader);

		tvMonthHeader.setText(monthFormat.format(app.from));
	}

}
