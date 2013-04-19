package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.reaction.VariableReaction;
import cz.robyer.gamework.util.Log;

/**
 * Represents variable with only 2 states - true and false.
 * @author Robert Pösel
 */
public class BooleanVariable extends Variable {
	protected boolean value;
	
	public BooleanVariable(String id, boolean value) {
		super(id);
		this.value = value;
	}
	
	public static BooleanVariable fromString(String id, String value) {
		return new BooleanVariable(id, Boolean.parseBoolean(value));
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	@Override
	public void modify(int type, Object val) {
		boolean value;
		
		if (val instanceof Integer)
			value = (Boolean)val;
		else if (val instanceof String)
			value = Boolean.parseBoolean((String)val);
		else {
			Log.e(TAG, "Not supported VariableReaction value");
			return;
		}
		
		switch (type) {
		case VariableReaction.SET:
			this.value = value;
			break;
		case VariableReaction.NEGATE:
			this.value = !this.value;
			break;
		default:
			Log.e(TAG, "Not supported VariableReaction type");
			return;
		}
		
		callHooks(this);
	}
}
