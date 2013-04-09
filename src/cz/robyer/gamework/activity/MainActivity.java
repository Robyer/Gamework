package cz.robyer.gamework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cz.robyer.gamework.R;

/**
 * This is the main activity of application.
 * @author Robert Pösel
 */
public class MainActivity extends BaseActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initButtons();
	}
	
	/**
	 * Init handlers for main buttons.
	 */
	private void initButtons() {
		((Button)findViewById(R.id.btn_testing)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TestingActivity.class);
				startActivity(intent);
			}
		});
		
		OnClickListener notImplementedListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
			}
		}; 
		
		((Button)findViewById(R.id.btn_play)).setOnClickListener(notImplementedListener);		
		((Button)findViewById(R.id.btn_scenarios)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_options)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		((Button)findViewById(R.id.btn_help)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});
	}

}
