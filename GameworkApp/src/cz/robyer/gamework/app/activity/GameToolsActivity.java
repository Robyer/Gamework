package cz.robyer.gamework.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.ScenarioInfo;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.app.R;

/**
 * Represents global settings of application.
 * @author Robert Pösel
 */
public class GameToolsActivity extends BaseGameActivity {
	//private static final String TAG = GameToolsActivity.class.getSimpleName();
	private TextView myLatitude, myLongitude, time_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_settings);
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
	
	private void updateLocation() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (GameService.isRunning()) {
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
				if (GameService.isRunning()) {					
					long time = getGame().getTime();
					int seconds = (int) (time / 1000);
					int minutes = seconds / 60;
					seconds     = seconds % 60;

					time_text.setText(String.format("%d:%02d", minutes, seconds));
				}
			}
		});		
	}
	
	public void showNotRunning() {
		Toast.makeText(GameToolsActivity.this, "Game is not running.", Toast.LENGTH_LONG).show();
	}
	
	protected void initButtons() {
		super.initButtons();
		
		final GameService game = getGame();
		
		((Button)findViewById(R.id.btn_info)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (game != null && game.getScenario() == null) {
					showNotRunning();
					return;
				}
				
				ScenarioInfo info = game.getScenario().getInfo();
				
				AlertDialog ad = new AlertDialog.Builder(GameToolsActivity.this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				//ad.setIcon(android.R.drawable.ic_dialog_info);

				ad.setTitle(info.title);
				ad.setMessage(getResources().getString(
						R.string.scenario_info, 
							info.author,
							info.version,
							info.location,
							info.duration,
							info.difficulty,
							info.description
						));
				
				ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {  
				    @Override  
				    public void onClick(DialogInterface dialog, int which) {  
				        dialog.dismiss();
				    }  
				});
				ad.show();
			}	
		});
		
		((Button)findViewById(R.id.btn_reaction)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (game != null && game.getScenario() == null) {
					showNotRunning();
					return;
				}
				
				String value = ((EditText)findViewById(R.id.edit_reaction)).getText().toString();
				Reaction reaction = game.getScenario().getReaction(value);
				if (reaction != null)
					reaction.action();
				else
					Toast.makeText(GameToolsActivity.this, "This reaction doesn't exists.", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void receiveEvent(GameEvent event) {
		super.receiveEvent(event);
		
		switch (event.type) {
		case UPDATED_TIME:
			updateTime();
			break;
		case UPDATED_LOCATION:
			updateLocation();
			break;
		default:
			break;
		}			
	}
	
}
