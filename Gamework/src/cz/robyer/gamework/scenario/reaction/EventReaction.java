package cz.robyer.gamework.scenario.reaction;

import android.util.Log;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEvent.EventType;

/**
 * Game reaction which sends game event.
 * @author Robert Pösel
 */
public class EventReaction extends Reaction {
	protected GameEvent value;
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param value GameEvent which should be sent.
	 */
	public EventReaction(String id, GameEvent value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param type EventType of event which should be set.
	 */
	public EventReaction(String id, EventType type) {
		this(id, new GameEvent(type));
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (value == null) {
			Log.e(TAG, "GameEvent is null");
			return;
		}
		
		getHandler().broadcastEvent(value);
	}

}
