package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.scenario.IdentificableObject;

public abstract class Reaction extends IdentificableObject {
	public static final String TYPE_MULTI = "multi";
	// Standard handling
	public static final String TYPE_SOUND = "sound";
	public static final String TYPE_VIBRATE = "vibrate";
	public static final String TYPE_HTML = "html";
	// Variables handling
	public static final String TYPE_VAR_SET = "set";
	public static final String TYPE_VAR_INC = "increment";
	public static final String TYPE_VAR_DEC = "decrement";
	public static final String TYPE_VAR_MUL = "multiply";
	public static final String TYPE_VAR_DIV = "divide";
	public static final String TYPE_VAR_NEG = "negate";	
	// Game events handling
	public static final String TYPE_GAME_START = "game_start";
	public static final String TYPE_GAME_WON = "game_won";
	public static final String TYPE_GAME_LOSE = "game_lose";
	
	public Reaction(String id) {
		super(id);
	}
	
	public abstract void action();
	
}
