package cz.robyer.gamework.scenario.area;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.HookableObject;

public abstract class Area extends HookableObject {
	
	protected boolean inArea = false;
	
	public Area(String id) {
		super(id);
	}
	
	abstract protected boolean isPointInArea(double lat, double lon);
	
	public void checkPointInArea(double lat, double lon) {
		boolean r = isPointInArea(lat, lon);
		
		Log.d("Area", "Checking point in area - was: " + (inArea ? "true" : "false") + ", is: " + (r ? "true" : "false"));
		
		if (inArea != r) {
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
				
			Log.d("Area", valid ? "Calling hooks." : "Not calling hooks.");
				
			if (valid)
				h.call();
		}
	}

}
