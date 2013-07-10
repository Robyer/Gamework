package cz.robyer.gamework.app.game;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEvent.EventType;
import cz.robyer.gamework.game.GameStatus;
import cz.robyer.gamework.app.R;
import cz.robyer.gamework.app.activity.GameMapActivity;

public class GameService extends cz.robyer.gamework.game.GameService {

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.game.GameService#getGameNotification()
	 */
	@SuppressLint("DefaultLocale")
	@Override
	protected Notification getGameNotification() {
    	Intent notificationIntent = new Intent(this, GameMapActivity.class)
    			.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    	// location
    	Location loc = getLocation();
    	String gps = "-";
    	if (loc != null)
    		gps = String.format("%s, %s", loc.getLatitude(), loc.getLongitude());
    	
		// time
    	int seconds = (int) (getTime() / 1000);
		int minutes = seconds / 60;
		seconds     = seconds % 60;
		String time = String.format("%d:%02d", minutes, seconds);
		
    	String summary = null;
    	String text = null;
    	
    	switch (getStatus()) {
    	case GAME_LOADING:
    		summary = "Loading...";
    		break;
    	case GAME_LOST:
    		summary = "You lost this game!";
    		break;
    	case GAME_NONE:
    		summary = "No game loaded";
    		break;
    	case GAME_PAUSED:
    		summary = "Game is paused";
    		break;
    	case GAME_RUNNING:
    		summary = "Game is running";
    		text = String.format("Game time: %s\nGame location: %s", time, gps);
    		break;
    	case GAME_WAITING:
    		summary = "Waiting for start...";
    		break;
    	case GAME_WON:
    		summary = "You won this game!";
    		break;
    	}

    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
    			.setWhen(getStartTime())
    	        .setSmallIcon(R.drawable.ic_stat_game)
    	        .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
    	        .setContentTitle(getScenario().getInfo().title)
    	        .setContentText(summary);
    	
    	if (text != null)
    		mBuilder.setStyle(new NotificationCompat.BigTextStyle()
    	        .bigText(text)
				.setSummaryText(summary));
    	
    	return mBuilder.build();
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.game.GameService#onGameStart(boolean, android.content.Intent)
	 */
	@Override
	protected void onGameStart(boolean starting, Intent intent) {
		if (starting) {
    		Intent gameIntent = new Intent(this, GameMapActivity.class);
    		gameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(gameIntent);
    	} else {
    		Toast.makeText(getApplicationContext(), "Game is already running.", Toast.LENGTH_LONG).show();
    	}
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.game.GameService#onEvent(cz.robyer.gamework.game.GameEvent)
	 */
	@Override
	protected void onEvent(GameEvent event) {

    	switch (event.type) {
    	case UPDATED_LOCATION:
   			if (getStatus() == GameStatus.GAME_WAITING) {
   				gameHandler.broadcastEvent(EventType.GAME_START);
   			}
   			
    	case GAME_QUIT:
    	case GAME_START:
    	case GAME_PAUSE:
    	case GAME_WIN:
    	case GAME_LOSE:
    	case UPDATED_TIME:
    		refreshNotification(false);
    		break;

    	default:
    		break;
    	}

	}

}
