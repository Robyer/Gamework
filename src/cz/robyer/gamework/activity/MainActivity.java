package cz.robyer.gamework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cz.robyer.gamework.R;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		((Button)findViewById(R.id.btn_options)).setOnClickListener(notImplementedListener);
		
		((Button)findViewById(R.id.btn_help)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
