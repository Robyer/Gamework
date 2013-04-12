package cz.robyer.gamework.scenario.reaction;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

/**
 * 
 * @author Robert Pösel
 */
public class ActivityReaction extends Reaction {
	protected String value;
	protected Intent intent;
	
	public ActivityReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
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
	
	@Override
	public void action() {
		if (intent == null) {
			Log.e(TAG, "Intent for startActivity is null");
			return;
		}
		
		getContext().startActivity(intent);
	}
	
	private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
	}

}
