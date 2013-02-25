package cz.robyer.gamework.scenario.reaction;

public abstract class Reaction {
	public static final String TYPE_MULTI = "multi";
	// Standard handling
	public static final String TYPE_SOUND = "sound";
	public static final String TYPE_VIBRATE = "vibrate";
	public static final String TYPE_HTML = "html";
	// Variables handling
	public static final String TYPE_DECREMENT = "decrement";
	public static final String TYPE_INCREMENT = "increment";
	public static final String TYPE_SET = "set";
	// Game events handling
	public static final String TYPE_GAME_START = "game_start";
	public static final String TYPE_GAME_WIN = "game_win";
	public static final String TYPE_GAME_LOSE = "game_lose";
	
	private String id;

	public Reaction(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public abstract void action();
	
}
