package cz.robyer.gamework.scenario.reaction;

import android.util.Log;
import cz.robyer.gamework.scenario.variable.Variable;

/**
 * 
 * @author Robert Pösel
 */
public class VariableReaction extends Reaction {
	public static final int SET = 0;
	// decimals only
	public static final int INCREMENT = 1;
	public static final int DECREMENT = 2;
	public static final int MULTIPLY = 3;
	public static final int DIVIDE = 4;
	// booleans only
	public static final int NEGATE = 5;
	
	protected String variable;
	protected String value;
	protected int type;
	
	public VariableReaction(String id, int type, String variable, String value) {
		super(id);
		this.type = type;
		this.variable = variable;
		this.value = value;
	}

	@Override
	public void action() {
		Variable var = getScenario().getVariable(variable);
		if (var != null)
			var.modify(type, value); // TODO: improve effectivity somehow? (not convert from String all the time?)
		else
			Log.e(TAG, String.format("Variable '%s' is null", variable));
	}

}
