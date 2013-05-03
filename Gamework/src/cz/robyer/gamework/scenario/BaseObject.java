package cz.robyer.gamework.scenario;

import android.content.Context;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.util.Log;

/**
 * This is base object for scenario.
 * @author Robert Pösel
 */
public abstract class BaseObject {
	protected Scenario scenario;
	protected Context context;
	
	/**
	 * Attach scenario to this object.
	 * @param scenario
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
		this.context = scenario.getContext();
	}

	/**
	 * This is called when scenario loaded all objects.
	 * @return false if error occured
	 */
	public boolean onScenarioLoaded() {
		return true;
	}
	
	/**
	 * Checks if this object is attached to scenario.
	 * @return true if is attached, false otherwise
	 */
	public boolean isAttached() {
		return (scenario != null);
	}

	/**
	 * Return application context from scenario.
	 * @return Context or throws exception
	 */
	public Context getContext() {
		if (context == null) {
			Log.e("BaseObject", "BaseObject '" + this + "' is not attached to any Scenario");
			throw new RuntimeException();
		}
		
		return context;
	}
	
	/**
	 * Return game scenario object.
	 * @return Scenario or throws exception
	 */
	public Scenario getScenario() {
		if (scenario == null) {
			Log.e("BaseObject", "BaseObject '" + this + "' is not attached to any Scenario");
			throw new RuntimeException();
		}
			
		return scenario;
	}

	/**
	 * Shortcut for getting game handler from scenario.
	 * @return GameHandler or throws exception
	 */
	public GameHandler getHandler() {
		GameHandler handler = getScenario().getHandler();
		if (handler == null) {
			Log.e("BaseObject", "Scenario have attached no GameHandler");
			throw new RuntimeException();
		}
		
		return handler;
	}
	
}
