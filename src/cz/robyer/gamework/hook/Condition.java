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
	
	public static final int TYPE_EQUALS = 0;
	public static final int TYPE_NOTEQUALS = 1;
	public static final int TYPE_GREATER = 3;
	public static final int TYPE_SMALLER = 4;
	public static final int TYPE_GREATEREQUALS = 5;
	public static final int TYPE_SMALLEREQUALS = 6;
	
	protected int type;
	protected String variable;
	protected String value;
	protected Hook parent;
	
	/** holds Variable object from scenario to optimize access time */
	protected Variable var;
	
	/**
	 * Basic constructor.
	 * @param type of condition
	 * @param id of variable for condition, could be empty
	 * @param value to be compared with value of variable
	 */
	public Condition(int type, String variable, String value) {
		super();
		this.type = type;
		this.variable = variable;
		this.value = value;
	}
	
	@Override
	public boolean onScenarioLoaded() {
		var = getScenario().getVariable(variable);
		if (var == null)
			Log.e(TAG, String.format("Variable '%s' is null", variable));
		
		return var != null;
	}
	
	public int getType() {
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
		if (this.variable.length() > 0) {
			Log.d(TAG, String.format("This condition uses own variable '%s'.", this.variable));
			variable = var;
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
			case TYPE_EQUALS:
				valid = (varValue == condValue);
				break;
			case TYPE_NOTEQUALS:
				valid = (varValue != condValue);
				break;
			}
		} else if (variable instanceof DecimalVariable) {
			int varValue = ((DecimalVariable)variable).getValue();
			int condValue = Integer.parseInt(value);
			
			switch (type) {
			case TYPE_EQUALS:
				valid = (varValue == condValue);
				break;
			case TYPE_NOTEQUALS:
				valid = (varValue != condValue);
				break;
			case TYPE_GREATER:
				valid = (varValue > condValue);
				break;
			case TYPE_SMALLER:
				valid = (varValue < condValue);
				break;
			case TYPE_GREATEREQUALS:
				valid = (varValue >= condValue);
				break;
			case TYPE_SMALLEREQUALS:
				valid = (varValue <= condValue);
				break;
			}
		}

		return valid;
	}

}
