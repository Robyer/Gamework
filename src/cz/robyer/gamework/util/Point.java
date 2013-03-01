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
	
	/**
	 * Slightly modified version from http://www.androidsnippets.com/distance-between-two-gps-coordinates-in-meter
	 * @return double distance in meters between coordinates
	 */
	public double distanceTo(double latitude, double longitude) {
	    double pk = 180 / Math.PI;

	    double a1 = this.latitude / pk;
	    double a2 = this.longitude / pk;
	    double b1 = latitude / pk;
	    double b2 = longitude / pk;

	    double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
	    double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
	    double t3 = Math.sin(a1) * Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    double ellipsoidRadius = 6378.137 * (1 - 0.0033493 * Math.pow(Math.sin(0.5 * (this.latitude + latitude)), 2));
	    
	    return ellipsoidRadius * tt * 1000;
	}
	
}
