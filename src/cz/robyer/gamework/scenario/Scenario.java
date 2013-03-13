package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import cz.robyer.gamework.R;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;

public class Scenario {
	public static final String TAG = Scenario.class.getSimpleName();
	
	protected Context context;
	protected Handler handler;
	protected ScenarioInfo info;
	
	protected Map<String, Area> areas;// = new HashMap<String, Area>();
	protected Map<String, Variable> variables;// = new HashMap<String, Variable>();
	protected Map<String, Reaction> reactions;// = new HashMap<String, Reaction>();
	protected List<Hook> hooks;// = new ArrayList<Hook>();
	
	protected SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	protected TimeUpdater timeUpdater = new TimeUpdater(this);
	protected LocationUpdater locationUpdater = new LocationUpdater(this);
	
	protected long gameTime;
	protected long systemTime;
	protected Location location;
	
	public Scenario(Context context, ScenarioInfo info) {
		this.context = context;
		this.info = info;
	}
		
	public Context getContext() {
		return context;
	}
	
	public SoundPool getSoundPool() {
		return soundPool;
	}
	
	public void addArea(String id, Area area) {
		if (areas == null)
			areas = new HashMap<String, Area>();
		else if (areas.containsKey(id))
			Log.w(TAG, "Duplicit definition of area id='" + id + "'.");
		
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
		if (variables == null)
			variables = new HashMap<String, Variable>();
		else if (variables.containsKey(id))
			Log.w(TAG, "Duplicit definition of variable id='" + id + "'.");
		
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
		if (reactions == null)
			reactions = new HashMap<String, Reaction>();
		else if (reactions.containsKey(id))
			Log.w(TAG, "Duplicit definition of reaction id='" + id + "'.");
		
		reactions.put(id, reaction);
		reaction.setScenario(this);
	}
	
	public void addReaction(Reaction reaction) {
		addReaction(reaction.getId(), reaction);
	}
	
	public Reaction getReaction(String id) {
		return reactions.get(id);
	}
	
	
	public void addHook(Hook hook, int type, String value) {
		if (hooks == null)
			hooks = new ArrayList<Hook>();
		
		HookableObject hookable = null;
		
		switch (type) {
		case Hook.TYPE_AREA:
		case Hook.TYPE_AREA_ENTER:
		case Hook.TYPE_AREA_LEAVE:
			hookable = areas.get(value);
			break;
		case Hook.TYPE_VARIABLE:
			hookable = variables.get(value);
			break;
		case Hook.TYPE_TIME:
			hookable = timeUpdater;
			break;
			// TODO: add locationUpdater
		}

		if (hookable != null) {
			hooks.add(hook);
			hook.setScenario(this);
			hookable.addHook(hook);
		} else {
			// TODO: throw exception?
		}
	}
	
	public void updateLocation(Location location) {
		// TODO: call hooks etc.
		this.location = location;
	}
	
	public void updateTime(long time, long systemTime) {
		// TODO: call hooks etc.
		this.gameTime = time;
		this.systemTime = systemTime;
	}
	
	public String getDescription() {
		return getContext().getResources().getString(R.string.scenarioInfo,
			info.getAuthor(),
			info.getVersion(),
			info.getLocation(),
			info.getDuration(),
			info.getDifficulty()
		);
	}

	public Handler getHandler() {
		return handler;
	}

	public ScenarioInfo getInfo() {
		return info;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public TimeUpdater getTimeUpdater() {
		return timeUpdater;
	}

}
