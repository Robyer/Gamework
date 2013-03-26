 package cz.robyer.gamework.game;

import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import cz.robyer.gamework.R;
import cz.robyer.gamework.activity.GameMapActivity;
import cz.robyer.gamework.activity.TestingActivity;
import cz.robyer.gamework.constants.Constants;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioParser;
import cz.robyer.gamework.util.Log;

public class GameService extends Service implements GameEventListener, LocationListener {		
	private static final String TAG = GameService.class.getSimpleName();
	
	public static boolean running; // signalizing that instance of GameService exists
	private static GameService instance;
	
	private GameHandler gameHandler = new GameHandler();
	
	private Scenario scenario;
	Status status = Status.GAME_STOPPED;
	
	private Timer timer; // http://stackoverflow.com/questions/4597690/android-timer-how
	private long time;
	private long start;
	private Location location;
	
	private LocationManager locationManager;
	
	protected enum Status {
		GAME_STOPPED, 	// game has not started yet
		GAME_LOADING, 	// game is loading needed resources
		GAME_WAITING,	// game is waiting for starting event (player must stand on particular location, etc.) 
		GAME_RUNNING,	// game was started and is running 
		GAME_FINISHED,	// game was completed (either won or failed)
	};
	
    private final TimerTask timerTask = new TimerTask() {		
		@Override
		public void run() {
			// TODO: when game will be pausable, this need to be improved (gameTime must being incremented)
			time = SystemClock.uptimeMillis() - start;
			gameHandler.broadcastEvent(GameEvent.UPDATED_TIME);
		}
	};
	
	public boolean registerListener(GameEventListener listener) {
    	return gameHandler.addListener(listener);
    }
    
    public boolean unregisterListener(GameEventListener listener) {
    	return gameHandler.removeListener(listener);
    }
    
    @Override
    public void onCreate() {
        // The service is being created    	
    	Log.i(TAG, "onCreate()");
    	
    	instance = this;
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
      
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.i(TAG, "onStartCommand()");
    	
    	if (running) {
    		// game is already running, do nothing
    		Log.i(TAG, "Game is already running");
    		Toast.makeText(getApplicationContext(), "Game is already running.", Toast.LENGTH_LONG).show();
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
    	timer.schedule(timerTask, 1000, 1000);
    	    	
    	showForegroundNotification(true);
    	
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    	
		// TODO: have this here? Maybe rather no, as we want proper location at start of game {
		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if (location == null)
    		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	if (location != null)
    		gameHandler.broadcastEvent(GameEvent.UPDATED_LOCATION);
    	// }
    	
    	Intent gameIntent = new Intent(this, GameMapActivity.class);
    	gameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(gameIntent);    	
    	
    	// The service is starting, due to a call to startService()
        return START_NOT_STICKY; // or START_REDELIVER_INTENT?
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	Log.i(TAG, "onBind()");
    	return null; // we don't support binding
    }

    @Override
    public void onDestroy() {
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
    
    public Location getLocation() {
    	return location;
    }
    
    public Scenario getScenario() {
    	return scenario;
    }
    
    public long getTime() {
    	return time;
    }
    
    public Status getStatus() {
    	return status;
    }
    
    public static GameService getInstance() {
    	return instance;
    }
	
	@Override
    public void onLocationChanged(Location location) {
    	this.location = location;

    	gameHandler.broadcastEvent(GameEvent.UPDATED_LOCATION);    	    	
    }
	
	@Override public void onProviderDisabled(String provider) {}
	@Override public void onProviderEnabled(String provider) {}
	@Override public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	private void showForegroundNotification(boolean foreground) {
    	// Creates an explicit intent for an Activity in your app
    	Intent notificationIntent = new Intent(this, TestingActivity.class) // TODO: GameActivity
    			.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//    			.setOnlyAlertOnce(true)
//    			.setOngoing(true)
    			.setWhen(start)
    	        .setSmallIcon(R.drawable.ic_stat_game)
    	        .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
    	        .setContentTitle("Gamework - playing")    	        
    	        .setStyle(new NotificationCompat.BigTextStyle()
    	        				.bigText(String.format(
    	        	        			"Game time: %s\nGame location: %s, %s",
    	        	        			time/1000,
    	        	        			location != null ? location.getLatitude() : "-",
    	        	        			location != null ? location.getLongitude() : "-"
    	        	    	        ))
//								.setBigContentTitle(title)
								.setSummaryText("Game is running"));
    	
    	if (foreground)
    		startForeground(Constants.NOTIFICATION_GAMEPLAY, mBuilder.build());
    	else {
    		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   			mNotificationManager.notify(Constants.NOTIFICATION_GAMEPLAY, mBuilder.build());
    	}
    		
	}
	
    @Override
	public void receiveEvent(GameEvent event) {
    	
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
    		showForegroundNotification(false);
    		break;
    		
    	case UPDATED_TIME:
    		scenario.onTimeUpdate(time);
    		showForegroundNotification(false);
    		break;
    	}

	}

}
