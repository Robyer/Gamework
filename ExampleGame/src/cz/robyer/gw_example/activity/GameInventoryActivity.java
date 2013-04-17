package cz.robyer.gw_example.activity;

import android.os.Bundle;
import cz.robyer.gw_example.R;

/**
 * Represents game inventory with 'tools' like QR scanner
 * @author Robert Pösel
 */
public class GameInventoryActivity extends BaseGameActivity {
	//private static final String TAG = GameInventoryActivity.class.getSimpleName();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_inventory);
		initButtons();
	}

}
