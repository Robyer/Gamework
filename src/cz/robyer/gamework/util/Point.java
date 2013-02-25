package cz.robyer.gamework.util;

public class Point {
	private double latitude;
	private double longitude;
	
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	
}
