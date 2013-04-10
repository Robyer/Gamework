package cz.robyer.gw_example.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cz.robyer.gw_example.R;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.scenario.message.Message;

/**
 * Represents list of game messages.
 * @author Robert Pösel
 */
public class GameMessagesActivity extends BaseGameActivity {
	private static final String TAG = GameMessagesActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_messages);
		super.initButtons();
		
		ListView list = (ListView)findViewById(R.id.list_messages);
			
		final List<Message> messages = getGame().getScenario().getVisibleMessages();
		
		ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, android.R.id.text1, messages);

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(GameMessagesActivity.this, MessageActivity.class);
				
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + position + ", id " + id, Toast.LENGTH_LONG).show();
			}
		});
		
		refreshList();
	}
	
	/**
	 * Refreshes list of messages.
	 */
	public void refreshList() {
		ListView list = (ListView)findViewById(R.id.list_messages);
	}
	
	/**
	 * Checks UPDATED_MESSAGES event and updates game messages list.
	 */
	@Override
	public void receiveEvent(GameEvent event) {
		// TODO: think up and implement
		switch (event) {
		case UPDATED_MESSAGES:
			refreshList();
			break;
		}
		super.receiveEvent(event);
	}

}
