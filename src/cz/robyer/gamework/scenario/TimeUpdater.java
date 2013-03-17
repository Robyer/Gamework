package cz.robyer.gamework.scenario;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;

public class TimeUpdater extends HookableObject {
	
	public TimeUpdater(Scenario scenario) {
		super("Gamework:TIME");
		this.scenario = scenario;
	}
	
	public void updateTime(long time) {
		Log.d("TimeUpdater", "Updating time (" + time + ")");		
		callHooks(time);
	}
	
	protected void callHooks(long time) {
		if (hooks == null)
			return;
		
		long secs = time / 1000; // from mili to seconds
		boolean isMinute = (secs % 60 == 0);
		boolean isHour = (secs % 3600 == 0);
		
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case Hook.TYPE_TIME:				
				if (h.getValue().equalsIgnoreCase("second")) {
					valid = true;
				} else if (h.getValue().equalsIgnoreCase("minute")) {
					valid = isMinute;
				} else if (h.getValue().equalsIgnoreCase("hour")) {
					valid = isHour;
				}
				break;
			}
			
			if (valid)
				h.call();
		}
	}
	
}
