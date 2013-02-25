package cz.robyer.gamework.scenario.reaction;

public class GameReaction extends Reaction {
	public static final int START = 0;
	public static final int WIN = 1;
	public static final int LOSE = 2;

	private int type;
	
	public GameReaction(String id, int type) {
		super(id);
		this.type = type;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

}
