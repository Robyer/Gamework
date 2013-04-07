package cz.robyer.gamework.util;

/**
 * 
 * @author Robert Pösel
 */
public class Point {

	/**
	 * Slightly modified version of code from http://www.androidsnippets.com/distance-between-two-gps-coordinates-in-meter
	 * @return Distance in meters between given coordinates
	 */
	public static double distanceBetween(double latFrom, double lonFrom, double latTo, double lonTo) {
	    double pk = 180 / Math.PI;

	    double a1 = latFrom / pk;
	    double a2 = lonFrom / pk;
	    double b1 = latTo / pk;
	    double b2 = lonTo / pk;

	    double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
	    double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
	    double t3 = Math.sin(a1) * Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    double ellipsoidRadius = 6378.137 * (1 - 0.0033493 * Math.pow(Math.sin(0.5 * (latFrom + latTo)), 2));
	    
	    return ellipsoidRadius * tt * 1000;
	}

}
