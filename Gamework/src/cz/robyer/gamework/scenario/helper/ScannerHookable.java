package cz.robyer.gamework.scenario.helper;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.hook.Hook.HookType;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.Scenario;

/**
 * This object distributes scanned data ({@link #update(Object)}) to hooked scenario objects.
 * @author Robert Pösel
 */
public class ScannerHookable extends HookableObject {
	private static final String TAG = ScannerHookable.class.getSimpleName();
	
	/**
	 * Class constructor.
	 * @param scenario to be attached to.
	 */
	public ScannerHookable(Scenario scenario) {
		super("Gamework:SCANNER");
		this.scenario = scenario;
	}
	
	/**
	 * Distributes scanned data to attached objects.
	 * @param data - right now only String object supported.
	 */
	public void update(Object data) {
		Log.d(TAG, "Distributing scanned data (" + data + ")");
		callHooks(data);
	}
	
	/**
	 * Call attached hooks.
	 * @param data - scanned data.
	 */
	protected void callHooks(Object data) {
		if (hooks == null)
			return;
		
		for (Hook h : hooks) {
			if (h.getType() != HookType.SCANNER)
				continue;
			
			if (data instanceof String) {
				if (h.getValue().equalsIgnoreCase((String)data))
					h.call(null);
			} else {
				Log.e(TAG, "Unsupported scanned data type");
			}
		}
	}
	
}
