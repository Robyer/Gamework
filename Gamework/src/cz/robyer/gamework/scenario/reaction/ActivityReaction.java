package cz.robyer.gamework.scenario.reaction;

import android.content.Intent;
import android.util.Log;
import cz.robyer.gamework.utils.Utils;

/**
 * Game reaction which starts activity.
 * @author Robert Pösel
 */
public class ActivityReaction extends Reaction {
	protected String value;
	protected Intent intent;
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param value Full name (with namespace) of activity class which should be started.
	 */
	public ActivityReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Prepares intent which will be used to run activity.
	 */
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		intent = new Intent();
		
		// fix for relative class name, make it absolute
		if (value.charAt(0) == '.')
			value = getContext().getPackageName() + value;
		
		intent.setClassName(getContext(), value);
		intent.putExtra("reaction", id); // distribute reaction id to the activity
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		if (!Utils.isIntentCallable(getContext(), intent)) {
			Log.e(TAG, String.format("Activity '%s' is not callable", value));
			intent = null;
			return false;
		}
		
		return true;
	}
	
	/**
	 * Start defined activity (if it exists).
	 */
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (intent == null) {
			Log.e(TAG, "Intent for startActivity is null");
			return;
		}
		
		getContext().startActivity(intent);
	}

}
