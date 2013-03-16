package cz.robyer.gamework;

import java.util.WeakHashMap;

import cz.robyer.gamework.util.Log;

public class GameEventHandler implements GameEventBroadcaster {

	// We use a weak hash map to ensure that if an object is added to this as a
	// listener, but never removes itself, we won't keep it from being
	// garbage collected.
	private final WeakHashMap<GameEventListener, Boolean> listeners =
			new WeakHashMap<GameEventListener, Boolean>();

	public synchronized boolean addListener(GameEventListener listener) {
		Log.d("GameEventHandler", "Adding GameEventListener " + listener.toString());
		if (listeners.put(listener, true) == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean removeListener(GameEventListener listener) {
		Log.d("GameEventHandler", "Removing GameEventListener " + listener.toString());
		return listeners.remove(listener);
 	}
	
	public synchronized void clearListeners() {
		Log.d("GameEventHandler", "Clearing GameEventListeners.");
		listeners.clear();
	}
	
	public synchronized void broadcastEvent(GameEvent event) {
		int severity = android.util.Log.INFO;
		if (event == GameEvent.UPDATED_LOCATION ||
			event == GameEvent.UPDATED_TIME) {
			severity = android.util.Log.DEBUG;
	    }
	
	    if (Log.loggingEnabled()) {
	    	Log.println(severity, "GameEventHandler", "Broadcasting event " + event.name());
	    }
	
	    for (GameEventListener listener : listeners.keySet()) {
	    	if (listener != null) {
	    		listener.receiveEvent(event);
	    	}
	    }
	}
}
