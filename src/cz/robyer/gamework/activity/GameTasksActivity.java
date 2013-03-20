package cz.robyer.gamework.activity;

import android.os.Bundle;
import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.R;

public class GameTasksActivity extends BaseGameActivity {
	private static final String TAG = GameTasksActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_tasks);
		super.initButtons();
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
		super.receiveEvent(event);
	}

}
