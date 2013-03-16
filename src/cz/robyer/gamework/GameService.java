package cz.robyer.gamework;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

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
import cz.robyer.gamework.activity.TestingActivity;
import cz.robyer.gamework.constants.Constants;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioParser;
import cz.robyer.gamework.util.Log;

public class GameService extends Service implements GameEventListener, LocationListener {
	public static boolean running;
	
	private GameEventHandler eventHandler = new GameEventHandler();
	
	private Scenario scenario;
	Status status = Status.GAME_STOPPED;
	
	private Timer timer;
	private long time;
	private long start;
	private static Location location;
	
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
			eventHandler.broadcastEvent(GameEvent.UPDATED_TIME);
		}
	};
    
    @Override
    public void onCreate() {
        // The service is being created    	
    	Log.d("GameService", "onCreate()");
    	
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if (running) {
    		// game is already running, do nothing
    		return 0; // TODO: maybe different code... and... just do something different :(
    	}
    	
    	Log.d("GameService", "onStartCommand()");
    	
    	String filename = intent.getStringExtra("filename");
		try {
			ScenarioParser parser = new ScenarioParser(getApplicationContext(), false);
			InputStream file = getAssets().open(filename);
			scenario = parser.parse(file, false);
			file.close();
		} catch (Exception e) {
			Log.e("GameService", e.getMessage(), e);
		}
		
    	
    	running = true;
    	eventHandler.addListener(this);
    	start = SystemClock.uptimeMillis();
    		// system time clock: System.currentTimeMillis()
    		// uptime clock (not ticking in deep sleep): SystemClock.uptimeMillis();
    	
    	timer = new Timer();
    	timer.schedule(timerTask, 1000, 1000);
    	
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_stat_game)
    	        .setContentTitle("Gamework - playing")
    	        .setContentText("Hello World!");
    	
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(this, TestingActivity.class); // TODO: GameActivity

    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
/*    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(TestingActivity.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
*/    	
    	PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
    	
    	mBuilder.setContentIntent(resultPendingIntent);
    	/*NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(mId, mBuilder.build());*/
    	
    	startForeground(Constants.NOTIFICATION_GAMEPLAY, mBuilder.build());
    	
    	
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    	
		// TODO: have this here? Maybe rather no, as we want proper location at start of game {
		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if (location == null)
    		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	if (location != null)
    		eventHandler.broadcastEvent(GameEvent.UPDATED_LOCATION);
    	// }
    	
    	// The service is starting, due to a call to startService()
        return START_NOT_STICKY; // or START_REDELIVER_INTENT?
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	Log.d("GameService", "onBind()");
    	return null; // we dont support binding
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    	Log.d("GameService", "onDestroy()");    	    	
    	
    	running = false;
    	eventHandler.clearListeners();
    	
		timer.cancel();
		timer.purge();
		timer = null;
		
		locationManager.removeUpdates(this);
		location = null;
    }
	
	@Override
    public void onLocationChanged(Location location) {
    	GameService.location = location;

    	eventHandler.broadcastEvent(GameEvent.UPDATED_LOCATION);    	    	
    }
	
	@Override public void onProviderDisabled(String provider) {}
	@Override public void onProviderEnabled(String provider) {}
	@Override public void onStatusChanged(String provider, int status, Bundle extras) {}

	
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
    		break;
    		
    	case UPDATED_TIME:
    		scenario.onTimeUpdate(time);
    		break;
    	}

	}

}
