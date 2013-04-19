package cz.robyer.gamework.scenario.reaction;

import android.util.Log;
import cz.robyer.gamework.scenario.variable.BooleanVariable;
import cz.robyer.gamework.scenario.variable.DecimalVariable;
import cz.robyer.gamework.scenario.variable.Variable;

/**
 * Game reaction which modify value of game variable.
 * @author Robert Pösel
 */
public class VariableReaction extends Reaction {
	public static enum OperatorType {SET, INCREMENT, DECREMENT, MULTIPLY, DIVIDE, NEGATE};
	
	protected String variable;
	protected String value;
	protected OperatorType type;
	
	/** holds Variable object from scenario to optimize access time */
	protected Variable var;
	/** holds value as correct type to optimize use */
	protected Object val;
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param type Type of modification to apply to variable.
	 * @param variable Identificator of variable which should be modified.
	 * @param value Value which will be used in modification.
	 */
	public VariableReaction(String id, OperatorType type, String variable, String value) {
		super(id);
		this.type = type;
		this.variable = variable;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		var = getScenario().getVariable(variable);
		if (var == null)
			Log.e(TAG, String.format("Variable '%s' is null", variable));
		else {
			// prepare corect type of value for optimalization
			if (var instanceof BooleanVariable) {
				val = Boolean.parseBoolean(value);
			} else if (var instanceof DecimalVariable) {
				val = Integer.parseInt(value);
			} else {
				// no known type, use classic string representation
				val = value;
			}
		}
		
		return var != null;
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (var == null) {
			Log.e(TAG, String.format("Variable '%s' is null", variable));
			return;
		}
		
		var.modify(type, val);
	}

}
