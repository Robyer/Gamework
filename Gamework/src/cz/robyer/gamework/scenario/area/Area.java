package cz.robyer.gamework.scenario.area;

import android.util.Log;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.hook.Hook;

/**
 * This is basic abstract object for game areas.
 * @author Robert Pösel
 */
public abstract class Area extends HookableObject {
	protected boolean inArea = false;
	
	/**
	 * Class constructor
	 * @param id Identificator of area.
	 */
	public Area(String id) {
		super(id);
	}
	
	/**
	 * Checks if user is inside area.
	 * @return true if user is inside, false otherwise.
	 */
	public final boolean isInside() {
		return inArea;
	}
	
	/**
	 * Checks if particular latitude and longitude is in this area.
	 * @param lat latitude
	 * @param lon longitude
	 * @return true if location is inside area, false otherwise
	 */
	abstract protected boolean isPointInArea(double lat, double lon);
	
	/**
	 * Updates actual location of player for this area and call hooks if entering/leaving.
	 * @param lat latitude of player
	 * @param lon longitude of player
	 */
	public void updateLocation(double lat, double lon) {
		boolean r = isPointInArea(lat, lon);
		
		if (inArea != r) {
			// entering or leaving area
			Log.i(TAG, String.format("We %s location", (r ? "entered" : "leaved")));
			inArea = r;
			callHooks(r);
		}
	}
	
	/**
	 * Call valid attached hooks.
	 * @param inside - did player entered or leaved this area?
	 */
	protected void callHooks(boolean inside) {
		if (hooks == null)
			return;
		
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case AREA:
				valid = true;
				break;
			case AREA_ENTER:
				valid = inside;
				break;
			case AREA_LEAVE:
				valid = !inside;
				break;
			default:
				break;
			}
				
			if (valid)
				h.call(null);
		}
	}

}
