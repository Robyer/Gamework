package cz.robyer.gamework.scenario.reaction;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;

public class SoundReaction extends Reaction {
	protected String value;
	protected int soundId;
	
	public SoundReaction(String id, String value) {
		super(id);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public void action() {
		if (soundId == 0) {
			// TODO: rewrite better while loading scenario so android have time to load this sound
			try {
				AssetFileDescriptor descriptor = getContext().getAssets().openFd(value);
				soundId = getScenario().getSoundPool().load(descriptor, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (soundId != 0)
			scenario.getSoundPool().play(soundId, 0.5f, 0.5f, 1, 0, 1.0f);
	}

	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}

}
