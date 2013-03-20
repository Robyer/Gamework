package cz.robyer.gamework.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.GameEventListener;
import cz.robyer.gamework.GameService;
import cz.robyer.gamework.R;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.util.IntentFactory;

public class TestingActivity extends Activity implements GameEventListener {
	private static final String TAG = TestingActivity.class.getSimpleName();
	private TextView myLatitude, myLongitude, time_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);

		// Show the Up button in the action bar.
		setupActionBar();
			
		initButtons();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		// GPS location
		myLatitude = (TextView)findViewById(R.id.Latitude);
		myLongitude = (TextView)findViewById(R.id.Longitude);
		time_text = (TextView)findViewById(R.id.edit_timestamp);
		
		updateLocation();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (GameService.running)
			getGame().registerListener(this);		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (GameService.running)
			getGame().unregisterListener(this);		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		//myLatitude = myLongitude = time_text = null;
	}
	
	private void updateLocation() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (GameService.running) {
					Location loc = getGame().getLocation();
					if (loc != null) {				
						myLatitude.setText(String.valueOf(loc.getLatitude()) + " (" + loc.getProvider() + ")");
						myLongitude.setText(String.valueOf(loc.getLongitude()) + " (" + loc.getProvider() + ")");
					}		
				}
			}
		});
	}
	
	private void updateTime() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (GameService.running) {					
					long time = getGame().getTime();
					int seconds = (int) (time / 1000);
					int minutes = seconds / 60;
					seconds     = seconds % 60;

					time_text.setText(String.format("%d:%02d", minutes, seconds));
				}
			}
		});		
	}
	
	private GameService getGame() {
		return GameService.getInstance();
	}
	
	private void showNotification(int id, String title, String content) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(content);
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, TestingActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(TestingActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(id, mBuilder.build());
	}
	
	private void hideNotification(int id) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(id);
	}
	
	private void initButtons() {
		// Start/Stop timer
		Button btn_timer = (Button)findViewById(R.id.btn_game);
		
		btn_timer.setText(GameService.running ? "Stop game" : "Start game");
		
		btn_timer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button b = (Button)v;
                if (b.getText().equals("Stop game")){
                	b.setText("Start game");
                	stopService(IntentFactory.createGameServiceIntent(getApplicationContext()));                    
                } else {
                	ProgressDialog.show(TestingActivity.this, "", "Loading. Please wait...", true, true);

                	String filename = ((EditText)findViewById(R.id.edit_filename)).getText().toString();
                	startService(IntentFactory.createGameServiceIntent(getApplicationContext(), filename));
                	
                	/*AlertDialog ad = new AlertDialog.Builder(TestingActivity.this).create();
					ad.setCancelable(false); // This blocks the 'BACK' button
					//ad.setIcon(android.R.drawable.ic_dialog_info);
					ad.setTitle(scenario.getInfo().getTitle());
					ad.setMessage(scenario.getDescription());
					ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {  
					    @Override  
					    public void onClick(DialogInterface dialog, int which) {  
					        dialog.dismiss();
					    }  
					});
					
					//dialog.hide();
					ad.show();*/
                }
			}	
		});
		
		((Button)findViewById(R.id.btn_reaction)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String value = ((EditText)findViewById(R.id.edit_reaction)).getText().toString();
				GameService game = getGame();
				if (game != null) {
					Scenario scenario = game.getScenario();
					if (scenario != null) {
						Reaction reaction = scenario.getReaction(value);
						if (reaction != null)
							reaction.action();
						else
							Toast.makeText(TestingActivity.this, "This reaction doesn't exists.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(TestingActivity.this, "Game is not running.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void receiveEvent(GameEvent event) {
		switch (event) {
		case UPDATED_TIME:
			updateTime();
			break;
		case UPDATED_LOCATION:
			updateLocation();
			break;
		}			
	}

}
