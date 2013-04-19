package cz.robyer.gamework.scenario.reaction;

import cz.robyer.gamework.scenario.IdentificableObject;

/**
 * Base game reaction class.
 * @author Robert Pösel
 */
public abstract class Reaction extends IdentificableObject {
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 */
	public Reaction(String id) {
		super(id);
	}
	
	/**
	 * This method activate reaction's behavior (e.g. vibrate, play sound).
	 */
	public abstract void action();
	
}
