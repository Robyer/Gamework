package cz.robyer.gamework.scenario;

public abstract class IdentificableObject extends BaseObject {
	protected String id;
	
	public IdentificableObject(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
}
