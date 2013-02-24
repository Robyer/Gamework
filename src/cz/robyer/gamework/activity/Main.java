package cz.robyer.gamework.activity;

import java.io.InputStream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cz.robyer.gamework.R;
import cz.robyer.gamework.service.ScenarioParser;
import cz.robyer.gamework.util.Log;

public class Main extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		OnClickListener notImplementedListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Main.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
			}
		}; 
		
		((Button)findViewById(R.id.btn_play)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_scenarios)).setOnClickListener(notImplementedListener);
		((Button)findViewById(R.id.btn_options)).setOnClickListener(notImplementedListener);
		
		((Button)findViewById(R.id.btn_help)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Help.class);
				startActivity(intent);
			}
		});
		
		((Button)findViewById(R.id.btn_parse)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parseScenario("example.xml");
			}
		});
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	private void parseScenario(String filename) {
		try {
			ScenarioParser parser = new ScenarioParser();
			
			InputStream file = getAssets().open(filename);
			parser.parse(file);
			file.close();
		} catch (Exception e) {
			Log.e("Main", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
