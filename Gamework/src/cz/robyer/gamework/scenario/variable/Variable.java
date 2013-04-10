package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.HookableObject;

/**
 * Abstract variable object.
 * @author Robert Pösel
 */
public abstract class Variable extends HookableObject {

	public Variable(String id) {
		super(id);
	}
	
	public abstract void modify(int type, String value);
	
}
