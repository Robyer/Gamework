package cz.robyer.gw_example.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.robyer.gw_example.R;
import cz.robyer.gw_example.util.IntentFactory;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEventListener;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioInfo;
import cz.robyer.gamework.scenario.reaction.Reaction;

/**
 * Activity only for testing and debug different features.
 * TODO: this activity shouldn't be in final application.
 * @author Robert Pösel
 */
public class TestingActivity extends Activity implements GameEventListener {
	private static final String TAG = TestingActivity.class.getSimpleName();
	private TextView myLatitude, myLongitude, time_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
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
	
	private void initButtons() {
		// start/stop timer
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
