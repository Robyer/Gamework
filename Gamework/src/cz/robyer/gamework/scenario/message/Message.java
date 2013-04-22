package cz.robyer.gamework.scenario.message;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEvent.EventType;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.scenario.IdentificableObject;

/**
 * Represents in-game message.
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
	
	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		InputStream stream = null;
		String content = null;
		try {
			stream = getContext().getAssets().open(value);
			int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            content = new String(buffer);
		} catch (IOException e) {
			Log.e(TAG, String.format("Can't load file '%s'", value));
		} finally {
			try {
	        	if (stream != null)
	        		stream.close();
	        } catch (IOException ioe) {
	        	Log.e(TAG, ioe.getMessage(), ioe);
	        }
		}
		return content;
	}
	
	public long getReceived() {
		return received;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isVisible() {
		return (status != MessageStatus.NONE) && (status != MessageStatus.DELETED);
	}
	
	public void activate() {
		if (status == MessageStatus.NONE) {
			GameHandler handler = getScenario().getHandler();
			
			status = MessageStatus.UNREAD;
			received = handler.getGameTime();
			handler.broadcastEvent(new GameEvent(EventType.UPDATED_MESSAGES, id));
		}
	}
	
}
