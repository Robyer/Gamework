package cz.robyer.gamework.activity;

import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import cz.robyer.gamework.R;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioParser;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.util.Log;

public class TestingActivity extends Activity {

	private Scenario scenario;
	private static final String TAG = TestingActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
		// Show the Up button in the action bar.
		setupActionBar();
		
		((Button)findViewById(R.id.btn_filename)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				String filename = ((EditText)findViewById(R.id.edit_filename)).getText().toString();
				scenario = parseScenario(filename);
				
				if (scenario != null) {
					//ProgressDialog dialog = ProgressDialog.show(TestingActivity.this, "", "Loading. Please wait...", true);
					Toast.makeText(TestingActivity.this, "Sucessfully parsed scenario '" + filename + "'.", Toast.LENGTH_LONG).show();
					
					AlertDialog ad = new AlertDialog.Builder(TestingActivity.this).create();
					ad.setCancelable(false); // This blocks the 'BACK' button
					//ad.setIcon(android.R.drawable.ic_dialog_info);
					ad.setTitle(scenario.getTitle());
					ad.setMessage(scenario.getDescription());
					ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {  
					    @Override  
					    public void onClick(DialogInterface dialog, int which) {  
					        dialog.dismiss();
					    }  
					});
					
					//dialog.hide();
					ad.show();
				} else {
					Toast.makeText(TestingActivity.this, "Error when parsing scenario '" + filename + "'.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		((Button)findViewById(R.id.btn_reaction)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String value = ((EditText)findViewById(R.id.edit_reaction)).getText().toString();
				if (scenario != null) {
					Reaction reaction = scenario.getReaction(value);
					if (reaction != null)
						reaction.action();
					else {
						Toast.makeText(TestingActivity.this, "Reaction with this id doesn't exist.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(TestingActivity.this, "You must load some scenario first.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		// Show map button
		((Button)findViewById(R.id.btn_showmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(i);
			}
		});
		
	}
	
	private Scenario parseScenario(String filename) {
		Scenario scenario = null;
		try {
			ScenarioParser parser = new ScenarioParser(getApplicationContext(), false);
			InputStream file = getAssets().open(filename);
			scenario = parser.parse(file, false);
			file.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return scenario;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.testing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
