package cz.robyer.gamework.scenario;

import cz.robyer.gamework.util.Point;

public class PointArea extends Area {
	private Point point;
	private int radius;
	
	public PointArea(String id, Point point, int radius) {
		super(id);
		this.point = point;
		this.radius = radius;
	}

	public boolean isPointInArea(double lat, double lon) {
		// TODO implement it
		return false;
	}

	public Point getPoint() {
		return point;
	}

	public int getRadius() {
		return radius;
	}

}
