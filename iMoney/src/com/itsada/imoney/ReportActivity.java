package com.itsada.imoney;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import com.itsada.framework.models.Transaction;
import com.itsada.framework.repository.SQLite.TransactionRepository;

import android.app.Activity;
import android.os.Bundle;

public class ReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.report_layout);
		
		TransactionRepository transactionRepository = new TransactionRepository(getApplicationContext(),
				App.configuration.getLocale());
		
		PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
		
		App app = (App)getApplication();
		
		for (Transaction t : transactionRepository.getTransactionByInterval(1,app.from.getTime(), app.to.getTime())) {
			mPieChart.addPieSlice(new PieModel(t.getTransactionCategory().getName(), (float)t.getAmount(), t.getTransactionCategory().getColor()));
		}
	}

}
