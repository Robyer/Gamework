package cz.robyer.gamework.scenario.reaction;

import android.content.Context;
import android.os.Vibrator;

// TODO: patterns, repeations, etc. - http://android.konreu.com/developer-how-to/vibration-examples-for-android-phone-development/

public class VibrateReaction extends Reaction {
	protected int value = 100;
	
	public VibrateReaction(String id, int value) {
		super(id);
		this.value = Math.min(Math.max(value, 1), 5000); // TODO: make limit as constant?
	}

	@Override
	public void action() {
		if (isAttached()) {
			Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(value);
		}
	}

}
