package cz.robyer.gw_example.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.robyer.gamework.game.GameEvent;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gw_example.R;
import cz.robyer.gw_example.service.MessageAdapter;

/**
 * Represents list of game objectives (tasks)
 * @author Robert Pösel
 */
public class GameObjectivesActivity extends BaseGameActivity {
	//private static final String TAG = GameObjectivesActivity.class.getSimpleName();
	
	ArrayAdapter<Message> adapter;
	List<Message> messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_tasks);
		initButtons();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		ListView list = (ListView)findViewById(R.id.list_messages);
		
		if (GameService.isRunning()) {
			messages = getGame().getScenario().getVisibleMessages("goals");
			adapter = new MessageAdapter(this, R.layout.messagelist_item, messages);			
			list.setAdapter(adapter);
		}
	}
	
	/**
	 * Refreshes list of messages.
	 */
	public void refreshList() {
		messages = getGame().getScenario().getVisibleMessages("goals");
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
		}
		super.receiveEvent(event);
	}

}
