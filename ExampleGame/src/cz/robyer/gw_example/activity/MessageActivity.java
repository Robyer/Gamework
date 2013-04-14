package cz.robyer.gw_example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import cz.robyer.gw_example.R;

/**
 * Represents detail of game message.
 * @author Robert Pösel
 */
public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		// TODO: load and show message data
		
		int position = getIntent().getIntExtra("position", 0);  
		long id = getIntent().getLongExtra("id", 0);
		Toast.makeText(this, "Message position: " + position + ", id:" + id, Toast.LENGTH_LONG).show();
	}

}
