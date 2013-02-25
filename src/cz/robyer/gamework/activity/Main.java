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
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.reaction.SoundReaction;
import cz.robyer.gamework.scenario.reaction.VibrateReaction;
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
		
		((Button)findViewById(R.id.btn_play)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Scenario scenario = new Scenario(getApplicationContext(), "", "", "", "", "", "");
				Reaction reaction = new VibrateReaction("", 500);
				reaction.setScenario(scenario);
				reaction.action();
			}
		});
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
			parser.parse(this.getApplicationContext(), file);
			file.close();
		} catch (Exception e) {
			Log.e("Main", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
