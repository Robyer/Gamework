package cz.robyer.gamework.scenario.reaction;

import java.util.ArrayList;
import java.util.List;

import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.util.Log;

/**
 * Game reaction as container with other game reactions.
 * @author Robert Pösel
 */
public class MultiReaction extends Reaction {

	protected List<Reaction> reactions = new ArrayList<Reaction>();
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 */
	public MultiReaction(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#setScenario(cz.robyer.gamework.scenario.Scenario)
	 */
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
	
		for (Reaction r : reactions) {
			r.setScenario(scenario);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		// help variable as onScenarioLoaded() must be call'd for all reactions
		boolean ok = true;
		for (Reaction r : reactions) {
			if (!r.onScenarioLoaded())
				ok = false;
		}
		return ok;
	}
		
	/**
	 * Add child reaction into this container.
	 * @param reaction Reaction to be added. It CAN'T be another MultiReaction.
	 */
	public void addReaction(Reaction reaction) {
		if (reaction == null) {
			Log.w(TAG, "addReaction() with null reaction");
			return;
		} else if (reaction instanceof MultiReaction) {
			Log.e(TAG, "addReaction() can't add another MultiReaction");
			return;
		}
		
		if (isAttached())
			reaction.setScenario(scenario);
		
		reactions.add(reaction);
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		for (Reaction reaction : reactions) {
			reaction.action();
		}
	}

}
