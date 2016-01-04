//package com.itsada.imoney.views;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.itsada.framework.models.Transaction;
//import com.itsada.imoney.BaseActivity;
//import com.itsada.imoney.R;
//
//@SuppressLint("SimpleDateFormat")
//public class TransactionGroupAdepter extends BaseAdapter {
//
//	private Context mContext;
//	private Activity mActivity;
//	private final Set<String> groups;
//	private ArrayList<Transaction> mTransactions;
//
//	public TransactionGroupAdepter(Activity activity,
//			ArrayList<Transaction> transactions) {
//		this.mContext = activity.getApplicationContext();
//		this.mActivity = activity;
//		this.mTransactions = transactions;
//		groups = new HashSet<String>();
//		
//		if (transactions.size() > 0) {
//			for (Transaction tt : transactions) {
//				groups.add(BaseActivity.dateFormat.format(tt.getCreateDate()));
//			}
//		}else{
//			//set empty
//			groups.add("");
//		}
//	}
//
//	@Override
//	public int getCount() {
//		return groups.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		String s = "";
//		s = (String) groups.toArray()[position];
//		return s;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		Log.d("tG", "getVIEW");
//		final ViewHolder viewHolder;
//		String g = (String) groups.toArray()[position];
//
//		if (convertView == null) {
//			LayoutInflater inflater = (LayoutInflater) mContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//			viewHolder = new ViewHolder();
//
//			convertView = inflater.inflate(R.layout.transaction_list_template,
//					parent, false);
//
//			viewHolder.v = convertView;
//			viewHolder.transactionDate = (TextView) convertView
//					.findViewById(R.id.tvDate);
//			viewHolder.transactionDateBalance = (TextView) convertView
//					.findViewById(R.id.tvDateBalance);
//
//			viewHolder.imgCollapes = (ImageView) convertView
//					.findViewById(R.id.imgCollapes);
//			viewHolder.imgExpand = (ImageView) convertView
//					.findViewById(R.id.imgExpand);
//			viewHolder.transactionLayout = (LinearLayout) viewHolder.v
//					.findViewById(R.id.transactionLayout);
//
////			viewHolder.child = (ListView) convertView
////					.findViewById(R.id.transactionListView);
//
//			convertView.setTag(viewHolder);
//
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//
//		viewHolder.transactionLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				LinearLayout parent = (LinearLayout) v.getParent();
////				ListView child = (ListView) parent
////						.findViewById(R.id.transactionListView);
//
//				if (child.getVisibility() == View.VISIBLE) {
//
//					child.setVisibility(View.GONE);
//					child.startAnimation(AnimationUtils.loadAnimation(mContext,
//							R.anim.push_up_in));
//
//					parent.findViewById(R.id.imgExpand).setVisibility(
//							View.VISIBLE);
//					parent.findViewById(R.id.imgCollapes).setVisibility(
//							View.GONE);
//				} else {
//					child.setVisibility(View.VISIBLE);
//					child.startAnimation(AnimationUtils.loadAnimation(mContext,
//							R.anim.push_up_in));
//					parent.findViewById(R.id.imgExpand)
//							.setVisibility(View.GONE);
//					parent.findViewById(R.id.imgCollapes).setVisibility(
//							View.VISIBLE);
//				}
//			}
//		});
//
//		viewHolder.transactionDate.setText(g);
//
//		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
//		for (Transaction transaction : mTransactions) {
//			if (g.equals(BaseActivity.dateFormat.format(transaction
//					.getCreateDate()))) {
//				transactions.add(transaction);
//			}
//		}
//
//		viewHolder.child.setAdapter(new TransactionAdepter(mActivity,
//				transactions));
//
//		return convertView;
//	}
//
//	static class ViewHolder {
//
//		private TextView transactionDate;
//		private TextView transactionDateBalance;
//
//		private ImageView imgCollapes;
//		private ImageView imgExpand;
//
//		private LinearLayout transactionLayout;
//		private View v;
//		private ListView child;
//	}
//
//}
