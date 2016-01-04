package com.itsada.management;

import java.util.ArrayList;
import java.util.Locale;

import com.itsada.framework.models.TransactionCategory;
import com.itsada.imoney.R;

public class ResourcesManagement {

	public static ArrayList<Integer> getIconAccount() {
		ArrayList<Integer> icons = new ArrayList<Integer>();

		// icons.add(R.drawable.bankok);
		// icons.add(R.drawable.cimb);
		// icons.add(R.drawable.gsb);
		// icons.add(R.drawable.kbank);
		// icons.add(R.drawable.krungsi);
		// icons.add(R.drawable.ktb);
		// icons.add(R.drawable.matercard);
		// icons.add(R.drawable.paypal);
		// icons.add(R.drawable.sc);
		// icons.add(R.drawable.scb);
		// icons.add(R.drawable.thanachat);
		// icons.add(R.drawable.tmb);
		// icons.add(R.drawable.uob);
		// icons.add(R.drawable.visa);
		icons.add(R.drawable.ic_account_balance_black);
		icons.add(R.drawable.ic_account_balance_wallet_black);
		icons.add(R.drawable.ic_account_box_black);
		icons.add(R.drawable.ic_account_circle_black);
		icons.add(R.drawable.ic_card_giftcard_black);
		icons.add(R.drawable.ic_card_membership_black);
		icons.add(R.drawable.ic_card_travel_black);
		icons.add(R.drawable.ic_cloud_black);
		icons.add(R.drawable.ic_cloud_circle_black);
		icons.add(R.drawable.ic_cloud_queue_black);
		icons.add(R.drawable.ic_credit_card_black);
		icons.add(R.drawable.ic_favorite_black);
		icons.add(R.drawable.ic_favorite_border_black);
		icons.add(R.drawable.ic_grade_black);
		icons.add(R.drawable.ic_group_work_black);
		icons.add(R.drawable.ic_https_black);
		icons.add(R.drawable.ic_loyalty_black_24dp);
		icons.add(R.drawable.ic_polymer_black);
		icons.add(R.drawable.ic_shopping_basket_black);
		icons.add(R.drawable.ic_shopping_cart_black);
		icons.add(R.drawable.ic_stars_black);
		icons.add(R.drawable.ic_work_black);

		return icons;
	}

//	public static ArrayList<TransactionCategory> getIncomes() {
//		ArrayList<TransactionCategory> transactionGroups = new ArrayList<TransactionCategory>();
//
//		transactionGroups.add(new TransactionCategory(1, "เงินเดือน",
//				R.drawable.ic_local_atm_black));
//		transactionGroups.add(new TransactionCategory(2, "เบี้ยเลี้ยง",
//				R.drawable.database_sticker));
//		// transactionGroups.add(
//		// R.drawable.database_sticker));
//		transactionGroups.add(new TransactionCategory(4, "ขายสินค้า",
//				R.drawable.database_sticker));
//		transactionGroups.add(new TransactionCategory(5,
//				"เงินสงเคราะห์หรือสวัสดิการ", R.drawable.database_sticker));
//		transactionGroups.add(new TransactionCategory(6, "จากการเสี่ยงโชคค",
//				R.drawable.database_sticker));
//		transactionGroups.add(new TransactionCategory(7, "ดอกเบี้ย",
//				R.drawable.database_sticker));
//
//		return transactionGroups;
//	}
//
//	public static ArrayList<TransactionCategory> getExpenses() {
//
//		ArrayList<TransactionCategory> transactionGroups = new ArrayList<TransactionCategory>();
//
//		transactionGroups.add(new TransactionCategory(1, "ค่าโดยสาร",
//				R.drawable.ic_directions_bus_black));
//		transactionGroups.add(new TransactionCategory(2, "ค่าตั๋วเครื่องบิน",
//				R.drawable.ic_flight_black));
//		transactionGroups.add(new TransactionCategory(3, "ค่าน้ำมันเชื้อเพลิง",
//				R.drawable.ic_local_gas_station_black));
//		transactionGroups.add(new TransactionCategory(4, "ค่าอาหาร",
//				R.drawable.ic_local_dining_black));
//		transactionGroups.add(new TransactionCategory(5, "ค่าเครื่องดื่ม",
//				R.drawable.ic_local_bar_black));
//		transactionGroups.add(new TransactionCategory(6, "ค่ากาแฟ",
//				R.drawable.ic_local_cafe_black));
//		transactionGroups.add(new TransactionCategory(7, "ค่าล้างรถ",
//				R.drawable.ic_local_car_wash_black));
//		transactionGroups.add(new TransactionCategory(8, "ค่ากาแฟ",
//				R.drawable.ic_local_cafe_black));
//		transactionGroups.add(new TransactionCategory(9, "ค่าต้นไม้",
//				R.drawable.ic_local_florist_black));
//		transactionGroups.add(new TransactionCategory(10, "ค่าซอปปิ้ง",
//				R.drawable.ic_local_grocery_store_black));
//		transactionGroups.add(new TransactionCategory(11, "ค่าหมอ",
//				R.drawable.ic_local_hospital_black));
//		transactionGroups.add(new TransactionCategory(12, "ค่าหนังสือ",
//				R.drawable.ic_local_library_black));
//		transactionGroups.add(new TransactionCategory(13, "ค่าจอดรถ",
//				R.drawable.ic_local_parking_black));
//		transactionGroups.add(new TransactionCategory(14, "ค่าภาพยนต์",
//				R.drawable.ic_local_movies_black));
//		transactionGroups.add(new TransactionCategory(15, "ค่าโทรศัพท์",
//				R.drawable.ic_local_phone_black));
//		transactionGroups.add(new TransactionCategory(16, "ค่าแท็กซี่",
//				R.drawable.ic_local_taxi_black));
//
//		return transactionGroups;
//	}

	public static ArrayList<Integer> getIcons() {
		ArrayList<Integer> icons = new ArrayList<Integer>();

		icons.add(R.drawable.ic_account_balance_black);
		icons.add(R.drawable.ic_account_balance_wallet_black);
		icons.add(R.drawable.ic_account_box_black);
		icons.add(R.drawable.ic_account_circle_black);
		icons.add(R.drawable.ic_card_giftcard_black);
		icons.add(R.drawable.ic_card_membership_black);
		icons.add(R.drawable.ic_card_travel_black);
		icons.add(R.drawable.ic_cloud_black);
		icons.add(R.drawable.ic_cloud_circle_black);
		icons.add(R.drawable.ic_cloud_queue_black);
		icons.add(R.drawable.ic_credit_card_black);
		icons.add(R.drawable.ic_favorite_black);
		icons.add(R.drawable.ic_favorite_border_black);
		icons.add(R.drawable.ic_grade_black);
		icons.add(R.drawable.ic_group_work_black);
		icons.add(R.drawable.ic_https_black);
		icons.add(R.drawable.ic_loyalty_black_24dp);
		icons.add(R.drawable.ic_polymer_black);
		icons.add(R.drawable.ic_shopping_basket_black);
		icons.add(R.drawable.ic_shopping_cart_black);
		icons.add(R.drawable.ic_stars_black);
		icons.add(R.drawable.ic_work_black);

		icons.add(R.drawable.ic_local_atm_black);
		icons.add(R.drawable.ic_directions_bus_black);
		icons.add(R.drawable.ic_flight_black);
		icons.add(R.drawable.ic_local_gas_station_black);
		icons.add(R.drawable.ic_local_dining_black);
		icons.add(R.drawable.ic_local_bar_black);
		icons.add(R.drawable.ic_local_cafe_black);
		icons.add(R.drawable.ic_local_car_wash_black);
		icons.add(R.drawable.ic_local_cafe_black);
		icons.add(R.drawable.ic_local_florist_black);
		icons.add(R.drawable.ic_local_grocery_store_black);
		icons.add(R.drawable.ic_local_hospital_black);
		icons.add(R.drawable.ic_local_library_black);
		icons.add(R.drawable.ic_local_parking_black);
		icons.add(R.drawable.ic_local_movies_black);
		icons.add(R.drawable.ic_local_phone_black);
		icons.add(R.drawable.ic_local_taxi_black);

		return icons;
	}

	public static ArrayList<String> displayBy() {
		ArrayList<String> menus = new ArrayList<String>();
		menus.add("วัน");
		menus.add("เดือน");
		menus.add("ปี");

		return menus;
	}

	public static ArrayList<String> GroupBy(Locale locale) {
		ArrayList<String> menus = new ArrayList<String>();

		if (locale.equals(Locale.ENGLISH)) {
			menus.add("Group by date");
			menus.add("Group by category");
			menus.add("Group by income/expend");
		} else {
			// menus.add("จัดกลุ่ม");
			menus.add("จัดกลุ่มตามวัน");
			menus.add("จัดกลุ่มตามประเภท");
			menus.add("จัดกลุ่มตามรับ/จ่าย");
		}
		return menus;
	}

}
