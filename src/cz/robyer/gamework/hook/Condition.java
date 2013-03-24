package cz.robyer.gamework.hook;

import cz.robyer.gamework.scenario.BaseObject;
import cz.robyer.gamework.scenario.variable.BooleanVariable;
import cz.robyer.gamework.scenario.variable.DecimalVariable;
import cz.robyer.gamework.scenario.variable.Variable;
import cz.robyer.gamework.util.Log;

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
	
	// holds Variable object from scenario to optimize access time
	protected Variable var;
	
	public Condition(int type, String variable, String value) {
		super();
		this.type = type;
		this.variable = variable;
		this.value = value;
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
	
	private Variable getVariable() {
		if (var == null)
			var = getScenario().getVariable(variable);
		
		return var;
	}
	
	public boolean isValid(Variable variable) {
		// If this condition has defined own variable, we use that
		if (this.variable.length() > 0)
			variable = getVariable();
		
		if (variable == null) {
			Log.e(TAG, "Variable to check is null");
			throw new RuntimeException();
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
