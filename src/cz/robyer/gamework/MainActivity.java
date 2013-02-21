package cz.robyer.gamework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		OnClickListener notImplementedListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
			}
			
		}; 
		
		((Button)findViewById(R.id.btn_play)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_scenarios)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_options)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_help)).setOnClickListener(notImplementedListener);
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
