package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.GameEventHandler;

public class EventReaction extends Reaction {
	protected GameEvent value;
	
	public EventReaction(String id, GameEvent value) {
		super(id);
		this.value = value;
	}

	@Override
	public void action() {
		// TODO: Shouldn't this condition be in the Scenario or somewhere else?
		GameEventHandler handler = getScenario().getHandler();
		if (handler == null)
			throw new RuntimeException("EventReaction got null GameEventHandler from attached Scenario.");
			
		handler.broadcastEvent(value);
	}

}
