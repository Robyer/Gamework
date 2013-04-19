package cz.robyer.gamework.scenario.reaction;

import android.util.Log;

/**
 * Game reaction which only activate another reaction.
 * @author Robert Pösel
 */
public class ReferenceReaction extends Reaction {
	protected String value;
	protected Reaction reaction;
	
	/**
	 * Class constructor.
	 * @param id Identificator of this reaction.
	 * @param value Identificator of reaction which should be activated.
	 */
	public ReferenceReaction(String id, String value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * Checks if given Reaction can be cross referenced to this. It works also recursively for MultiReactions.
	 * @param reaction MultiReaction to check cross reference.
	 * @return Reaction which points to this reaction or null if there isn's any.
	 */
	private Reaction checkCrossReference(Reaction reaction, int num) {
		if (num > 10) {
			Log.e(TAG, "There are too many nested reactions, most likely due to cross references.");
			return reaction;
		}
		
		if (reaction instanceof ReferenceReaction) {
			String value = ((ReferenceReaction)reaction).value;
			
			if (value.equalsIgnoreCase(id)) // ! this IF must be before the other one
				return reaction; // cross-reference with this reaction
			
			if (value.equalsIgnoreCase(reaction.getId()))
				return null; // self-reference of that reaction

			reaction = getScenario().getReaction(value);
			return checkCrossReference(reaction, num+1);
		} else if (reaction instanceof MultiReaction) {
			for (Reaction r : ((MultiReaction)reaction).getReactions()) {
				Reaction crossref = checkCrossReference(r, num+1);
				if (crossref != null)
					return crossref;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#onScenarioLoaded()
	 */
	@Override
	public boolean onScenarioLoaded() {
		reaction = getScenario().getReaction(value);
		if (reaction == null)
			Log.e(TAG, String.format("Reaction '%s' is null", value));
		
		// check cross references
		Reaction crossReaction = checkCrossReference(reaction, 0);
		if (crossReaction != null) {
			Log.e(TAG, "This reaction is cross-referenced with reaction id='" + crossReaction.getId() + "'");
			reaction = null;
		}
		
		return reaction != null;
	}

	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.reaction.Reaction#action()
	 */
	@Override
	public void action() {
		if (reaction == null) {
			Log.e(TAG, String.format("Reaction '%s' is null", value));
			return;
		}
		
		reaction.action();
	}

}
