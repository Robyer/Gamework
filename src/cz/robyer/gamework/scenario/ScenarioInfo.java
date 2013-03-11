package cz.robyer.gamework.scenario;

public class ScenarioInfo {

	protected String title;
	protected String author;
	protected String version;
	protected String location;
	protected String duration;
	protected String difficulty;
	
	public ScenarioInfo() {
		
	}
	
	public ScenarioInfo(String title, String author, String version, String location, String duration, String difficulty) {
		this.title = title;
		this.author = author;
		this.version = version;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
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
