package cz.robyer.gamework.scenario.area;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.util.Point;

public class MultiPointArea extends Area {
	protected List<Point> points;
	protected double minLat;
	protected double maxLat;
	protected double minLon;
	protected double maxLon;

	public MultiPointArea(String id) {
		super(id);
	}

	public void addPoint(Point point) {
		if (point == null)
			return;
		
		if (points == null)
			points = new ArrayList<Point>();
			
		points.add(point);
		calcBoundaries(point.getLatitude(), point.getLongitude());
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	private void calcBoundaries(double lat, double lon) {
		if (minLat > lat) minLat = lat;		
		if (maxLat < lat) maxLat = lat;		
		if (minLon > lon) minLon = lon;		
		if (maxLon < lon) maxLon = lon;
	}
	
	@Override
	protected boolean isPointInArea(double lat, double lon) {
		// little optimalization - check min-max rectangle first  
		if (lat < minLat || lat > maxLat || lon < minLon || lon > maxLon)
			return false;
		
		boolean c = false;
		// algorithm from http://www.faqs.org/faqs/graphics/algorithms-faq/
		for (int i = 0, j = points.size()-1; i < points.size(); j = i++) {
			if ((((points.get(i).getLatitude() <= lat) && (lat < points.get(j).getLatitude())) ||
				((points.get(j).getLatitude() <= lat) && (lat < points.get(i).getLatitude()))) &&
				(lon < (points.get(j).getLongitude() - points.get(i).getLongitude()) * (lat - points.get(i).getLatitude()) / (points.get(j).getLatitude() - points.get(i).getLatitude()) + points.get(i).getLongitude()))		
				c = !c;
		}
		
		// algorithm from http://en.wikipedia.org/wiki/Even-odd_rule
		/*for (int i = 0, j = points.size()-1; i < points.size(); j = i++) {
			if (((points.get(i).getLatitude() > lat) != (points.get(j).getLatitude() > lat)) &&
				(lon < (points.get(j).getLongitude() - points.get(i).getLongitude()) *
				(lat - points.get(i).getLatitude()) / (points.get(j).getLatitude() - points.get(i).getLatitude()) + points.get(i).getLongitude()))	
				c = !c;
		}*/
		
		return c;
	}

}
