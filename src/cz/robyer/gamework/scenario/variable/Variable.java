package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.IdentificableObject;
import cz.robyer.gamework.util.Log;

public abstract class Variable extends IdentificableObject {
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_DECIMAL = "decimal";

	public Variable(String id) {
		super(id);
	}
	
	public void callHooks() {
		// TODO
		Log.d("Variable", "Changed value of variable '" + id + "'.");
	}
	
	public abstract void modify(int type, String value);
	
}
