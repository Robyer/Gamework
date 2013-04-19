package cz.robyer.gamework.scenario.message;

import cz.robyer.gamework.game.GameEvent.EventType;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.scenario.IdentificableObject;

/**
 * 
 * @author Robert Pösel
 */
public class Message extends IdentificableObject {
	protected String title;
	protected String value;
	protected MessageStatus status;
	protected long received;
	
	public static enum MessageStatus {NONE, UNREAD, READ, DELETED}
	
	public Message(String id, String title, String value) {
		super(id);
		this.title = title;
		this.value = value;
		this.status = MessageStatus.NONE;
	}
	
	public void setStatus(MessageStatus status) {
		this.status = status;
	}
	
	public MessageStatus getStatus() {
		return status;
	}
	
	public boolean isVisible() {
		return (status != MessageStatus.NONE) && (status != MessageStatus.DELETED);
	}
	
	public void activate() {
		if (status == MessageStatus.NONE) {
			GameHandler handler = getScenario().getHandler();
			
			status = MessageStatus.UNREAD;
			received = handler.getGameTime();
			handler.broadcastEvent(EventType.UPDATED_MESSAGES);
		}
	}
	
}
