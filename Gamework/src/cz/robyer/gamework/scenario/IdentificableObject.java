package cz.robyer.gamework.scenario;

/**
 * Base identificable object.
 * @author Robert Pösel
 */
public abstract class IdentificableObject extends BaseObject {
	protected final String id;
	protected final String TAG;
	
	/**
	 * Class constructor.
	 * @param id - identificator of object
	 */
	public IdentificableObject(String id) {
		super();
		this.id = id;
		this.TAG = String.format("%s (%s)", getClass().getSimpleName(), id);
	}

	/**
	 * Returns identificator of object.
	 * @return identificator
	 */
	public String getId() {
		return id;
	}
	
}
