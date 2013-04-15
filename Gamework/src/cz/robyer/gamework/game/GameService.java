package cz.robyer.gamework.game;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
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
	
	private Scenario scenario;
	private GameStatus status = GameStatus.GAME_NONE;
	protected final GameHandler gameHandler = new GameHandler();
	
	private Timer timer;
	private long start, time;
	
	private LocationManager locationManager;
	private Location location;
	
	/** help variable for incrementing time value. */
	private long lastTime;
	
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
    	running = true;
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
      
    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
    	Log.i(TAG, "onStartCommand()");

		// TODO: allow starting new game also when in GAME_WON, GAME_LOST status?
    	if (status != GameStatus.GAME_NONE) {
    		// game is already running, do nothing
    		Log.i(TAG, "Game is already running");
    		// inform about game was already started
    		onGameStart(false, intent);
    		return 0;    		
    	}
    	
    	status = GameStatus.GAME_LOADING;
    	String filename = intent.getStringExtra("filename");
    	scenario = ScenarioParser.fromAsset(getApplicationContext(), filename);
    	if (scenario == null) {
    		Log.e(TAG, "Scenario '" + filename + "' wasn't loaded");
    		status = GameStatus.GAME_NONE;
    		return 0;
    	}
    	
    	Log.i(TAG, "Scenario '" + filename + "' was loaded");

    	scenario.setHandler(gameHandler);
    	registerListener(this);
    	
    	start = lastTime = SystemClock.uptimeMillis();
    	time = 0;
    	
    	status = GameStatus.GAME_WAITING;
    	    	
    	// make this service foreground and show notification
    	startForeground(Constants.NOTIFICATION_GAMEPLAY, getGameNotification());
    	
    	// register getting location updates
    	startLocationUpdates();
		
    	// inform about game starting
		onGameStart(true, intent);
    	
    	// service is starting, due to a call to startService()
        return START_NOT_STICKY; // or START_REDELIVER_INTENT?
    }
    
    @Override
    public final IBinder onBind(Intent intent) {
    	Log.i(TAG, "onBind()");
    	return null; // we don't support binding
    }

    @Override
    public final void onDestroy() {
        // service is no longer used and is being destroyed
    	Log.i(TAG, "onDestroy()");
    	
    	running = false;
    	instance = null;
    	
    	gameHandler.clearListeners();
    	stopAllUpdates();
    }
    
    /**
     * Returns info whether instance of service exists or not.
     * @return true if instance of GameService exists, false otherwise.
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
    public final GameStatus getStatus() {
    	return status;
    }
    
    /**
     * Returns instance of this game.
     */
    public static final GameService getInstance() {
    	return instance;
    }
    
    /**
	 * Calculate and update actual game time.
	 */
	private final void updateGameTime() {
		long now = SystemClock.uptimeMillis();
		time += now - lastTime;
		lastTime = now;
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
	 * This method is called every time when there was new event received.
	 * @param event
	 */
	protected abstract void onEvent(GameEvent event);
	
	/**
	 * Show or update this service's notification.
	 * This method uses {@link #getGameNotification()} to get notification.
	 */
	public final void refreshNotification(boolean foreground) {
		if (foreground) {
			startForeground(Constants.NOTIFICATION_GAMEPLAY, getGameNotification());
		} else {
    		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   			mNotificationManager.notify(Constants.NOTIFICATION_GAMEPLAY, getGameNotification());
    	}
	}
	
	/**
	 * Starts requesting location updates.
	 */
	private final void startLocationUpdates() {
		if (locationManager != null) {    		
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    	}
	}
	
	/**
	 * Creates and starts timer (if not exists already) for getting time updates.
	 */
	private final void startTimeUpdates() {
		if (timer == null) {
    		timer = new Timer();
        	timer.schedule(new TimerTask() {
        		@Override
        		public void run() {
        			updateGameTime();
        			gameHandler.broadcastEvent(GameEvent.UPDATED_TIME);
        		}
        	}, 1000, 1000);
		}
	}
	
	/**
	 * Stops location and time updates.
	 */
	private final void stopAllUpdates() {
		if (locationManager != null)
			locationManager.removeUpdates(this);
		
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	/**
	 * Received game event handling.
	 */
    @Override
	public final void receiveEvent(GameEvent event) {
    	
    	switch (event) {
    	case GAME_START:
    		if (status != GameStatus.GAME_WAITING && status != GameStatus.GAME_PAUSED) {
    			Log.w(TAG, "Game can't be started in '" + status + "' state. Only GAME_WAITING and GAME_PAUSED allowed");
    			break;
    		}
    		lastTime = SystemClock.uptimeMillis();
    		
    		startTimeUpdates();
    			
        	// requesting location updates was already done if game service is in waiting state
        	if (status != GameStatus.GAME_WAITING)
        		startLocationUpdates();
        	
        	status = GameStatus.GAME_RUNNING;
    		break;
    	
    	case GAME_PAUSE:
    	case GAME_WIN:
    	case GAME_LOSE:
    		if (status != GameStatus.GAME_RUNNING) {
    			Log.w(TAG, "Game can't be paused/won/lost in '" + status + "' state. Only GAME_RUNNING allowed");
    			break;
    		}    		
    		stopAllUpdates();

    		if (event == GameEvent.GAME_WIN)
    			status = GameStatus.GAME_WON;
    		else if (event == GameEvent.GAME_LOSE)
    			status = GameStatus.GAME_LOST;
    		else
    			status = GameStatus.GAME_PAUSED;
    		
   	  		break;
   	  		
    	case GAME_QUIT:
    		stopSelf();
    		status = GameStatus.GAME_NONE;
    		break;
   	  		
    	case UPDATED_LOCATION:
    		if (status == GameStatus.GAME_RUNNING && location != null)
    			scenario.onLocationUpdate(location.getLatitude(), location.getLongitude());
    		break;
    		
    	case UPDATED_TIME:
    		if (status == GameStatus.GAME_RUNNING)
    			scenario.onTimeUpdate(time);
    		break;
    	}
    	
    	onEvent(event);
	}

}
