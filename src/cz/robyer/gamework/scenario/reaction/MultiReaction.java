package cz.robyer.gamework.scenario.reaction;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.scenario.Scenario;

public class MultiReaction extends Reaction {

	protected List<Reaction> reactions;
	
	public MultiReaction(String id) {
		super(id);
	}
	
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
	
		if (reactions != null)
			for (Reaction r : reactions) {
				r.setScenario(scenario);
			}
	}
		
	public void addReaction(Reaction reaction) {
		if (reaction != null) {
			if (reactions == null)
				reactions = new ArrayList<Reaction>();
			
			reactions.add(reaction);
		}
	}
	
	@Override
	public void action() {
		if (reactions != null)
			for (Reaction reaction : reactions) {
				reaction.action();
			}
	}

}
