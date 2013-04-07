package cz.robyer.gamework.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import cz.robyer.gamework.R;

/**
 * Contains global settings for whole application.
 * @author Robert Pösel
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.settings);
	}
	
}
