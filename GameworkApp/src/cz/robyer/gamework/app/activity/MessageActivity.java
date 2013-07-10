package cz.robyer.gamework.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cz.robyer.gamework.game.GameService;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gamework.scenario.message.Message.MessageStatus;
import cz.robyer.gamework.app.R;

/**
 * Represents detail of game message.
 * @author Robert Pösel
 */
public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		if (!GameService.isRunning())
			return;

		String id = getIntent().getStringExtra("id");
		Message message = GameService.getInstance().getScenario().getMessage(id);
		
		if (!message.isVisible())
			return;

		TextView title = (TextView)findViewById(R.id.title);
		TextView content = (TextView)findViewById(R.id.content);
		
		if (title != null)
			title.setText(message.getTitle());
		
		if (content != null)
			content.setText(message.getContent());

		message.setStatus(MessageStatus.READ);
	}

}
