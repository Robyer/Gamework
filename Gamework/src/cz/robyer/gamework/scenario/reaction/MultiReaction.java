package cz.robyer.gamework.scenario.reaction;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.hook.Condition;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.util.Log;

/**
 * 
 * @author Robert Pösel
 */
public class MultiReaction extends Reaction {

	protected List<Reaction> reactions;
	
	public MultiReaction(String id) {
		super(id);
	}
	
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
	
		if (reactions != null)
			for (Reaction r : reactions) {
				r.setScenario(scenario);
			}
	}
	
	@Override
	public boolean onScenarioLoaded() {
		boolean ok = true;
		if (reactions != null)
			for (Reaction r : reactions) {
				if (!r.onScenarioLoaded())
					ok = false;
			}
		
		return ok;
	}
		
	public void addReaction(Reaction reaction) {
		if (reaction == null) {
			Log.w(TAG, "addReaction() with null reaction");
			return;
		}
		
		if (reactions == null)
			reactions = new ArrayList<Reaction>();

		if (isAttached())
			reaction.setScenario(scenario);
		
		reactions.add(reaction);
	}
	
	@Override
	public void action() {
		if (reactions != null)
			for (Reaction reaction : reactions) {
				reaction.action();
			}
	}

}
