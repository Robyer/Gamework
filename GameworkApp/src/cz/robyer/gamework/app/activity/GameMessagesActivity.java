package cz.robyer.gamework.app.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gamework.app.R;
import cz.robyer.gamework.app.service.MessageAdapter;

/**
 * Represents list of game messages.
 * @author Robert Pösel
 */
public class GameMessagesActivity extends BaseGameActivity {
	//private static final String TAG = GameMessagesActivity.class.getSimpleName();

	ArrayAdapter<Message> adapter;
	List<Message> messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_messages);
		initButtons();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		ListView list = (ListView)findViewById(R.id.list_messages);
		
		if (GameService.isRunning()) {
			messages = getGame().getScenario().getVisibleMessages("");
			adapter = new MessageAdapter(this, R.layout.messagelist_item, messages);
			
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(GameMessagesActivity.this, MessageActivity.class);
					Message msg = messages.get(position);
					intent.putExtra("id", msg.getId());
					startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * Refreshes list of messages.
	 */
	public void refreshList() {
		messages = getGame().getScenario().getVisibleMessages("");
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Checks UPDATED_MESSAGES event and updates game messages list.
	 */
	@Override
	public void receiveEvent(GameEvent event) {
		switch (event.type) {
		case UPDATED_MESSAGES:
			refreshList();
			break;
		default:
			break;
		}
		super.receiveEvent(event);
	}

}
