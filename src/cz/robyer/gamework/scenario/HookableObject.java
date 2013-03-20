package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import cz.robyer.gamework.hook.Hook;

public class HookableObject extends IdentificableObject {
	private static final String TAG = HookableObject.class.getSimpleName();
	
	protected List<Hook> hooks;
	
	public HookableObject(String id) {
		super(id);
	}
	
	public void addHook(Hook hook) {
		if (hooks == null)
			hooks = new ArrayList<Hook>();
		
		hooks.add(hook);
	}
	
	protected void callHooks() {
		Log.d(TAG, "Calling all hooks.");
		if (hooks != null) {
			for (Hook h : hooks) {
				h.call();
			}
		}
	}
	
}
