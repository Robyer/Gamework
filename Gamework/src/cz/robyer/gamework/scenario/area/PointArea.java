package cz.robyer.gamework.scenario.area;

import cz.robyer.gamework.util.GPoint;

/**
 * 
 * @author Robert Pösel
 */
public class PointArea extends Area {
	protected static int LEAVE_RADIUS = 3;
	
	protected GPoint point;
	protected int radius;
	
	public PointArea(String id, GPoint point, int radius) {
		super(id);
		this.point = point;
		this.radius = radius;
	}

	@Override
	protected boolean isPointInArea(double lat, double lon) {
		return GPoint.distanceBetween(point.latitude, point.longitude, lat, lon) < (radius + (inArea ? LEAVE_RADIUS : 0));
	}

	public GPoint getPoint() {
		return point;
	}

	public int getRadius() {
		return radius;
	}
	
}
