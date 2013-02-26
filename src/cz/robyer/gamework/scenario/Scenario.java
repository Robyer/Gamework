package cz.robyer.gamework.scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import cz.robyer.gamework.R;
import cz.robyer.gamework.hook.Hook;
import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.reaction.SoundReaction;
import cz.robyer.gamework.scenario.variable.Variable;

public class Scenario {
	public static final String TAG = Scenario.class.getSimpleName();
	
	protected Context context;
	
	protected String title;
	protected String author;
	protected String version;
	protected String location;
	protected String duration;
	protected String difficulty;
	
	protected Map<String, Area> areas;// = new HashMap<String, Area>();
	protected Map<String, Variable> variables;// = new HashMap<String, Variable>();
	protected Map<String, Reaction> reactions;// = new HashMap<String, Reaction>();
	protected List<Hook> hooks;// = new ArrayList<Hook>();
	protected SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	
	public Scenario(Context context) {
		this.context = context;
	}
	
	public Scenario(Context context, String title, String author, String version, String location, String duration, String difficulty) {
		this.context = context;
		this.title = title;
		this.author = author;
		this.version = version;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
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
		
		if (reaction instanceof SoundReaction) {
			// initialize sound in soundpool
			// TODO: rewrite better with support for MultiReactions...
			AssetFileDescriptor descriptor;
			try {
				descriptor = getContext().getAssets().openFd(((SoundReaction)reaction).getValue());
				int soundId = soundPool.load(descriptor, 1);
				((SoundReaction)reaction).setSoundId(soundId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
			// TODO: get hookable time object
			break;
		}

		if (hookable != null) {
			hooks.add(hook);
			hook.setScenario(this);
			hookable.addHook(hook);
		} else {
			// TODO: throw exception?
		}
	}
	
	/* basic information getters/setters */

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	public String getDescription() {
		return getContext().getResources().getString(R.string.scenarioInfo, author, version, location, duration, difficulty);
	}

}
