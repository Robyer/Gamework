package cz.robyer.gw_example.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gw_example.R;

/**
 * Represents list of game messages.
 * @author Robert Pösel
 */
public class GameMessagesActivity extends BaseGameActivity {
	private static final String TAG = GameMessagesActivity.class.getSimpleName();

	ArrayAdapter<Message> adapter;
	List<Message> messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_messages);
		super.initButtons();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		ListView list = (ListView)findViewById(R.id.list_messages);
		
		messages = getGame().getScenario().getVisibleMessages();
		adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, android.R.id.text1, messages);
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(GameMessagesActivity.this, MessageActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Refreshes list of messages.
	 */
	public void refreshList() {
		messages = getGame().getScenario().getVisibleMessages();
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Checks UPDATED_MESSAGES event and updates game messages list.
	 */
	@Override
	public void receiveEvent(GameEvent event) {
		// TODO: think up and implement
		switch (event.type) {
		case UPDATED_MESSAGES:
			refreshList();
			break;
		}
		super.receiveEvent(event);
	}

}
