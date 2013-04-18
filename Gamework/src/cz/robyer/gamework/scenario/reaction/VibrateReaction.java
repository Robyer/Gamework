package cz.robyer.gamework.scenario.reaction;

import android.content.Context;
import android.os.Vibrator;

// TODO: patterns, repeations, etc. - http://android.konreu.com/developer-how-to/vibration-examples-for-android-phone-development/

/**
 * 
 * @author Robert Pösel
 */
public class VibrateReaction extends Reaction {
	private static final int MAX = 5000;
	
	protected int value = 100;
	
	public VibrateReaction(String id, int value) {
		super(id);
		this.value = Math.min(Math.max(value, 1), MAX);
	}

	@Override
	public void action() {
		Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(value);
	}

}
