package cz.robyer.gamework.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import cz.robyer.gamework.scenario.area.Area;
import cz.robyer.gamework.scenario.reaction.Reaction;
import cz.robyer.gamework.scenario.variable.Variable;

public class Scenario {
	private Context context;
	
	private String title;
	private String author;
	private String version;
	private String location;
	private String duration;
	private String difficulty;
	
	private Map<String, Area> areas;// = new HashMap<String, Area>();
	private Map<String, Variable> variables;// = new HashMap<String, Variable>();
	private Map<String, Reaction> reactions;// = new HashMap<String, Reaction>();
	private List<Hook> hooks;// = new ArrayList<Hook>();
	
	public Scenario(Context context, String title, String author, String version, String location, String duration, String difficulty) {
		this.context = context;
		this.title = title;
		this.author = author;
		this.version = version;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
	}
	
	/* Getters, setters */
	
	public Context getContext() {
		return context;
	}
	
	// TODO: pozor na pøipojení scenario do dìtí (pokud se pøedávají všechny najednou tak asi pøipojené nejsou...
	public void setAreas(HashMap<String, Area> areas) {
		this.areas = areas;
	}
	
	public void setVariables(HashMap<String, Variable> variables) {
		this.variables = variables;
	}
	
	public void setReactions(HashMap<String, Reaction> reactions) {
		this.reactions = reactions;
	}
	
	public void setHooks(ArrayList<Hook> hooks) {
		this.hooks = hooks;
	}
	

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
