package com.itsada.imoney;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class MenuAdaperter extends ArrayAdapter {

	@SuppressWarnings("unchecked")
	public MenuAdaperter(Context context, int resource, ArrayList<String> objects) {
		super(context, resource, objects);
	}

	public TextView getView(int position
			, View convertView, ViewGroup parent) {

		TextView v = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.spinner, parent, false);
		}

		v = (TextView) super.getView(position, convertView, parent);
		v.setTextColor(Color.WHITE);
		v.setText("\u00A0" + v.getText().toString());

		return v;
	}

	public TextView getDropDownView(int position, View convertView,
			ViewGroup parent) {

		TextView v = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.spinner, parent, false);
		}

		v = (TextView) super.getView(position, convertView, parent);
		v.setText("\u00A0" + v.getText().toString());

		return v;
	}

}
