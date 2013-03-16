package cz.robyer.gamework.activity;

import android.os.Bundle;
import cz.robyer.gamework.GameEvent;
import cz.robyer.gamework.R;

public class GameMapActivity extends BaseGameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_map);
	}

	public void receiveEvent(GameEvent event) {
		super.receiveEvent(event);
		
		switch (event) {
		case UPDATED_LOCATION:
			// TODO: update position of user on map
	    	//myLatitude.setText(String.valueOf(location.getLatitude()));
			//myLongitude.setText(String.valueOf(location.getLongitude()));    	
			break;
		}
	}
		
}
