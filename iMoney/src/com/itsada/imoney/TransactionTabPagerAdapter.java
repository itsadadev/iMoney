package com.itsada.imoney;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itsada.imoney.fragment.GraphFragment;
import com.itsada.imoney.fragment.MapFragment;
import com.itsada.imoney.fragment.TransactionFragment;

public class TransactionTabPagerAdapter extends FragmentPagerAdapter {

	public TransactionTabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return TransactionFragment.newInstance(0, "T");
		case 1:
			return GraphFragment.newInstance(1, "G");
		case 2:
			return MapFragment.newInstance(2, "M");
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
