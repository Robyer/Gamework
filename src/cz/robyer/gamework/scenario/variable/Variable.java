package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.IdentificableObject;

public abstract class Variable extends IdentificableObject {
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_DECIMAL = "decimal";

	public Variable(String id) {
		super(id);
	}
	
}
