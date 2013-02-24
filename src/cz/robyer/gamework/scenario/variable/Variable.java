package cz.robyer.gamework.scenario.variable;

public abstract class Variable {
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_DECIMAL = "decimal";
	
	private String id;

	public Variable(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
}
