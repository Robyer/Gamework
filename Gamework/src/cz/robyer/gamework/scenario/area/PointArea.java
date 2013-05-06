package cz.robyer.gamework.scenario.area;

import cz.robyer.gamework.utils.GPoint;

/**
 * This represents circle game area based on center point and radius.
 * @author Robert Pösel
 */
public class PointArea extends Area {
	protected static int LEAVE_RADIUS = 3;
	
	protected GPoint point; // center
	protected int radius; // radius in meters
	
	public PointArea(String id, GPoint point, int radius) {
		super(id);
		this.point = point;
		this.radius = radius;
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.area.Area#isPointInArea(double, double)
	 */
	@Override
	protected boolean isPointInArea(double lat, double lon) {
		return GPoint.distanceBetween(point.latitude, point.longitude, lat, lon) < (radius + (inArea ? LEAVE_RADIUS : 0));
	}

	/**
	 * Returns center point of this area.
	 * @return center point
	 */
	public GPoint getPoint() {
		return point;
	}

	/**
	 * Returns radius of this area.
	 * @return radius in meters
	 */
	public int getRadius() {
		return radius;
	}
	
}
