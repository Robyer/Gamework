package cz.robyer.gamework.scenario;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.variable.DecimalVariable;

/**
 * 
 * @author Robert Pösel
 */
public class TimeUpdater extends HookableObject {
	private static final String TAG = TimeUpdater.class.getSimpleName();
	
	private DecimalVariable variable = new DecimalVariable("", 0);
	
	public TimeUpdater(Scenario scenario) {
		super("Gamework:TIME");
		this.scenario = scenario;
	}
	
	public void updateTime(long time) {
		Log.d(TAG, "Updating time (" + time + ")");		
		callHooks(time);
	}
	
	protected void callHooks(long time) {
		if (hooks == null)
			return;
		
		time /= 1000; // from mili to seconds
		int seconds = (int)(time % 60);
		int minutes = (int)(time / 60);
		int hours = (int)(time / 3600);
	
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case Hook.TYPE_TIME:				
				if (h.getValue().equalsIgnoreCase("second")) {
					valid = true;
					variable.setValue(seconds);
				} else if (h.getValue().equalsIgnoreCase("minute")) {
					valid = (seconds == 0);
					variable.setValue(minutes);
				} else if (h.getValue().equalsIgnoreCase("hour")) {
					valid = (seconds == 0 && minutes == 0);
					variable.setValue(hours);
				}
				break;
			}
			
			if (valid)
				h.call(variable);
		}
	}
	
}
