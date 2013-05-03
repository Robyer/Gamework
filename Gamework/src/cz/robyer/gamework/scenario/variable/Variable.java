package cz.robyer.gamework.scenario.variable;

import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.reaction.VariableReaction.OperatorType;

/**
 * Abstract variable object.
 * @author Robert Pösel
 */
public abstract class Variable extends HookableObject {

	/**
	 * Class constructor.
	 * @param id - identificator of variable
	 */
	public Variable(String id) {
		super(id);
	}
	
	/**
	 * This method applies modification of variable and call hooks.
	 * @param type of operator to be applied
	 * @param value value to be used
	 */
	public abstract void modify(OperatorType type, Object value);
	
}
