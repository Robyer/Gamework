package cz.robyer.gamework;

import java.util.WeakHashMap;

import cz.robyer.gamework.util.Log;

public class GameEventHandler implements GameEventBroadcaster {
	private static final String TAG = GameEventHandler.class.getSimpleName();
	
	// We use a weak hash map to ensure that if an object is added to this as a
	// listener, but never removes itself, we won't keep it from being
	// garbage collected.
	private final WeakHashMap<GameEventListener, Boolean> listeners =
			new WeakHashMap<GameEventListener, Boolean>();

	public synchronized boolean addListener(GameEventListener listener) {
		Log.d(TAG, "Adding GameEventListener " + listener.toString());
		return (listeners.put(listener, true) == null);
	}
	
	public synchronized boolean removeListener(GameEventListener listener) {
		Log.d(TAG, "Removing GameEventListener " + listener.toString());
		return (listeners.remove(listener) != null);
 	}
	
	public synchronized void clearListeners() {
		Log.d(TAG, "Clearing GameEventListeners");
		listeners.clear();
	}
	
	public synchronized void broadcastEvent(GameEvent event) {
		int severity = android.util.Log.INFO;
		if (event == GameEvent.UPDATED_LOCATION ||
			event == GameEvent.UPDATED_TIME) {
			severity = android.util.Log.DEBUG;
	    }
	
	    if (Log.loggingEnabled()) {
	    	Log.println(severity, TAG, "Broadcasting event " + event.name() + " to " + listeners.size() + " listeners");
	    }
	
	    for (GameEventListener listener : listeners.keySet()) {
	    	if (listener != null) {
	    		listener.receiveEvent(event);
	    	}
	    }
	}
}
