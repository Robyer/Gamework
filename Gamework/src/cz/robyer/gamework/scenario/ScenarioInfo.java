package cz.robyer.gamework.scenario;

/**
 * Represents basic informations about {@link Scenario}.
 * @author Robert Pösel
 */
public class ScenarioInfo {

	public final String title;
	public final String author;
	public final String version;
	public final String location;
	public final String duration;
	public final String difficulty;
	public final String description;
	
	/**
	 * Class constructor.
	 * @param title
	 * @param author
	 * @param version
	 * @param location
	 * @param duration
	 * @param difficulty
	 */
	public ScenarioInfo(String title, String author, String version, String location, String duration, String difficulty, String description) {
		this.title = title;
		this.author = author;
		this.version = version;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
		this.description = description;
	}

}
