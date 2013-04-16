package cz.robyer.gamework.scenario.area;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.HookableObject;

/**
 * 
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
	
	abstract protected boolean isPointInArea(double lat, double lon);
	
	public void updateLocation(double lat, double lon) {
		boolean r = isPointInArea(lat, lon);
		
		if (inArea != r) {
			// entering or leaving area
			Log.i(TAG, String.format("We %s location", (r ? "entered" : "leaved")));
			inArea = r;
			callHooks(r);
		}
	}
	
	protected void callHooks(boolean inside) {
		if (hooks == null)
			return;
		
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case Hook.TYPE_AREA:
				valid = true;
				break;
			case Hook.TYPE_AREA_ENTER:
				valid = inside;
				break;
			case Hook.TYPE_AREA_LEAVE:
				valid = !inside;
				break;
			}
				
			if (valid)
				h.call(null);
		}
	}

}
