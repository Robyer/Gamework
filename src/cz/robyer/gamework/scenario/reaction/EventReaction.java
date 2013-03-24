package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.GameEvent;

public class EventReaction extends Reaction {
	protected GameEvent value;
	
	public EventReaction(String id, GameEvent value) {
		super(id);
		this.value = value;
	}

	@Override
	public void action() {
		getHandler().broadcastEvent(value);
	}

}
