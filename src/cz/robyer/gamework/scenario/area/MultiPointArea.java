package cz.robyer.gamework.scenario.area;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.util.Point;

public class MultiPointArea extends Area {
	protected List<Point> points;

	public MultiPointArea(String id) {
		super(id);
	}

	public void addPoint(Point point) {
		if (point != null) {
			if (points == null)
				points = new ArrayList<Point>();
			
			points.add(point);
		}
	}
	
	@Override
	public boolean isPointInArea(double lat, double lon) {
		// TODO implement it
		return false;
	}
	
}
