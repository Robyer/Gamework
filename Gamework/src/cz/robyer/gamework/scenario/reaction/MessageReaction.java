package cz.robyer.gamework.scenario.reaction;

import android.util.Log;
import cz.robyer.gamework.scenario.message.Message;

/**
 * 
 * @author Robert Pösel
 */
public class MessageReaction extends Reaction {
	protected String value;
	protected Message message;

	public MessageReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	@Override
	public boolean onScenarioLoaded() {
		message = getScenario().getMessage(value);
		if (message == null)
			Log.e(TAG, String.format("Message '%s' is null", value));
		
		return message != null;
	}

	@Override
	public void action() {
		if (message == null) {
			Log.e(TAG, "Message to process is null");
			return;
		}
		
		message.activate();
	}

}
