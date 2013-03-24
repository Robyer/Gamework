package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.reaction.VariableReaction;

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
	
	public void modify(int type, String s) {
		boolean value = Boolean.parseBoolean(s);
		
		switch (type) {
		case VariableReaction.SET:
			this.value = value;
			break;
		case VariableReaction.NEGATE:
			this.value = !this.value;
			break;
		default:
			// TODO: not supported exception
			break;
		}
		
		callHooks(this);
	}
}
