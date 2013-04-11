package cz.robyer.gamework.game;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import cz.robyer.gamework.constants.Constants;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioParser;
import cz.robyer.gamework.util.Log;

/**
 * Main service object representing whole game.
 * @author Robert Pösel
 */
public abstract class GameService extends Service implements GameEventListener, LocationListener {		
	private static final String TAG = GameService.class.getSimpleName();
	
	/** signalize that instance of GameService exists */
	private static boolean running;
	private static GameService instance;
	
	private GameHandler gameHandler = new GameHandler();
	
	private Scenario scenario;
	Status status = Status.GAME_STOPPED;
	
	private Timer timer; // http://stackoverflow.com/questions/4597690/android-timer-how
	private long time;
	private long start;
	private Location location;
	
	private LocationManager locationManager;
	
	// TODO: do something with this or remove it
	protected enum Status {
		GAME_STOPPED, 	// game has not started yet
		GAME_LOADING, 	// game is loading needed resources
		GAME_WAITING,	// game is waiting for starting event (player must stand on particular location, etc.) 
		GAME_RUNNING,	// game was started and is running 
		GAME_FINISHED,	// game was completed (either won or failed)
	};
	
	/**
	 * Shortcut for registering {@link GameEventListener}s.
	 */
	public final boolean registerListener(GameEventListener listener) {
    	return gameHandler.addListener(listener);
    }
    
	/**
	 * Shortcut for unregistering {@link GameEventListener}s.
	 */
    public final boolean unregisterListener(GameEventListener listener) {
    	return gameHandler.removeListener(listener);
    }
    
    @Override
    public final void onCreate() {
        // The service is being created    	
    	Log.i(TAG, "onCreate()");
    	
    	instance = this;
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
      
    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
    	Log.i(TAG, "onStartCommand()");
    	
    	if (running) {
    		// game is already running, do nothing
    		Log.i(TAG, "Game is already running");
    		onGameStart(false, intent);
    		return 0;    		
    	}
    	
    	String filename = intent.getStringExtra("filename");
    	scenario = ScenarioParser.fromAsset(getApplicationContext(), filename);
    	if (scenario == null) {
    		Log.e(TAG, "Scenario '" + filename + "' wasn't loaded");
    		return 0;
    	}
    	
    	Log.i(TAG, "Scenario '" + filename + "' was loaded");
    	
    	running = true;
    	scenario.setHandler(gameHandler);
    	registerListener(this);
    	start = SystemClock.uptimeMillis();
    		// system time clock: System.currentTimeMillis()
    		// uptime clock (not ticking in deep sleep): SystemClock.uptimeMillis();
    	
    	timer = new Timer();
    	timer.schedule(new TimerTask() {
    		@Override
    		public void run() {
    			time = calcGameTime();
    			gameHandler.broadcastEvent(GameEvent.UPDATED_TIME);
    		}
    	}, 1000, 1000);
    	    	
    	// make this service foreground and show notification
    	startForeground(Constants.NOTIFICATION_GAMEPLAY, getGameNotification());
    	
    	// register to get location updates
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		
		onGameStart(true, intent);
    	
    	// The service is starting, due to a call to startService()
        return START_NOT_STICKY; // or START_REDELIVER_INTENT?
    }
    
    @Override
    public final IBinder onBind(Intent intent) {
    	Log.i(TAG, "onBind()");
    	return null; // we don't support binding
    }

    @Override
    public final void onDestroy() {
        // The service is no longer used and is being destroyed
    	Log.i(TAG, "onDestroy()");    	    	
    	
    	running = false;
    	gameHandler.clearListeners();
    	
		timer.cancel();
		timer.purge();
		timer = null;
		
		locationManager.removeUpdates(this);
		location = null;
		
		instance = null;
    }
    
    /**
     * @return true if instance of GameService exists. 
     */
    public static final boolean isRunning() {
		return running;
	}
    
    /**
     * Returns last saved location of user.
     */
    public final Location getLocation() {
    	return location;
    }
    
    /**
     * Returns actual {@link Scenario}.
     */
    public final Scenario getScenario() {
    	return scenario;
    }
    
	/**
	 * Calculates time between start of game and present.
	 * @return time in milliseconds
	 */
	protected long calcGameTime() {
		return SystemClock.uptimeMillis() - start;
	}
    
    /**
     * Returns time when game started.
     * @return
     */
    public final long getStartTime() {
    	return start;
    }
    
    /**
     * Returns actual game time.
     */
    public final long getTime() {
    	return time;
    }
    
    /**
     * Returns actual status of game.
     */
    public final Status getStatus() {
    	return status;
    }
    
    /**
     * Returns instance of this game.
     */
    public static final GameService getInstance() {
    	return instance;
    }
	
	/**
	 * Gets and broadcasts new user location.
	 */
    @Override
    public final void onLocationChanged(Location location) {
    	this.location = location;
    	gameHandler.broadcastEvent(GameEvent.UPDATED_LOCATION);    	    	
    }
	
	@Override public final void onProviderDisabled(String provider) {}
	@Override public final void onProviderEnabled(String provider) {}
	@Override public final void onStatusChanged(String provider, int status, Bundle extras) {}
	
	/**
	 * Returns game notification to show when game is running.
	 */
	protected abstract Notification getGameNotification();
	
	/**
	 * This is called after receiving new startService intent.
	 * @param starting means that {@link GameService} was just started for first time (true)
	 * or that new intent was received when game is running (false).
	 * @param intent which was used when starting this service
	 * @see #startService(Intent)
	 */
	protected abstract void onGameStart(boolean starting, Intent intent);
	
	/**
	 * This method is called every time there is new event received.
	 * @param event
	 */
	protected abstract void onEvent(GameEvent event);
	
	/**
	 * Show or update this service's notification.
	 * This method uses {@link #getGameNotification()} to get notification.
	 */
	public final void refreshNotification() {
		startForeground(Constants.NOTIFICATION_GAMEPLAY, getGameNotification());
	}

	/**
	 * Received game event handling.
	 */
    @Override
	public final void receiveEvent(GameEvent event) {
    	
    	switch (event) {
    	case GAME_QUIT:
    		stopSelf();
    		break;
    	
    	case GAME_START:
    		break;
    		
    	case GAME_RESUME:
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    		break;
    	
    	case GAME_PAUSE:
    	case GAME_WIN:
    	case GAME_LOSE:
   	  		break;
    	  
    	case UPDATED_LOCATION:
    		if (location != null)
    			scenario.onLocationUpdate(location.getLatitude(), location.getLongitude());
    		refreshNotification();
    		break;
    		
    	case UPDATED_TIME:
    		scenario.onTimeUpdate(time);
    		refreshNotification();
    		break;
    	}
    	
    	onEvent(event);
	}

}
