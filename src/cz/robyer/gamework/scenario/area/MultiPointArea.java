package cz.robyer.gamework.scenario.area;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class MultiPointArea extends Area {
	protected List<LatLng> points;
	protected double minLat;
	protected double maxLat;
	protected double minLon;
	protected double maxLon;

	public MultiPointArea(String id) {
		super(id);
	}

	public void addPoint(LatLng point) {
		if (point == null)
			return;
		
		if (points == null)
			points = new ArrayList<LatLng>();
			
		points.add(point);
		calcBoundaries(point.latitude, point.longitude);
	}
	
	public List<LatLng> getPoints() {
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
