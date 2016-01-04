package com.itsada.framework.cooridate;

import java.text.DecimalFormat;

public class ConvertCoordinate {

	DecimalFormat formatOne;
	DecimalFormat formatTwo;
	DecimalFormat formatThree;

	public ConvertCoordinate() {

		formatOne = new DecimalFormat("###0");
		formatTwo = new DecimalFormat("###0.00");
		formatThree = new DecimalFormat("####.#######");
	}

	/*
	 * Formula Degree = integer part of decimal value. (e.g. if the value is
	 * 45.3470 then Degree will be 45.) Multiply fraction part of value by 60
	 * and use the integer part of result for minutes. (e.g. if the value is
	 * 45.3470 then 0.3470X60 =20.820 so minutes will be 20) Multiply fraction
	 * part from the step no 2 by 60 and use the result for seconds. (e.g. if
	 * the value from step 2 is 20.820 then 0.820X60 =49.2 so seconds will be 49
	 * or 49.2)
	 * 
	 * @Test 45.3470 will be 40° 20´ 49´´
	 */
	public String convertDecimalToDegree(double decimal) {

		StringBuilder builder = new StringBuilder();
		double d, m, s;

		d = (int) decimal;
		m = (int) ((decimal - d) * 60);
		s = (((decimal - d) * 60) - m) * 60;

		builder.append(this.formatOne.format(d));
		builder.append("° ");
		builder.append(this.formatOne.format(m));
		builder.append("´ ");
		builder.append(this.formatTwo.format(s));
		builder.append("´´");

		return builder.toString();
	}

	/*
	 * Degree+(Minutes/60)+(Seconds/3600)
	 */
	public String convertDegreeToDecimal(double degree, double minutes,
			double second) {

		StringBuilder builder = new StringBuilder();

		double decimal = degree + (minutes / 60) + (second / 3600);
		builder.append(formatThree.format(decimal));
		
		return builder.toString();
	}

}
