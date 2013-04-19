package cz.robyer.gamework.scenario.reaction;

import android.content.Context;
import android.os.Vibrator;

/**
 * Game reaction which will start vibrating for a defined amount of time.
 * @author Robert Pösel
 */
public class VibrateReaction extends Reaction {
	private static final int USE_PATTERN = -1;
	private static final int VALUE_MAX = 10000;
	
	protected int value;
	protected long[] pattern;
	
	/**
	 * Class constructor for simple vibrating.
	 * @param id Identificator of this reaction.
	 * @param value Number of miliseconds to vibrate.
	 */
	public VibrateReaction(String id, int value) {
		super(id);
		this.value = Math.min(Math.max(value, 1), VALUE_MAX);
	}
	
	/**
	 * Class constructor for vibrating with pattern.
	 * @param id Identificator of reaction.
	 * @param pattern Vibration pattern.
	 * @see {@link Vibrator#vibrate(long[], int)}
	 */
	public VibrateReaction(String id, long[] pattern) {
		super(id);
		this.value = USE_PATTERN;
		this.pattern = pattern;
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		if (value == USE_PATTERN)
			v.vibrate(pattern, -1);
		else
			v.vibrate(value);
	}

}
