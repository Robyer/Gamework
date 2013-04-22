package cz.robyer.gamework.scenario.reaction;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.util.Log;
import cz.robyer.gamework.scenario.Scenario;

/**
 * Game reaction which play sound sample.
 * @author Robert Pösel
 */
public class SoundReaction extends Reaction {
	protected String value;
	protected int soundId = -1;
	protected float volume = 1.0f;
	protected float pitch = 1.0f;
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param value Filename of sound name from assets folder to be played.
	 */
	public SoundReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#setScenario(cz.robyer.gamework.scenario.Scenario)
	 */
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
		
		if (soundId == -1) {
			AssetFileDescriptor descriptor = null;
			try {
				descriptor = getContext().getAssets().openFd(value);
				soundId = getScenario().getSoundPool().load(descriptor, 1);
			} catch (IOException e) {
				Log.e(TAG, String.format("Can't load sound '%s'", value));
			} finally {
				try {
		        	if (descriptor != null)
		        		descriptor.close();
		        } catch (IOException ioe) {
		        	Log.e(TAG, ioe.getMessage(), ioe);
		        }
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (soundId == -1) {
			Log.e(TAG, String.format("Sound '%s' is not loaded", value));
			return;
		}
		
		getScenario().getSoundPool().play(soundId, volume, volume, 1, 0, pitch);
	}

}
