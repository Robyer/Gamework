package cz.robyer.gw_example.game;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.util.Log;
import cz.robyer.gw_example.R;
import cz.robyer.gw_example.activity.TestingActivity;

public class GameService extends cz.robyer.gamework.game.GameService {

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.game.GameService#getGameNotification()
	 */
	@Override
	protected Notification getGameNotification() {
		// Creates an explicit intent for an Activity in your app
    	Intent notificationIntent = new Intent(this, TestingActivity.class) // TODO: GameActivity
    			.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    	Location loc = getLocation();
    	
    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//    			.setOnlyAlertOnce(true)
//    			.setOngoing(true)
    			.setWhen(getStartTime())
    	        .setSmallIcon(R.drawable.ic_stat_game)
    	        .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
    	        .setContentTitle("Gamework - playing")    	        
    	        .setStyle(new NotificationCompat.BigTextStyle()
    	        				.bigText(String.format(
    	        	        			"Game time: %s\nGame location: %s, %s",
    	        	        			getTime()/1000,
    	        	        			loc != null ? loc.getLatitude() : "-",
    	        	        			loc != null ? loc.getLongitude() : "-"
    	        	    	        ))
//								.setBigContentTitle(title)
								.setSummaryText("Game is running"));
    	
    	return mBuilder.build();
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.game.GameService#onGameStarted()
	 */
	@Override
	protected void onGameStart(boolean starting, Intent intent) {
    	Log.i("MyGameService", "onGameStart(" + starting + ")");
		if (starting) {
    		Intent gameIntent = new Intent(this, GameMapActivity.class);
    		gameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(gameIntent);
    	} else {
    		Toast.makeText(getApplicationContext(), "Game is already running.", Toast.LENGTH_LONG).show();
    	}
	}

	@Override
	protected void onEvent(GameEvent event) {
    	
    	switch (event) {
    	case GAME_QUIT:
    	case GAME_START:
    	case GAME_RESUME:
    	case GAME_PAUSE:
    	case GAME_WIN:
    	case GAME_LOSE:
   	  		break;
    	  
    	case UPDATED_LOCATION:
    	case UPDATED_TIME:
    		refreshNotification(false);
    		break;
    	}

	}

}
