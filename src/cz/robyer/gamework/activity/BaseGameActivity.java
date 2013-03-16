package cz.robyer.gamework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.GameEventListener;
import cz.robyer.gamework.GameService;
import cz.robyer.gamework.R;

public abstract class BaseGameActivity extends BaseActivity implements GameEventListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!GameService.running) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish(); // TODO: Is this okay to be here? Or should we use completely different approach?
		}
		
		initButtons();
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}*/

	private void initButtons() {

		final OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
				
				Intent intent = new Intent(getParent(), cls);
				startActivity(intent);
			}
		};
		
		findViewById(R.id.btn_map).setOnClickListener(listener);
		findViewById(R.id.btn_messages).setOnClickListener(listener);
		findViewById(R.id.btn_tasks).setOnClickListener(listener);
		findViewById(R.id.btn_inventory).setOnClickListener(listener);
		findViewById(R.id.btn_settings).setOnClickListener(listener);
		
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

}
