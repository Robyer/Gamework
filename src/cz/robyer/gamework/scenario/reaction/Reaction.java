package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.scenario.IdentificableObject;

public abstract class Reaction extends IdentificableObject {
	
	public Reaction(String id) {
		super(id);
	}
	
	public abstract void action();
	
}
