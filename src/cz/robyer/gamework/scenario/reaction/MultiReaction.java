package cz.robyer.gamework.scenario.reaction;

import java.util.ArrayList;
import java.util.List;

public class MultiReaction extends Reaction {

	List<Reaction> reactions;
	
	public MultiReaction(String id) {
		super(id);
		this.reactions = new ArrayList<Reaction>();
	}
	
	public MultiReaction(String id, List<Reaction> reactions) {
		super(id);
		this.reactions = reactions;
	}
	
	public void addReaction(Reaction reaction) {
		if (reaction != null)
			this.reactions.add(reaction);
	}
	
	@Override
	public void action() {
		for (Reaction reaction : reactions) {
		    reaction.action();
		}
	}

}
