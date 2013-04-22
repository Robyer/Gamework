package cz.robyer.gw_example.activity;

import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.game.GameStatus;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.area.MultiPointArea;
import cz.robyer.gamework.scenario.area.PointArea;
import cz.robyer.gamework.scenario.area.SoundArea;
import cz.robyer.gamework.util.GPoint;
import cz.robyer.gw_example.R;

/**
 * Represents game map with showed areas and player position.
 * @author Robert Pösel
 */
public class GameMapActivity extends BaseGameActivity {
	private static final String TAG = GameMapActivity.class.getSimpleName();
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_map);		
		initButtons();
		
		if (!GameService.isRunning())
			return;
		
		if (map == null) {
	        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (map != null) {
	        	//map.animateCamera(CameraUpdateFactory.zoomIn());
	        	 
	            // The Map is verified. It is now safe to manipulate the map.
	        	//map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        	
	        	GameService game = getGame();	        	
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
		        	/*for (Map.Entry<String,Thing> entry : map.entrySet()) {
	        	    	String key = entry.getKey();
	        	    	Thing thing = entry.getValue();
	        	    	...
	        		}*/
		        	if (game.getStatus() == GameStatus.GAME_RUNNING)
		        		map.setMyLocationEnabled(true);

		        	Location loc = game.getLocation();
		        	if (loc != null) {
		        		LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
		        		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 12);
		        		map.moveCamera(update);
		        	}
	    		}
	        }
	    }
	}
	
	/**
	 * Checks game events and enabled/disables player location layer.
	 */
	public void receiveEvent(GameEvent event) {
		super.receiveEvent(event);
		
		switch (event.type) {
		case GAME_START:
			if (map != null)
        		map.setMyLocationEnabled(true);
			break;
		case GAME_LOSE:
		case GAME_PAUSE:
		case GAME_QUIT:
		case GAME_WIN:
			if (map != null)
				map.setMyLocationEnabled(false);
			break;
		}
	}
	
	private LatLng toLatLng(GPoint p) {
		return new LatLng(p.latitude, p.longitude);
	}
		
}
