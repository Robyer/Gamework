package cz.robyer.gamework.hook;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.scenario.BaseObject;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;
import cz.robyer.gamework.util.Log;

/**
 * Represents hook which is activated when some game state (variable, time,...) is changed.
 * @author Robert Pösel
 */
public class Hook extends BaseObject {
	private static final String TAG = Hook.class.getSimpleName();

	public static enum HookType {AREA, AREA_ENTER, AREA_LEAVE, VARIABLE, TIME, EVENT, SCANNER};	
	public static enum HookConditions {NONE, ANY, ALL};
	
	public static final int RUN_ALWAYS = -1;
	
	protected HookType type;
	protected HookConditions conditions_type;
	protected int runs;
	protected String value; 
	protected String reaction;
	protected List<Condition> conditions;
	protected HookableObject parent;
	
	protected Reaction react;
	
	/**
	 * Class constructor.
	 * @param type
	 * @param value
	 * @param reaction
	 * @param conditions_type
	 * @param runs
	 */
	public Hook(HookType type, String value, String reaction, HookConditions conditions_type, int runs) {
		super();
		this.type = type;
		this.value = value;
		this.reaction = reaction;
		this.conditions_type = conditions_type;
		this.runs = (runs > 0 ? runs : RUN_ALWAYS);
	}

	/**
	 * Class constructor.
	 * @param type
	 * @param value
	 * @param reaction
	 * @param conditions_type
	 */
	public Hook(HookType type, String value, String reaction, HookConditions conditions_type) {
		this(type, value, reaction, conditions_type, RUN_ALWAYS);
	}
	
	/**
	 * Class constructor.
	 * @param type
	 * @param value
	 * @param reaction
	 */
	public Hook(HookType type, String value, String reaction) {
		this(type, value, reaction, HookConditions.NONE, RUN_ALWAYS);
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#setScenario(cz.robyer.gamework.scenario.Scenario)
	 */
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
	
		if (conditions != null)
			for (Condition c : conditions) {
				c.setScenario(scenario);
			}
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		react = scenario.getReaction(reaction);
		if (react == null)
			Log.e(TAG, String.format("Reaction '%s' is null", reaction));
		
		boolean ok = react != null;
		
		if (conditions != null)
			for (Condition c : conditions) {
				if (!c.onScenarioLoaded())
					ok = false;
			}
		
		return ok;
	}
	
	/**
	 * Sets hook parent on which is attached.
	 * @param parent
	 */
	public void setParent(HookableObject parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns hook parent.
	 */
	public HookableObject getParent() {
		if (parent == null) {
			Log.e(TAG, "No parent is attached");
			throw new RuntimeException();
		}

		return parent;
	}
	
	/**
	 * Returns hook type.
	 * @return type
	 */
	public HookType getType() {
		return type;
	}
	
	/**
	 * Returns hook value.
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Add condition into list.
	 * @param condition
	 */
	public void addCondition(Condition condition) {
		if (condition == null) {
			Log.w(TAG, "addCondition() with null condition");
			return;
		}
		
		if (conditions == null)
			conditions = new ArrayList<Condition>();
		
		if (isAttached())
			condition.setScenario(scenario);
		
		condition.setParent(this);
		conditions.add(condition);
	}

	/**
	 * Run reaction if conditions and other conditions (runs) are valid.
	 * @param variable which was changed (and which runned this hook)
	 */
	public void call(Variable variable) {
		if (react == null) {
			Log.e(TAG, "Reaction to call is null");
			return;
		}
		
		boolean valid = false;
			
		switch (conditions_type) {
		case NONE:
			valid = true;
			Log.v(TAG, "Checking NO conditions");
			break;
		case ALL:
			valid = true;
			if (conditions != null)
				for (Condition condition : conditions) {
					if (!condition.isValid(variable)) {
						valid = false;
						break;
					}
				}
			Log.v(TAG, "Checking ALL conditions, valid=" + valid);
			break;
		case ANY:
			if (conditions != null)
				for (Condition condition : conditions) {
					if (condition.isValid(variable)) {
						valid = true;
						break;
					}
				}
			Log.v(TAG, "Checking ANY conditions, valid=" + valid);
			break;
		}
		
		if (runs != RUN_ALWAYS) {
			if (runs > 0) {
				runs--;
			} else {
				valid = false;
			}
		}
		
		if (valid)
			react.action();
	}

}
