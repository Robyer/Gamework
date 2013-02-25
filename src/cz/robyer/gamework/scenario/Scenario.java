package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.reaction.MultiReaction;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;

public class Scenario {
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
	
	public void addArea(String id, Area area) {
		if (areas == null)
			areas = new HashMap<String, Area>();
		
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
		
		reactions.put(id, reaction);
		reaction.setScenario(this);
	}
	
	public void addReaction(Reaction reaction) {
		addReaction(reaction.getId(), reaction);
	}
	
	public Reaction getReaction(String id) {
		return reactions.get(id);
	}
	
	
	public void addHook(Hook hook) {
		if (hooks == null)
			hooks = new ArrayList<Hook>();
		
		hooks.add(hook);
		// TODO: add hook to particular objects
		hook.setScenario(this);
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

}
