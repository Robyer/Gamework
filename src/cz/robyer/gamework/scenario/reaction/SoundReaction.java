package cz.robyer.gamework.scenario.reaction;

public class SoundReaction extends Reaction {
	private String value;
	
	public SoundReaction(String id, String value) {
		super(id);
		this.value = value;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		//scenario.soundManager.playSound(value);
	}

}
