package cz.robyer.gamework.scenario.reaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Represents reaction which starts defined activity.
 * @author Robert Pösel
 */
public class ActivityReaction extends Reaction {
	protected String value;
	protected Intent intent;
	
	/**
	 * Class constructor.
	 * @param id Identifier of this reaction.
	 * @param value Full name (with namespace) of activity class to be started.
	 */
	public ActivityReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Prepares intent which will be used to run activity.
	 */
	@Override
	public boolean onScenarioLoaded() {
		intent = new Intent();
		intent.setClassName(getContext(), value);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		if (!isCallable(intent)) {
			Log.e(TAG, String.format("Activity '%s' is not callable", value));
			intent = null;
			return false;
		}
		
		return true;
	}
	
	/**
	 * Start defined activity (if it exists).
	 */
	@Override
	public void action() {
		if (intent == null) {
			Log.e(TAG, "Intent for startActivity is null");
			return;
		}
		
		getContext().startActivity(intent);
	}
	
	/**
	 * Checks if there exists some activity for particular intent.
	 * @param intent to be checked.
	 * @return true if intent can be called, false otherwise.
	 */
	private boolean isCallable(Intent intent) {
        return getContext().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
	}

}
