package cz.robyer.gw_example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cz.robyer.gamework.game.GameService;
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
	
	protected void initButtons() {
		super.initButtons();

		((Button)findViewById(R.id.btn_scan_qr)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GameService.isRunning())
					getGame().startScanner(GameInventoryActivity.this);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (GameService.isRunning()) {
    		String message;
    		if (getGame().onActivityResult(requestCode, resultCode, data))
    			message = "Code was scanned!";
    		else
    			message = "Error when scanning code!";
    		
    		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    	}
    }

}
