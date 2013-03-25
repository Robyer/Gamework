package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import cz.robyer.gamework.R;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;

public class Scenario {
	private static final String TAG = Scenario.class.getSimpleName();
	
	protected final Context context;
	protected final ScenarioInfo info;
	protected GameHandler handler;
	
	protected final Map<String, Area> areas = new HashMap<String, Area>();
	protected final Map<String, Variable> variables = new HashMap<String, Variable>();
	protected final Map<String, Reaction> reactions = new HashMap<String, Reaction>();
	protected final Map<String, Message> messages = new HashMap<String, Message>();
	protected final List<Hook> hooks = new ArrayList<Hook>();
	
	protected final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	protected final TimeUpdater timeUpdater = new TimeUpdater(this);
	
	public Scenario(Context context, ScenarioInfo info) {
		this.context = context;
		this.info = info;
	}
	
	public Scenario(Context context, GameHandler handler, ScenarioInfo info) {
		this(context, info);
		setHandler(handler);
	}
		
	public Context getContext() {
		return context;
	}
	
	public GameHandler getHandler() {
		return handler;
	}
	
	public void setHandler(GameHandler handler) {
		this.handler = handler;
	}

	public TimeUpdater getTimeUpdater() {
		return timeUpdater;
	}

	
	public SoundPool getSoundPool() {
		return soundPool;
	}
	
	public void addArea(String id, Area area) {
		if (area == null) {
			Log.w(TAG, "addArea() with null area");
			return;
		}
		
		if (areas.containsKey(id))
			Log.w(TAG, "Duplicit definition of area id='" + id + "'");
		
		areas.put(id, area);
		area.setScenario(this);
	}
	
	public void addArea(Area area) {
		addArea(area.getId(), area);
	}
	
	public Area getArea(String id) {
		return areas.get(id);
	}
	
	public Map<String, Area> getAreas() {
		return areas;
	}
	
	public void addVariable(String id, Variable variable) {
		if (variable == null) {
			Log.w(TAG, "addVariable() with null variable");
			return;
		}
		
		if (variables.containsKey(id))
			Log.w(TAG, "Duplicit definition of variable id='" + id + "'");
		
		variables.put(id, variable);
		variable.setScenario(this);
	}
	
	public void addVariable(Variable variable) {
		addVariable(variable.getId(), variable);
	}
	
	public Variable getVariable(String id) {
		return variables.get(id);
	}
	
		
	public void addReaction(String id, Reaction reaction) {
		if (reaction == null) {
			Log.w(TAG, "addReaction() with null reaction");
			return;
		}
		
		if (reactions.containsKey(id))
			Log.w(TAG, "Duplicit definition of reaction id='" + id + "'");
		
		reactions.put(id, reaction);
		reaction.setScenario(this);
	}
	
	public void addReaction(Reaction reaction) {
		addReaction(reaction.getId(), reaction);
	}
	
	public Reaction getReaction(String id) {
		return reactions.get(id);
	}
	
	public void addMessage(String id, Message message) {
		if (message == null) {
			Log.w(TAG, "addMessage() with null message");
			return;
		}
		
		if (messages.containsKey(id))
			Log.w(TAG, "Duplicit definition of message id='" + id + "'");
		
		messages.put(id, message);
		message.setScenario(this);
	}
	
	public void addMessage(Message message) {
		addMessage(message.getId(), message);
	}
	
	public Message getMessage(String id) {
		return messages.get(id);
	}
	
	public List<Message> getVisibleMessages() {
		List<Message> list = new ArrayList<Message>();
		for (Message m : messages.values()) {
			//if (m.isVisible())
				list.add(m);
		}
		return list;
	}

	
	public void addHook(Hook hook) {
		if (hook == null) {
			Log.w(TAG, "addHook() with null hook");
			return;
		}
		
		hooks.add(hook);
		hook.setScenario(this);
	}
	
	public String getDescription() {
		return getContext().getResources().getString(R.string.scenario_info,
			info.getAuthor(),
			info.getVersion(),
			info.getLocation(),
			info.getDuration(),
			info.getDifficulty()
		);
	}

	public ScenarioInfo getInfo() {
		return info;
	}

	public void onTimeUpdate(long time) {
		// TODO: improve somehow?
		timeUpdater.updateTime(time);
	}

	public void onLocationUpdate(double lat, double lon) {
		// TODO: improve somehow?
		for (Area a : areas.values()) {
			a.updateLocation(lat, lon);
		}
	}

	private void initializeHooks() {
		for (Hook hook : hooks) {
			HookableObject hookable = null;
			String type = null;
			
			switch (hook.getType()) {
			case Hook.TYPE_AREA:
			case Hook.TYPE_AREA_ENTER:
			case Hook.TYPE_AREA_LEAVE:
				hookable = areas.get(hook.getValue());
				type = "Area";
				break;
			case Hook.TYPE_VARIABLE:
				hookable = variables.get(hook.getValue());
				type = "Variable";
				break;
			case Hook.TYPE_TIME:
				hookable = timeUpdater;
				type = "Time";
				break;
			}

			if (hookable != null) {
				hookable.addHook(hook);
			} else {
				Log.e(TAG, String.format("Hook can't be attached to %s '%s'", type, hook.getValue()));
			}
		}
	}
	
	/**
	 * Called after whole scenario was loaded (all game items are present).
	 */
	public void onLoaded() {
		initializeHooks();
		
		boolean ok = true;
		
		for (BaseObject obj : areas.values())
			if (!obj.onScenarioLoaded())
				ok = false;
		
		for (BaseObject obj : reactions.values())
			if (!obj.onScenarioLoaded())
				ok = false;
		
		for (BaseObject obj : variables.values())
			if (!obj.onScenarioLoaded())
				ok = false;
		
		for (BaseObject obj : hooks)
			if (!obj.onScenarioLoaded())
				ok = false;
		
		if (!ok)
			Log.e(TAG, "Scenario contains errors");
			// TODO: hmm, or better? put some logging functions into Scenario itself, which will log errors also into file, etc. - oh, maybe not in scenario, but in BaseObject itself... - hm, or not baseobject but keep it here? for errors that are "scenario-related" = chyby v xml definici, ne moje chyby pøi vývoji tady, ty logovat normálnì jen do konzole (tzn. chyby jako scenario is not attached jsou moje)
	}
	
}
