package cz.robyer.gamework.hook;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.scenario.BaseObject;
import cz.robyer.gamework.scenario.HookableObject;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;
import cz.robyer.gamework.util.Log;

public class Hook extends BaseObject {
	private static final String TAG = Hook.class.getSimpleName();
	
	public static final int TYPE_AREA = 0;
	public static final int TYPE_AREA_ENTER = 1;
	public static final int TYPE_AREA_LEAVE = 2;
	public static final int TYPE_VARIABLE = 3;
	public static final int TYPE_TIME = 4;
	
	public static final int CONDITIONS_NONE = 0;
	public static final int CONDITIONS_ANY = 1;
	public static final int CONDITIONS_ALL = 2;
	
	public static final int RUN_ALWAYS = -1;
	
	protected int type;
	protected int conditions_type;
	protected int runs;
	protected String value; 
	protected String reaction;
	protected List<Condition> conditions;
	protected HookableObject parent;
	
	public Hook(int type, String value, String reaction, int conditions_type, int runs) {
		super();
		this.type = type;
		this.value = value;
		this.reaction = reaction;
		this.conditions_type = conditions_type;
		this.runs = (runs > 0 ? runs : RUN_ALWAYS);
	}
	
	public Hook(int type, String value, String reaction, int conditions) {
		this(type, value, reaction, conditions, RUN_ALWAYS);
	}
	
	public Hook(int type, String value, String reaction) {
		this(type, value, reaction, CONDITIONS_NONE, RUN_ALWAYS);
	}
	
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
	
		if (conditions != null)
			for (Condition c : conditions) {
				c.setScenario(scenario);
			}
	}
	
	public void setParent(HookableObject parent) {
		this.parent = parent;
	}
	
	public HookableObject getParent() {
		if (parent == null) {
			Log.e(TAG, "No parent is attached");
			throw new RuntimeException();
		}

		return parent;
	}
	
	public int getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}

	public void addCondition(Condition condition) {
		if (condition == null) {
			Log.w(TAG, "addCondition() with null condition");
			return;
		}
		
		if (conditions == null)
			conditions = new ArrayList<Condition>();
		
		condition.setParent(this);
		conditions.add(condition);
	}

	public void call(Variable variable) {
		Reaction reaction = scenario.getReaction(this.reaction);
		if (reaction != null) {
			boolean valid = false;
			
			switch (conditions_type) {
			case CONDITIONS_NONE:
				valid = true;
				break;
			case CONDITIONS_ALL:
				valid = true;
				if (conditions != null)
					for (Condition condition : conditions) {
						if (!condition.isValid(variable)) {
							valid = false;
							break;
						}
					}
				break;
			case CONDITIONS_ANY:
				if (conditions != null)
					for (Condition condition : conditions) {
						if (condition.isValid(variable)) {
							valid = true;
							break;
						}
					}
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
				reaction.action();
		}
	}

}
