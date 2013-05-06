package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import cz.robyer.gamework.scenario.hook.Hook;
import cz.robyer.gamework.scenario.variable.Variable;

/**
 * Base hookable object which contains a list of attached hooks.
 * @author Robert Pösel
 */
public class HookableObject extends IdentificableObject {
	protected List<Hook> hooks;
	
	/**
	 * Class constructor.
	 * @param id of object
	 */
	public HookableObject(String id) {
		super(id);
	}
	
	/**
	 * Add new hook to be attached.
	 * @param hook
	 */
	public void addHook(Hook hook) {
		if (hooks == null)
			hooks = new ArrayList<Hook>();
		
		hooks.add(hook);
		hook.setParent(this);
	}
	
	/**
	 * Call all attached hooks.
	 * @param variable which was changed
	 */
	protected void callHooks(Variable variable) {
		Log.d(TAG, "Calling all hooks");
		if (hooks != null) {
			for (Hook h : hooks) {
				h.call(variable);
			}
		}
	}
	
}
