package cz.robyer.gamework.activity;

import android.os.Bundle;
import cz.robyer.gamework.R;

public class GameInventoryActivity extends BaseGameActivity {
	private static final String TAG = GameInventoryActivity.class.getSimpleName();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_inventory);
		super.initButtons();
	}

}
