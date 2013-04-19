package cz.robyer.gamework.scenario.reaction;

import android.util.Log;
import cz.robyer.gamework.scenario.message.Message;

/**
 * Game reaction which "receive" (make available to user) game message.
 * @author Robert Pösel
 */
public class MessageReaction extends Reaction {
	protected String value;
	protected Message message;

	/**
	 * Class constructor
	 * @param id Identificator of this reaction.
	 * @param value Identificator of game message which will be received.
	 */
	public MessageReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		message = getScenario().getMessage(value);
		if (message == null)
			Log.e(TAG, String.format("Message '%s' is null", value));
		
		return message != null;
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (message == null) {
			Log.e(TAG, "Message to process is null");
			return;
		}
		
		message.activate();
	}

}
