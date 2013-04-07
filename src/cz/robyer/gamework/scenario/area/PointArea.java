package cz.robyer.gamework.scenario.area;

import com.google.android.gms.maps.model.LatLng;

import cz.robyer.gamework.util.Point;

/**
 * 
 * @author Robert Pösel
 */
public class PointArea extends Area {
	protected static int LEAVE_RADIUS = 3;
	
	protected LatLng point;
	protected int radius;
	
	public PointArea(String id, LatLng point, int radius) {
		super(id);
		this.point = point;
		this.radius = radius;
	}

	@Override
	protected boolean isPointInArea(double lat, double lon) {
		return Point.distanceBetween(point.latitude, point.longitude, lat, lon) < (radius + (inArea ? LEAVE_RADIUS : 0));
	}

	public LatLng getPoint() {
		return point;
	}

	public int getRadius() {
		return radius;
	}
	
}
