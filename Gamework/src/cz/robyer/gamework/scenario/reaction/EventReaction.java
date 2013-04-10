package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.game.GameEvent;

/**
 * 
 * @author Robert Pösel
 */
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
