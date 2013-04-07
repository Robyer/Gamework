package cz.robyer.gamework.scenario;

/**
 * Represents basic informations about {@link Scenario}.
 * @author Robert Pösel
 */
public class ScenarioInfo {

	protected final String title;
	protected final String author;
	protected final String version;
	protected final String location;
	protected final String duration;
	protected final String difficulty;
	
	public ScenarioInfo(String title, String author, String version, String location, String duration, String difficulty) {
		this.title = title;
		this.author = author;
		this.version = version;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
	}
	
	public final String getTitle() {
		return title;
	}

	public final String getAuthor() {
		return author;
	}

	public final String getVersion() {
		return version;
	}

	public final String getLocation() {
		return location;
	}

	public final String getDuration() {
		return duration;
	}

	public final String getDifficulty() {
		return difficulty;
	}

}
