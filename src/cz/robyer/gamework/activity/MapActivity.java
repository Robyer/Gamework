package cz.robyer.gamework.activity;

import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import cz.robyer.gamework.R;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.area.SoundArea;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Show the Up button in the action bar.
		setupActionBar();
		
		if (map == null) {
	        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (map != null) {
	        	//map.animateCamera(CameraUpdateFactory.zoomIn());
	        	 
	            // The Map is verified. It is now safe to manipulate the map.
	        	//map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        	
	        	Map<String, Area> areas = TestingActivity.scenario.getAreas();
	        	for (Area a : areas.values()) {
	        		Log.i("MapActivity", "Draw area " + a.getId());
	        		
	        		if (a instanceof PointArea || a instanceof SoundArea) {
	        			PointArea area = (PointArea)a;
	        			
	        			CircleOptions circle = new CircleOptions(); 
	        			circle.center(area.getPoint());
	        			circle.radius(area.getRadius());
	        			
	        			circle.strokeWidth(2);
	        			circle.fillColor(Color.argb(40, 255, 0, 0));
	        			
	        			map.addCircle(circle);
	        		} else if (a instanceof MultiPointArea) {
	        			MultiPointArea area = (MultiPointArea)a;
	        			
	        			PolygonOptions polygon = new PolygonOptions();
	        			List<LatLng> points = area.getPoints();
	        			for (LatLng p : points) {
	        				polygon.add(p);	
	        			}
	        			
	        			polygon.strokeWidth(2);
	        			polygon.fillColor(Color.argb(40, 0, 255, 0));
	        			
	        			map.addPolygon(polygon);
	        		}
	        	}
	        	
	        	
	        	/*for (Map.Entry<String,Thing> entry : map.entrySet()) {
	        	    String key = entry.getKey();
	        	    Thing thing = entry.getValue();
	        	    ...
	        	}*/
	        }
	    }
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
