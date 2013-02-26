package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.HookableObject;

public abstract class Variable extends HookableObject {

	public Variable(String id) {
		super(id);
	}
	
	/*public void callHooks() {
		// TODO
		Log.i("Variable", "Changed value of variable '" + id + "'.");
	}*/
	
	public abstract void modify(int type, String value);
	
}
