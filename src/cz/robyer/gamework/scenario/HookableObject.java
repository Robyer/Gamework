package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.hook.Hook;

import android.util.Log;

public class HookableObject extends IdentificableObject {
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
		Log.d("HookableObject", "Calling all hooks.");
		for (Hook h : hooks) {
			h.call();
		}
	}
	
}
