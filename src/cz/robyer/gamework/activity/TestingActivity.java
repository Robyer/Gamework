package cz.robyer.gamework.activity;

import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.robyer.gamework.R;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.scenario.ScenarioParser;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.util.Log;

public class TestingActivity extends Activity {
	private static final String TAG = TestingActivity.class.getSimpleName();

	public static Scenario scenario;
	
	private LocationManager myLocationManager;
	private LocationListener myLocationListener;
	private TextView myLatitude, myLongitude;
	
/*	private Timer timer;*/
	
	long starttime = 0;
	TextView time_text;
	
	
	private final Handler h = new Handler(new Callback() {

		@Override
        public boolean handleMessage(Message msg) {
			//long millis = System.currentTimeMillis() - starttime;			
			Bundle data = msg.peekData();
			if (data != null) {
				long millis = data.getLong("time", 0); 
						
				int seconds = (int) (millis / 1000);
				int minutes = seconds / 60;
				seconds     = seconds % 60;

				time_text.setText(String.format("%d:%02d", minutes, seconds));
			}
			return false;
        }

    });
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
		// Show the Up button in the action bar.
		setupActionBar();
		
		time_text = (TextView)findViewById(R.id.edit_timestamp);		
		
		((Button)findViewById(R.id.btn_timer)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button b = (Button)v;
                if (b.getText().equals("Stop timer")){
                    myLocationManager.removeUpdates(myLocationListener);
                	
                	/*timer.cancel();
                    timer.purge();*/
                    //h2.removeCallbacks(run);
                    b.setText("Start timer");
                    
                    scenario.getTimeUpdater().stop();
                } else {
                    starttime = System.currentTimeMillis();
                    
            		myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            		myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
            		
            		scenario.getTimeUpdater().start(true);
                    
               /*     timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {

    					@Override
    					public void run() {
    						// http://stackoverflow.com/questions/4597690/android-timer-how
    						Log.i("Timer", "Tik tak. System time: " + System.currentTimeMillis());
    						h.sendEmptyMessage(0);
    						
    						
    						
    						/*TestingActivity.this.runOnUiThread(new Runnable() {

    			                @Override
    			                public void run() {
    			                   long millis = System.currentTimeMillis() - starttime;
    			                   int seconds = (int) (millis / 1000);
    			                   int minutes = seconds / 60;
    			                   seconds     = seconds % 60;

    			                   time_text.setText(String.format("%d:%02d", minutes, seconds));
    			                }
    			            });*/
/*    					}
    				
    				}, 0,500);*/
                    //timer.schedule(new secondTask(),  0,500);
                    //h2.postDelayed(run, 0);
                    b.setText("Stop timer");
                }
			}			
		});
		
		((Button)findViewById(R.id.btn_filename)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				String filename = ((EditText)findViewById(R.id.edit_filename)).getText().toString();
				scenario = parseScenario(filename);
				
				if (scenario != null) {					
					scenario.setHandler(TestingActivity.this.h);
					
					//ProgressDialog dialog = ProgressDialog.show(TestingActivity.this, "", "Loading. Please wait...", true);
					Toast.makeText(TestingActivity.this, "Sucessfully parsed scenario '" + filename + "'.", Toast.LENGTH_LONG).show();
					
					/*AlertDialog ad = new AlertDialog.Builder(TestingActivity.this).create();
					ad.setCancelable(false); // This blocks the 'BACK' button
					//ad.setIcon(android.R.drawable.ic_dialog_info);
					ad.setTitle(scenario.getInfo().getTitle());
					ad.setMessage(scenario.getDescription());
					ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {  
					    @Override  
					    public void onClick(DialogInterface dialog, int which) {  
					        dialog.dismiss();
					    }  
					});
					
					//dialog.hide();
					ad.show();*/
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
		
		// 'Show map' button
		((Button)findViewById(R.id.btn_showmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(i);
			}
		});
		
		// GPS location
		myLatitude = (TextView)findViewById(R.id.Latitude);
		myLongitude = (TextView)findViewById(R.id.Longitude);

		myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		myLocationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				myLatitude.setText(String.valueOf(location.getLatitude()));
				myLongitude.setText(String.valueOf(location.getLongitude()));
			}
		};

		// Get the current location in start-up
		Location loc = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null)
			loc = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		if (loc != null) {
			myLatitude.setText(String.valueOf(loc.getLatitude()) + " (" + loc.getProvider() + ")");
			myLongitude.setText(String.valueOf(loc.getLongitude()) + " (" + loc.getProvider() + ")");
		}
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
