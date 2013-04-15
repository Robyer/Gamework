package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEvent.EventType;

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
	
	public EventReaction(String id, EventType type) {
		this(id, new GameEvent(type));
	}

	@Override
	public void action() {
		getHandler().broadcastEvent(value);
	}

}
