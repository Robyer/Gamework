package cz.robyer.gamework.scenario.reaction;

import android.content.Context;
import cz.robyer.gamework.scenario.Scenario;

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
	
	protected String id;
	protected Scenario scenario;
	protected Context context;

	public Reaction(String id) {
		// TODO: remove this method as Scenario parameter is required
		this.id = id;
	}
	
	public Reaction(Scenario scenario, String id) {
		this.scenario = scenario;
		this.context = scenario.getContext();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
		this.context = scenario.getContext();
	}
	
	public abstract void action();
	
}
