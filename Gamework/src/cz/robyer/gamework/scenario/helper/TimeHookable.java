package cz.robyer.gamework.scenario.helper;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.variable.DecimalVariable;

/**
 * This object distributes time events ({@link #updateTime(long)}) to hooked scenario objects.
 * @author Robert Pösel
 */
public class TimeHookable extends HookableObject {
	private static final String TAG = TimeHookable.class.getSimpleName();
	
	private DecimalVariable variable = new DecimalVariable("", 0);
	
	/**
	 * Class constructor.
	 * @param scenario to be attached to.
	 */
	public TimeHookable(Scenario scenario) {
		super("Gamework:TIME");
		this.scenario = scenario;
	}
	
	/**
	 * Distributes time data to attached objects.
	 * @param data - game time value
	 */
	public void updateTime(long time) {
		Log.d(TAG, "Distributing time (" + time + ")");		
		callHooks(time);
	}
	
	/**
	 * Call attached hooks with actual time value.
	 * @param time - actual time value.
	 */
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
				} else {
					Log.e(TAG, "Unknown time hook value '" + h.getValue() + "'");
				}
				break;
			}
			
			if (valid)
				h.call(variable);
		}
	}
	
}
