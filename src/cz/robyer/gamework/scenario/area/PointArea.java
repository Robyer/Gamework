package cz.robyer.gamework.scenario.area;

import cz.robyer.gamework.util.Point;

public class PointArea extends Area {
	protected static int LEAVE_RADIUS = 3;
	
	protected Point point;
	protected int radius;
	
	public PointArea(String id, Point point, int radius) {
		super(id);
		this.point = point;
		this.radius = radius;
	}

	@Override
	protected boolean isPointInArea(double lat, double lon) {
		return point.distanceTo(lat, lon) < (radius + (inArea ? LEAVE_RADIUS : 0));
	}

	public Point getPoint() {
		return point;
	}

	public int getRadius() {
		return radius;
	}
	
}
