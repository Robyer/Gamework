package cz.robyer.gamework.scenario.area;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.util.Point;

public class MultiPointArea extends Area {
	private List<Point> points;

	public MultiPointArea(String id) {
		super(id);
		this.points = new ArrayList<Point>();
	}
	
	public MultiPointArea(String id, List<Point> points) {
		super(id);
		this.points = points;
	}

	@Override
	public boolean isPointInArea(double lat, double lon) {
		// TODO implement it
		return false;
	}

	public void addPoint(Point point) {
		if (point != null)
			this.points.add(point);
	}
	
}
