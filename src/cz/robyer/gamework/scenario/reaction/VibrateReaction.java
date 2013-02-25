package cz.robyer.gamework.scenario.reaction;

import android.os.Vibrator;

public class VibrateReaction extends Reaction {
	private int value = 5;
	
	public VibrateReaction(String id, int value) {
		super(id);
		this.value = value;
	}

	@Override
	public void action() {
		if (context != null) {
			Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
			v.vibrate(Math.max(1000, value));
		} else {
			// not attached scenario yet
		}
	}

}
