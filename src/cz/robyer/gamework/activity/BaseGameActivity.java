package cz.robyer.gamework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.GameEventListener;
import cz.robyer.gamework.GameService;
import cz.robyer.gamework.R;
import cz.robyer.gamework.util.IntentFactory;

public abstract class BaseGameActivity extends FragmentActivity implements GameEventListener {
	private static final String TAG = BaseGameActivity.class.getSimpleName();
	
	protected boolean backClicked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkGameRunning();
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		checkGameRunning();
		overridePendingTransition(0,0);
		
		backClicked = false;		
	}
	
	protected void checkGameRunning() {
		if (!GameService.running) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish(); // TODO: Is this okay to be here? Or should we use completely different approach?
		}
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}*/

	protected void initButtons() {

		final OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("BaseGameActivity", "onClick buttons bar");
				Class<?> cls = null;
				if (v.getId() == R.id.btn_map)
					cls = GameMapActivity.class;
				else if (v.getId() == R.id.btn_messages)
					cls = GameMessagesActivity.class;
				else if (v.getId() == R.id.btn_tasks)
					cls = GameTasksActivity.class;
				else if (v.getId() == R.id.btn_inventory)
					cls = GameInventoryActivity.class;
				else if (v.getId() == R.id.btn_settings)
					cls = GameSettingsActivity.class;
				
				// ignore unknown buttons or same activity
				if (cls == null || cls == this.getClass())
					return;
				
				Intent intent = new Intent(getApplicationContext(), cls);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) // TODO: hope this works as i want		
						.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // should be cool
					;
					
				startActivity(intent);
			}
		};
		
		View buttons = findViewById(R.id.buttonsbar);
		if (buttons != null) {
			buttons.findViewById(R.id.btn_map).setOnClickListener(listener);
			buttons.findViewById(R.id.btn_messages).setOnClickListener(listener);
			buttons.findViewById(R.id.btn_tasks).setOnClickListener(listener);
			buttons.findViewById(R.id.btn_inventory).setOnClickListener(listener);
			buttons.findViewById(R.id.btn_settings).setOnClickListener(listener);
		}
		
	}
	
	@Override
	public void receiveEvent(GameEvent event) {
		// TODO: think up and implement
		switch (event) {
		case GAME_LOSE:
		case GAME_PAUSE:
		case GAME_QUIT:
		case GAME_RESUME:
		case GAME_START:
		case GAME_WIN:
		case UPDATED_LOCATION:
		case UPDATED_TIME:
			break;
		}
	}
	
	protected GameService getGame() {
		return GameService.getInstance();
	}
	
	@Override
	public void onBackPressed() {			
		if (!backClicked) {
			backClicked = true;
			Toast.makeText(getApplicationContext(), R.string.press_back_again_to_quit, Toast.LENGTH_LONG).show();			
			return;
		}

		stopService(IntentFactory.createGameServiceIntent(getApplicationContext()));
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		
		//super.onBackPressed();
	}

}
