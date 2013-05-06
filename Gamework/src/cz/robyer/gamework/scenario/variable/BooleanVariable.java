package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType;
import cz.robyer.gamework.utils.Log;

/**
 * Represents variable with only 2 states - true and false.
 * @author Robert Pösel
 */
public class BooleanVariable extends Variable {
	protected boolean value;
	
	/**
	 * Class constructor.
	 * @param id - identificator of variable
	 * @param value - actual (default) value
	 */
	public BooleanVariable(String id, boolean value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Factory for creating this variable from string value.
	 * @param id - identificator of variable
	 * @param value - actual (default) value a string
	 * @return BooleanVariable
	 */
	public static BooleanVariable fromString(String id, String value) {
		return new BooleanVariable(id, Boolean.parseBoolean(value));
	}
	
	/**
	 * Sets value of this variable.
	 * @param value to be set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
	
	/**
	 * Get value of this variable.
	 * @return actual value
	 */
	public boolean getValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.variable.Variable#modify(cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType, java.lang.Object)
	 */
	@Override
	public void modify(OperatorType type, Object val) {
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
		case SET:
			this.value = value;
			break;
		case NEGATE:
			this.value = !this.value;
			break;
		default:
			Log.e(TAG, "Not supported VariableReaction type");
			return;
		}
		
		callHooks(this);
	}

}
