package cz.robyer.gamework.scenario.area;

import cz.robyer.gamework.scenario.HookableObject;

public abstract class Area extends HookableObject {
	
	public Area(String id) {
		super(id);
	}
	
	abstract public boolean isPointInArea(double lat, double lon);
	
}
