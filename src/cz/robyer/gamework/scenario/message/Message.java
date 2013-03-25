package cz.robyer.gamework.scenario.message;

import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.scenario.IdentificableObject;

public abstract class Message extends IdentificableObject {
	protected String title;
	protected String value;
	protected Status status;
	protected long received;
	
	public static enum Status {NONE, UNREAD, READ, DELETED}
	
	public Message(String id, String title, String value) {
		super(id);
		this.title = title;
		this.value = value;
		this.status = Status.NONE;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isVisible() {
		return (status != Status.NONE) && (status != Status.DELETED);
	}
	
	public void activate() {
		if (status == Status.NONE) {
			GameHandler handler = getScenario().getHandler();
			
			status = Status.UNREAD;
			received = handler.getGameTime();
			handler.broadcastEvent(GameEvent.UPDATED_MESSAGES);
		}
	}
	
}
