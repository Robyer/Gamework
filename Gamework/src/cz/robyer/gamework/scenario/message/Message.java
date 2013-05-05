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
	protected String tag;
	protected String title;
	protected String value;
	protected MessageStatus status;
	protected long received;
	
	public static enum MessageStatus {NONE, UNREAD, READ, DELETED}
	
	/**
	 * Class constructor
	 * @param id - identificator of message
	 * @param title - title of message
	 * @param value - text or path to file with content of message
	 */
	public Message(String id, String tag, String title, String value, boolean def) {
		super(id);
		this.title = title;
		this.tag = (tag == null ? "" : tag);
		this.value = value;
		this.status = def ? MessageStatus.UNREAD : MessageStatus.NONE;
	}
	
	/**
	 * Sets status of this message.
	 * @param status to be set
	 */
	public void setStatus(MessageStatus status) {
		this.status = status;
	}
	
	/**
	 * Returns actual status of this message.
	 * @return actual status
	 */
	public MessageStatus getStatus() {
		return status;
	}
	
	/**
	 * Returns tag of this message.
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Returns title of this message
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns content of text file in assets, which path is represented by value.
	 * @return text of message
	 */
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
	
	/**
	 * Return time when this message was received.
	 * @return long timestamp
	 */
	public long getReceived() {
		return received;
	}
	
	/**
	 * Returns value of this message.
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Checks if this message is received and not deleted.
	 * @return true if message is visible, false otherwise
	 */
	public boolean isVisible() {
		return (status != MessageStatus.NONE) && (status != MessageStatus.DELETED);
	}
	
	/**
	 * Receive this message - marks as unread if not visible yet.
	 */
	public void activate() {
		if (status == MessageStatus.NONE) {
			GameHandler handler = getScenario().getHandler();
			
			status = MessageStatus.UNREAD;
			received = handler.getGameTime();
			handler.broadcastEvent(new GameEvent(EventType.UPDATED_MESSAGES, id));
		}
	}
	
}
