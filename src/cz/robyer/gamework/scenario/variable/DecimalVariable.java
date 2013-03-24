package cz.robyer.gamework.scenario.variable;

import java.security.InvalidParameterException;

import cz.robyer.gamework.scenario.reaction.VariableReaction;
import cz.robyer.gamework.util.Log;

public class DecimalVariable extends Variable {
	protected int value;
	protected int min = 0;
	protected int max = 0;

	public DecimalVariable(String id, int value) {
		super(id);
		this.value = value;
	}
	
	public DecimalVariable(String id, int value, int min, int max) {
		this(id, value);
		setLimit(min, max);
	}

	public static DecimalVariable fromString(String id, String value) {
		return new DecimalVariable(id, Integer.parseInt(value));
	}
	
	public static DecimalVariable fromString(String id, String value, String min, String max) {
		DecimalVariable variable = new DecimalVariable(id, Integer.parseInt(value));
		variable.setLimit(Integer.parseInt(min), Integer.parseInt(max));
		return variable;
	}
		
	public void setValue(int value) {
		this.value = value;
		checkLimit();
	}
	
	public int getValue() {
		return value;
	}
	
	public void setLimit(int min, int max) {
		if (min > max) {
			Log.e(TAG, String.format("Minimum value (%d) must be lower than maximum value (%d)", min, max));
			throw new InvalidParameterException("Minimum value must be lower than maximum value");
		}
	
		this.min = min;
		this.max = max;
	}
	
	private void checkLimit() {
		// if there is any limit applied
		if (min != max) {
			value = Math.min(Math.max(value, min), max);
		}
	}
	
	public void modify(int type, String s) {
		int value = Integer.parseInt(s);
		
		switch (type) {
		case VariableReaction.SET:
			this.value = value;
			break;
		case VariableReaction.INCREMENT:
			this.value += value;
			break;
		case VariableReaction.DECREMENT:
			this.value -= value;
			break;
		case VariableReaction.MULTIPLY:
			this.value *= value;
			break;
		case VariableReaction.DIVIDE:
			this.value /= value;
			break;
		default:
			// TODO: not supported exception
			break;
		}
		
		checkLimit();
		callHooks(this);
	}

}
