package cz.robyer.gamework.activity;

import android.os.Bundle;
import cz.robyer.gamework.R;

/**
 * Represents global settings of application.
 * @author Robert Pösel
 */
public class GameSettingsActivity extends BaseGameActivity {
	private static final String TAG = GameSettingsActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_settings);
		super.initButtons();
	}
}
