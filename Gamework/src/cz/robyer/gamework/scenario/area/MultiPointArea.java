package cz.robyer.gamework.scenario.area;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import cz.robyer.gamework.util.GPoint;

/**
 * 
 * @author Robert Pösel
 */
public class MultiPointArea extends Area {
	protected List<GPoint> points;
	protected double minLat;
	protected double maxLat;
	protected double minLon;
	protected double maxLon;

	public MultiPointArea(String id) {
		super(id);
	}

	public void addPoint(GPoint point) {
		if (point == null)
			return;
		
		if (points == null)
			points = new ArrayList<GPoint>();
			
		points.add(point);
		calcBoundaries(point.latitude, point.longitude);
	}
	
	public List<GPoint> getPoints() {
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
		if (points.size() < 3) {
			Log.e(TAG, "MultiPointArea must contain at least 3 points");
			return false;
		}
		
		// little optimalization - check min-max rectangle first  
		if (lat < minLat || lat > maxLat || lon < minLon || lon > maxLon)
			return false;
		
		boolean c = false;
		// algorithm from http://www.faqs.org/faqs/graphics/algorithms-faq/
		for (int i = 0, j = points.size()-1; i < points.size(); j = i++) {
			if ((((points.get(i).latitude <= lat) && (lat < points.get(j).latitude)) ||
				((points.get(j).latitude <= lat) && (lat < points.get(i).latitude))) &&
				(lon < (points.get(j).longitude - points.get(i).longitude) * (lat - points.get(i).latitude) / (points.get(j).latitude - points.get(i).latitude) + points.get(i).longitude))
				c = !c;
		}
		
		// algorithm from http://en.wikipedia.org/wiki/Even-odd_rule
		/*for (int i = 0, j = points.size()-1; i < points.size(); j = i++) {
			if (((points.get(i).latitude > lat) != (points.get(j).latitude > lat)) &&
				(lon < (points.get(j).longitude - points.get(i).longitude) *
				(lat - points.get(i).latitude) / (points.get(j).latitude - points.get(i).latitude) + points.get(i).longitude))	
				c = !c;
		}*/
		
		return c;
	}

}
