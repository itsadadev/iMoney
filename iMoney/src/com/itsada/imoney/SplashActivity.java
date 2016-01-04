package com.itsada.imoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_layout);

		Handler x = new Handler();
		x.postDelayed(new SplashHandler(), 2000);
	}

	class SplashHandler implements Runnable {
		public void run() {

			if (App.configuration == null) {
				startActivity(new Intent(getApplicationContext(),
						InitialActivity.class));
				SplashActivity.this.finish();
			} else {
				
//				startActivity(new Intent(getApplicationContext(),
//						Transaction.class));
						
				startActivity(new Intent(getApplicationContext(),
						AccountListActivity.class));
				
//				startActivity(new Intent(getApplicationContext(),
//						TransactionFragmentActivity.class));

				// startActivity(new Intent(getApplicationContext(),
				// MainActivity.class));
				SplashActivity.this.finish();
			}
		}
	}

}
