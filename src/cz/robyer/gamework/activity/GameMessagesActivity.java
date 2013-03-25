package cz.robyer.gamework.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.robyer.gamework.R;
import cz.robyer.gamework.scenario.message.Message;

public class GameMessagesActivity extends BaseGameActivity {
	private static final String TAG = GameMessagesActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_messages);
		super.initButtons();
		
		ListView list = (ListView)findViewById(R.id.list_messages);
			
		ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getGame().getScenario().getVisibleMessages());

		list.setAdapter(adapter); 
		
		refreshList();
	}
	
	public void refreshList() {
		ListView list = (ListView)findViewById(R.id.list_messages);
		
		
	}

}
