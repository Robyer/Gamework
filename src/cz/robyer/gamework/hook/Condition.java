package cz.robyer.gamework.hook;

import cz.robyer.gamework.scenario.BaseObject;
import cz.robyer.gamework.scenario.variable.Variable;

public class Condition extends BaseObject {
	public static final int TYPE_EQUALS = 0;
	public static final int TYPE_NOTEQUALS = 1;
	public static final int TYPE_TRUE = 2;
	public static final int TYPE_FALSE = 3;
	public static final int TYPE_GREATER = 4;
	public static final int TYPE_SMALLER = 5;
	public static final int TYPE_GREATEREQUALS = 6;
	public static final int TYPE_SMALLEREQUALS = 7;
	
	protected int type;
	protected String variable;
	protected String value;
	
	public Condition(int type, String variable, String value) {
		super();
		this.type = type;
		this.variable = variable;
		this.value = value;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isValid() {
		Variable variable = getScenario().getVariable(this.variable);
		boolean valid = false;
		
		if (variable != null) {		
			switch (type) {
			case TYPE_EQUALS:
				
				break;
			case TYPE_NOTEQUALS:
				
				break;
			case TYPE_TRUE:
				
				break;
			case TYPE_FALSE:
				
				break;
			case TYPE_GREATER:
				
				break;
			case TYPE_SMALLER:
				
				break;
			case TYPE_GREATEREQUALS:
				
				break;
			case TYPE_SMALLEREQUALS:
				
				break;
			}
		}

		return valid;
	}

}
