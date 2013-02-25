package cz.robyer.gamework.scenario.reaction;

public class VariableReaction extends Reaction {
	public static final int SET = 0;
	public static final int INCREMENT = 1;
	public static final int DECREMENT = 2;
	
	private String variable;
	private String value;
	private int type;
	
	public VariableReaction(String id, int type, String variable, String value) {
		super(id);
		this.type = type;
		this.variable = variable;
		this.value = value;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

}
