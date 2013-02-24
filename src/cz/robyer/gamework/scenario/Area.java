package cz.robyer.gamework.scenario;

public abstract class Area {
	public static final String TYPE_POINT = "point";
	public static final String TYPE_MULTIPOINT = "multipoint";
	
	private String id;

	public Area(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	abstract public boolean isPointInArea(double lat, double lon);
	
}
