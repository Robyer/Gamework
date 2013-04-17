package cz.robyer.gamework.game;

import java.util.WeakHashMap;

import cz.robyer.gamework.game.GameEvent.EventType;
import cz.robyer.gamework.util.Log;

/**
 * Handler for broadcasting {@link GameEvent}s.
 * @author Robert Pösel
 */
public class GameHandler implements GameEventBroadcaster {
	private static final String TAG = GameHandler.class.getSimpleName();
	
	private final WeakHashMap<GameEventListener, Boolean> listeners = new WeakHashMap<GameEventListener, Boolean>();
	private final GameService parent;
	
	/**
	 * Class constructor.
	 * @param parent GameService which uses this handler.
	 */
	public GameHandler(GameService parent) {
		this.parent = parent;
	}
	
	/**
	 * Add listener.
	 * @param listener
	 * @return true if listener was added or false if it already existed
	 */
	public synchronized boolean addListener(GameEventListener listener) {
		Log.d(TAG, "Adding GameEventListener " + listener.toString());
		return (listeners.put(listener, true) == null);
	}
	
	/**
	 * Remove registered listener.
	 * @param listener to remove
	 * @return true when listener was removed or false if it did not existed
	 */
	public synchronized boolean removeListener(GameEventListener listener) {
		Log.d(TAG, "Removing GameEventListener " + listener.toString());
		return (listeners.remove(listener) != null);
 	}
	
	/**
	 * Removes all registered listeners.
	 */
	public synchronized void clearListeners() {
		Log.d(TAG, "Clearing GameEventListeners");
		listeners.clear();
	}
	
	/**
	 * Broadcasts {@link GameEvent} to all registered listeners.
	 */
	@Override
	public synchronized void broadcastEvent(GameEvent event) {
		int severity = android.util.Log.INFO;
		if (event.type == EventType.UPDATED_LOCATION ||
			event.type == EventType.UPDATED_TIME) {
			severity = android.util.Log.DEBUG;
	    }
	
	    if (Log.loggingEnabled()) {
	    	Log.println(severity, TAG, "Broadcasting event " + event.type.name() + " to " + listeners.size() + " listeners");
	    }
	
	    for (GameEventListener listener : listeners.keySet()) {
	    	if (listener != null) {
	    		listener.receiveEvent(event);
	    	}
	    }
	}
	
	/**
	 * Broadcasts {@link GameEvent} to all registered listeners.
	 * @param type
	 */
	public synchronized void broadcastEvent(EventType type) {
		broadcastEvent(new GameEvent(type));
	}

	/**
	 * Returns actual game time in milliseconds.
	 */
	public long getGameTime() {
		return parent.getTime();
	}
}
