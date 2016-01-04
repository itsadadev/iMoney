package com.itsada.imoney.views;

import android.app.Activity;

import com.itsada.imoney.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public abstract class SlideMenu {

	protected SlidingMenu slidingMenu;
	protected Activity activity;

	public SlideMenu(Activity activity, int layoutId) {

		this.activity = activity;
		slidingMenu = new SlidingMenu(activity);

		slidingMenu.setShadowWidth(5);
		slidingMenu.setFadeDegree(0.0f);
		slidingMenu.setMode(SlidingMenu.RIGHT);
		slidingMenu.setBehindWidth(100);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setMenu(layoutId);
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
	}
	
	public abstract void updateMenu();

}
