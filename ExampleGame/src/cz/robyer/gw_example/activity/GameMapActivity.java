package cz.robyer.gw_example.activity;

import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.area.SoundArea;
import cz.robyer.gamework.utils.GPoint;
import cz.robyer.gw_example.R;

/**
 * Represents game map with showed areas and player position.
 * @author Robert Pösel
 */
public class GameMapActivity extends BaseGameActivity {
	private static final String TAG = GameMapActivity.class.getSimpleName();
	private GoogleMap map;
	private Marker playerMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_map);		
		initButtons();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if (!GameService.isRunning())
			return;
		
		final GameService game = getGame();
		
		if (map == null) {
	        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

	        if (map != null) {	        	
	        	Scenario scenario = null;
	    		if (game != null)
	    			scenario = game.getScenario();

	    		if (scenario != null) {
		        	Map<String, Area> areas = scenario.getAreas();
		        	for (Area a : areas.values()) {
		        		Log.d(TAG, "Draw area " + a.getId());
		        		
		        		if (a instanceof PointArea || a instanceof SoundArea) {
		        			PointArea area = (PointArea)a;
		        			
		        			CircleOptions circle = new CircleOptions(); 
		        			circle.center(toLatLng(area.getPoint()));
		        			circle.radius(area.getRadius());
		        			
		        			circle.strokeWidth(2);
		        			circle.fillColor(Color.argb(40, 255, 0, 0));
		        			
		        			map.addCircle(circle);
		        		} else if (a instanceof MultiPointArea) {
		        			MultiPointArea area = (MultiPointArea)a;
		        			
		        			PolygonOptions polygon = new PolygonOptions();
		        			List<GPoint> points = area.getPoints();
		        			for (GPoint p : points) {
		        				polygon.add(toLatLng(p));	
		        			}
		        			
		        			polygon.strokeWidth(2);
		        			polygon.fillColor(Color.argb(40, 0, 255, 0));
		        			
		        			map.addPolygon(polygon);
		        		}
		        	}
	    		}
	    		
	    		map.setOnMapLongClickListener(new OnMapLongClickListener() {
					@Override
					public void onMapLongClick(LatLng point) {
						Toast.makeText(GameMapActivity.this, "Player location set.", Toast.LENGTH_SHORT).show();
						
						Location location = new Location("custom");
						location.setLatitude(point.latitude);
						location.setLongitude(point.longitude);

						game.onLocationChanged(location);
					}
				});
	        }
	    }
		
		if (game != null)
			updateMarker(game.getLocation());
	}
	
	/**
	 * Checks game events and enabled/disables player location layer.
	 */
	public void receiveEvent(final GameEvent event) {
		super.receiveEvent(event);
		
		switch (event.type) {
		case UPDATED_LOCATION:
			if (event.value instanceof Location) {
				updateMarker((Location)event.value);
			}
		}
	}
	
	private void updateMarker(Location loc) {
		if (map == null || loc == null)
			return;
		
		if (playerMarker == null) {
			MarkerOptions opt = new MarkerOptions()
        		.draggable(false)
        		.visible(true)
        		.title("Player")
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        		.position(new LatLng(loc.getLatitude(), loc.getLongitude()));

        	playerMarker = map.addMarker(opt);

        	// move camera to player position
        	LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
    		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 12);
    		map.moveCamera(update);
		} else						
			playerMarker.setPosition(new LatLng(loc.getLatitude(), loc.getLongitude()));
	}
	
	private LatLng toLatLng(GPoint p) {
		return new LatLng(p.latitude, p.longitude);
	}
		
}
