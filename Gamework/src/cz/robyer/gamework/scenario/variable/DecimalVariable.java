package cz.robyer.gamework.scenario.variable;

import java.security.InvalidParameterException;

import cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType;
import cz.robyer.gamework.utils.Log;

/**
 * Represents numeric decimal variable.
 * @author Robert Pösel
 */
public class DecimalVariable extends Variable {
	protected int value;
	protected int min = 0;
	protected int max = 0;

	/**
	 * Class constructor.
	 * @param id - identificator of variable
	 * @param value - actual (default) value
	 */
	public DecimalVariable(String id, int value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Class constructor.
	 * @param id - identificator of variable
	 * @param value - actual (default) value
	 * @param min - minimal value
	 * @param max - maximal value
	 */
	public DecimalVariable(String id, int value, int min, int max) {
		this(id, value);
		setLimit(min, max);
	}

	/**
	 * Factory for creating this variable from string value.
	 * @param id - identificator of variable
	 * @param value - actual (default) value a string
	 * @return DecimalVariable
	 */
	public static DecimalVariable fromString(String id, String value) {
		return new DecimalVariable(id, Integer.parseInt(value));
	}
	
	/**
	 * Factory for creating this variable from string value.
	 * @param id - identificator of variable
	 * @param value - actual (default) value a string
	 * @param min - minimal value
	 * @param max - maximal value
	 * @return DecimalVariable
	 */
	public static DecimalVariable fromString(String id, String value, String min, String max) {
		DecimalVariable variable = new DecimalVariable(id, Integer.parseInt(value));
		variable.setLimit(Integer.parseInt(min), Integer.parseInt(max));
		return variable;
	}

	/**
	 * Sets value of this variable.
	 * @param value to be set
	 */
	public void setValue(int value) {
		this.value = value;
		checkLimit();
	}
	
	/**
	 * Get value of this variable.
	 * @return actual value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets limit of this variable, must be min < max.
	 * @param min - minimal value
	 * @param max - maximal value
	 */
	public void setLimit(int min, int max) {
		if (min > max) {
			Log.e(TAG, String.format("Minimum value (%d) must be lower than maximum value (%d)", min, max));
			throw new InvalidParameterException("Minimum value must be lower than maximum value");
		}
	
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Applies min/max limit to actual value.
	 */
	private void checkLimit() {
		// if there is any limit applied
		if (min != max) {
			value = Math.min(Math.max(value, min), max);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.variable.Variable#modify(cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType, java.lang.Object)
	 */
	@Override
	public void modify(OperatorType type, Object val) {
		int value;
		
		if (val instanceof Integer)
			value = (Integer)val;
		else if (val instanceof String)
			value = Integer.parseInt((String)val);
		else {
			Log.e(TAG, "Not supported VariableReaction value");
			return;
		}
		
		switch (type) {
		case SET:
			this.value = value;
			break;
		case INCREMENT:
			this.value += value;
			break;
		case DECREMENT:
			this.value -= value;
			break;
		case MULTIPLY:
			this.value *= value;
			break;
		case DIVIDE:
			this.value /= value;
			break;
		default:
			Log.e(TAG, "Not supported VariableReaction type");
			return;
		}
		
		checkLimit();
		callHooks(this);
	}

}
