package cz.robyer.gw_example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameEventListener;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gw_example.R;
import cz.robyer.gw_example.util.IntentFactory;

/**
 * This is base activity for all game activities.
 * @author Robert Pösel
 */
public abstract class BaseGameActivity extends BaseActivity implements GameEventListener {
	private static final String TAG = BaseGameActivity.class.getSimpleName();
	
	/** Helper for quitting activity by second press of 'back' */
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
		overridePendingTransition(0,0); // disable animations

		/** Register listening to game events */
		getGame().registerListener(this);			

		// reset 'back' clicked helper
		backClicked = false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		/** Unregister listening to game events */
		if (GameService.isRunning())
			getGame().unregisterListener(this);		
	}
	
	/**
	 * If {@link GameService} is not running then finish this activity and start MainActivity.
	 */
	protected void checkGameRunning() {
		if (!GameService.isRunning()) {
			Log.w(TAG, "Game is not running, quitting activity");
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

	/**
	 * Init buttons bar and handlers for buttons.
	 */
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
					cls = GameObjectivesActivity.class;
				else if (v.getId() == R.id.btn_inventory)
					cls = GameInventoryActivity.class;
				else if (v.getId() == R.id.btn_settings)
					cls = GameSettingsActivity.class;
				
				// ignore unknown buttons and link to same activity
				if (cls == null || cls == this.getClass())
					return;
				
				Intent intent = new Intent(BaseGameActivity.this, cls);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)		
						.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					
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
	
	/* (non-Javadoc)
	 * @see cz.robyer.gw_example.game.GameEventListener#receiveEvent(cz.robyer.gw_example.game.GameEvent)
	 */
	@Override
	public void receiveEvent(GameEvent event) {
		// TODO: think up and implement
		switch (event) {
		case GAME_LOSE:
		case GAME_PAUSE:
		case GAME_QUIT:
		case GAME_START:
		case GAME_WIN:
		case UPDATED_LOCATION:
		case UPDATED_TIME:
			break;
		}
	}
	
	/**
	 * Shortcut for getting instance of {@link GameService}.
	 * @return instance
	 */
	protected GameService getGame() {
		return GameService.getInstance();
	}
	
	/**
	 * Quit application on second 'back' press.
	 */
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
