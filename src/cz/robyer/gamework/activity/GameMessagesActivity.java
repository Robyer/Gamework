package cz.robyer.gamework.activity;

import android.os.Bundle;
import cz.robyer.gamework.R;

public class GameMessagesActivity extends BaseGameActivity {
	private static final String TAG = GameMessagesActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_messages);
		super.initButtons();
	}

}
