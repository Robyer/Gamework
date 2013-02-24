package cz.robyer.gamework.scenario.variable;

public class BooleanVariable extends Variable {
	private boolean value;
	
	public BooleanVariable(String id, boolean value) {
		super(id);
		this.value = value;
	}
	
	public static BooleanVariable fromString(String id, String value) {
		return new BooleanVariable(id, Boolean.parseBoolean(value));
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
}
