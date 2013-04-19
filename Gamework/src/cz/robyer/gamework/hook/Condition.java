package cz.robyer.gamework.hook;

import cz.robyer.gamework.scenario.BaseObject;
import cz.robyer.gamework.scenario.variable.BooleanVariable;
import cz.robyer.gamework.scenario.variable.DecimalVariable;
import cz.robyer.gamework.scenario.variable.Variable;
import cz.robyer.gamework.util.Log;

/**
 * Represents condition for {@link Hook}.
 * @author Robert Pösel
 */
public class Condition extends BaseObject {
	public static final String TAG = Condition.class.getSimpleName();
	
	public static enum ConditionType {EQUALS, NOTEQUALS, GREATER, SMALLER, GREATEREQUALS, SMALLEREQUALS};
	
	protected ConditionType type;
	protected String variable;
	protected String value;
	protected Hook parent;
	
	/**
	 * Basic constructor.
	 * @param type of condition
	 * @param id of variable for condition, could be empty
	 * @param value to be compared with value of variable
	 */
	public Condition(ConditionType type, String variable, String value) {
		super();
		this.type = type;
		this.variable = variable;
		this.value = value;
	}
	
	public ConditionType getType() {
		return type;
	}
	
	public void setParent(Hook parent) {
		this.parent = parent;
	}
	
	public Hook getParent() {
		if (parent == null) {
			Log.e(TAG, "No parent is attached");
			throw new RuntimeException();
		}

		return parent;
	}
	
	/**
	 * Checks if this condition pass.
	 * @param variable which was changed (and which called our hook)
	 * @return true if this condition is valid, false otherwise
	 */
	public boolean isValid(Variable variable) {
		// If this condition has defined own variable, we use that
		if (this.variable != null) {
			Log.d(TAG, String.format("This condition uses own variable '%s'.", this.variable));
			variable = getScenario().getVariable(this.variable);
		}
		
		if (variable == null) {
			Log.e(TAG, "Variable to check is null");
			return false;
		}
		
		boolean valid = false;
		
		if (variable instanceof BooleanVariable) {
			boolean varValue = ((BooleanVariable)variable).getValue();
			boolean condValue = Boolean.parseBoolean(value);
			
			switch (type) {
			case EQUALS:
				valid = (varValue == condValue);
				break;
			case NOTEQUALS:
				valid = (varValue != condValue);
				break;
			}
			
			Log.v(TAG, "Checking condition on varValue='" + varValue + ", condValue='" + condValue + "', valid='" + valid + "'");
		} else if (variable instanceof DecimalVariable) {
			int varValue = ((DecimalVariable)variable).getValue();
			int condValue = Integer.parseInt(value);
			
			switch (type) {
			case EQUALS:
				valid = (varValue == condValue);
				break;
			case NOTEQUALS:
				valid = (varValue != condValue);
				break;
			case GREATER:
				valid = (varValue > condValue);
				break;
			case SMALLER:
				valid = (varValue < condValue);
				break;
			case GREATEREQUALS:
				valid = (varValue >= condValue);
				break;
			case SMALLEREQUALS:
				valid = (varValue <= condValue);
				break;
			}
			
			Log.v(TAG, "Checking condition on varValue='" + varValue + ", condValue='" + condValue + "', valid='" + valid + "'");
		}

		return valid;
	}

}
