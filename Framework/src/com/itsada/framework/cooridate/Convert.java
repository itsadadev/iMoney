package com.itsada.framework.cooridate;

public class Convert {

	// public double latitude = 13.760496;
	// public double longitude = 100.644370;
	 public double latitude;
	 public double longitude;

	public double easting;// lat
	public double northing;// lon

	 public double lat;

	// ----------------------------------------------------------------------------
	// Datum Constants Variables
	// ----------------------------------------------------------------------------
	public final double k0 = 0.9996;
	public double a;
	public double b;
	public double f;
	public double inversef;
	public double rm;
	public double e;
	public double e1sq;
	public double n;
	public double rho;
	public double nu;

	// ----------------------------------------------------------------------------
	// Meridional Arc Length Variables
	// ----------------------------------------------------------------------------
	public double S;
	public double A0;
	public double B0;
	public double C0;
	public double D0;
	public double E0;

	// ----------------------------------------------------------------------------
	// Calculation Constants Variables
	// ----------------------------------------------------------------------------
	public double zone;
	public double zoneCM;
	public double deltaLong;
	public double p;

	// ----------------------------------------------------------------------------
	// Coefficients for UTM Coordinates Variables
	// ----------------------------------------------------------------------------
	public double K1;
	public double K2;
	public double K3;
	public double K4;
	public double K5;
	public double A6;
	public double sin1 = Math.PI / (180 * 3600);

	public Convert() {
		// TODO Auto-generated constructor stub
	}

	public Convert(double latitude, double logitude) {
		 this.latitude = latitude;
		 this.longitude = logitude;
		 this.lat = degreeToRadian(latitude);

		datumConstants();
		calculateMeridionalArcLength();
		calculationConstants();
		coefficientsForUTMCoordinates();
		calculateNorthingEasting();

		String zone = getZone();
	}

	public String getZone() {
		return longitudeZone() + latitudeZone();
	}

	public double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}

	private void datumConstants() {		

		// Hard code datum is WGS84
		a = 6378137.0;
		b = 6356752.3142;
		f = (a - b) / a;
		inversef = 1 / f;
		rm = Math.sqrt(a * b);
		e = Math.sqrt((1 - Math.pow(b / a, 2)));
		e1sq = e * e / (1 - e * e);
		n = (a - b) / (a + b);

		// a*(1-e*e)/((1-(e*SIN(lat))^2)^(3/2))
		rho = a * (1 - e * e) / (Math.pow(1 - Math.pow(e * Math.sin(lat), 2), 3 / 2.0));
		nu = a / Math.pow(1 - Math.pow(e * Math.sin(lat), 2), (1 / 2.0));

	}

	private void calculateMeridionalArcLength() {		

		A0 = a * (1 - n + (5 * n * n / 4) * (1 - n) + (81 * Math.pow(n, 4) / 64) * (1 - n));
		B0 = ((3 * a * n) / 2) * (1 - n - ((7 * Math.pow(n, 2) * (1 - n)) / 8) + ((55 * Math.pow(n, 4)) / 64));
		C0 = (15 * a * n * n / 16) * (1 - n + (3 * n * n / 4) * (1 - n));
		D0 = (35 * a * Math.pow(n, 3) / 48) * (1 - n + 11 * n * n / 16);
		E0 = (315 * a * Math.pow(n, 4) / 51) * (1 - n);

		S = A0 * lat - B0 * Math.sin(2 * lat) + C0 * Math.sin(4 * lat) - D0 * Math.sin(6 * lat) + E0 * Math.sin(8 * lat);

	}

	private void calculationConstants() {

		zone = ((int) ((180 + longitude) / 6.0)) + 1;
		zoneCM = (6 * zone) - 183;
		deltaLong = longitude - zoneCM;
		p = (deltaLong) * Math.PI / 180;
	}

	private void coefficientsForUTMCoordinates() {

		double lat = degreeToRadian(latitude);
		// double sin1 = 4.84814E-06;

		K1 = S * k0;
		K2 = nu * Math.sin(lat) * Math.cos(lat) * k0 / 2;
		K3 = ((nu * Math.sin(lat) * Math.pow(Math.cos(lat), 3)) / 24)
				* (5 - Math.pow(Math.tan(lat), 2) + 9 * e1sq * Math.pow(Math.cos(lat), 2) + 4 * Math.pow(e1sq, 2) * Math.pow(Math.cos(lat), 4)) * k0;
		K4 = nu * Math.cos(lat) * k0;
		K5 = (Math.pow(Math.cos(lat), 3)) * (nu / 6) * (1 - Math.pow(Math.tan(lat), 2) + e1sq * Math.pow(Math.cos(lat), 2)) * k0;

		A6 = (Math.pow(p * sin1, 6) * nu * Math.sin(lat) * Math.pow(Math.cos(lat), 5) / 720)
				* (61 - 58 * Math.pow(Math.tan(lat), 2) + Math.pow(Math.tan(lat), 4) + 270 * e1sq * Math.pow(Math.cos(lat), 2) - 330 * e1sq
						* Math.pow(Math.sin(lat), 2)) * k0 * (1E+24);
	}

	private void calculateNorthingEasting() {
		northing = latitude < 0.0 ? 10000000 + (K1 + K2 * p * p + K3 * Math.pow(p, 4)) : (K1 + K2 * p * p + K3 * Math.pow(p, 4));
		easting = 500000 + (K4 * p + K5 * Math.pow(p, 3));
	}

	private char[] letters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z' };

	private int[] degrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24, -16, -8, 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 84 };

	private char[] negLetters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M' };

	private int[] negDegrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24, -16, -8 };

	private char[] posLetters = { 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z' };

	private int[] posDegrees = { 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 84 };

	private int arrayLength = 22;

	private String latitudeZone() {
		int latIndex = -2;
		int lat = (int) latitude;

		if (lat >= 0) {
			int len = posLetters.length;
			for (int i = 0; i < len; i++) {
				if (lat == posDegrees[i]) {
					latIndex = i;
					break;
				}

				if (lat > posDegrees[i]) {
					continue;
				} else {
					latIndex = i - 1;
					break;
				}
			}
		} else {
			int len = negLetters.length;
			for (int i = 0; i < len; i++) {
				if (lat == negDegrees[i]) {
					latIndex = i;
					break;
				}

				if (lat < negDegrees[i]) {
					latIndex = i - 1;
					break;
				} else {
					continue;
				}

			}

		}

		if (latIndex == -1) {
			latIndex = 0;
		}
		if (lat >= 0) {
			if (latIndex == -2) {
				latIndex = posLetters.length - 1;
			}
			return String.valueOf(posLetters[latIndex]);
		} else {
			if (latIndex == -2) {
				latIndex = negLetters.length - 1;
			}
			return String.valueOf(negLetters[latIndex]);

		}
	}

	private String longitudeZone() {
		double longZone = 0;
		if (longitude < 0.0) {
			longZone = ((180.0 + longitude) / 6) + 1;
		} else {
			longZone = (longitude / 6) + 31;
		}
		String val = String.valueOf((int) longZone);
		if (val.length() == 1) {
			val = "0" + val;
		}

		return val;
	}
}
