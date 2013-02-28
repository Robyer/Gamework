package cz.robyer.gamework.scenario.reaction;

import java.io.IOException;

import cz.robyer.gamework.scenario.Scenario;

import android.content.res.AssetFileDescriptor;

public class SoundReaction extends Reaction {
	protected String value;
	protected int soundId = -1;
	
	public SoundReaction(String id, String value) {
		super(id);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
		
		if (soundId == -1) {
			try {
				AssetFileDescriptor descriptor = getContext().getAssets().openFd(value);
				soundId = getScenario().getSoundPool().load(descriptor, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	@Override
	public void action() {
		if (soundId != -1)
			getScenario().getSoundPool().play(soundId, 1.0f, 1.0f, 1, 0, 0.6f);
	}

}
