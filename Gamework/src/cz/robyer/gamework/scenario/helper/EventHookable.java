package cz.robyer.gamework.scenario.helper;

import android.util.Log;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.hook.Hook;
import cz.robyer.gamework.scenario.hook.Hook.HookType;

/**
 * This object distributes custom events ({@link #update(Object)}) to hooked scenario objects.
 * @author Robert Pösel
 */
public class EventHookable extends HookableObject {
	private static final String TAG = EventHookable.class.getSimpleName();
	
	/**
	 * Class constructor.
	 * @param scenario to be attached to.
	 */
	public EventHookable(Scenario scenario) {
		super("Gamework:EVENT");
		this.scenario = scenario;
	}
	
	/**
	 * Distributes custom event data to attached objects.
	 * @param data - right now only String object supported.
	 */
	public void update(Object data) {
		Log.d(TAG, "Distributing custom event (" + data + ")");
		callHooks(data);
	}
	
	/**
	 * Call attached hooks.
	 * @param data - data of custom event.
	 */
	protected void callHooks(Object data) {
		if (hooks == null)
			return;
		
		for (Hook h : hooks) {
			if (h.getType() != HookType.EVENT)
				continue;
			
			if (data instanceof String) {
				if (h.getValue().equalsIgnoreCase((String)data))
					h.call(null);
			} else {
				Log.e(TAG, "Unsupported Custom event data type");
			}
		}
	}
	
}
