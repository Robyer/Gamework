package cz.robyer.gamework.scenario.area;

import cz.robyer.gamework.scenario.IdentificableObject;

public abstract class Area extends IdentificableObject {
	public static final String TYPE_POINT = "point";
	public static final String TYPE_MULTIPOINT = "multipoint";
	
	public Area(String id) {
		super(id);
	}
	
	abstract public boolean isPointInArea(double lat, double lon);
	
}
