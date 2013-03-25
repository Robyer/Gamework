package cz.robyer.gamework.scenario;

import android.content.Context;
import cz.robyer.gamework.game.GameHandler;
import cz.robyer.gamework.util.Log;

public abstract class BaseObject {
	protected Scenario scenario;
	protected Context context;
	
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
		this.context = scenario.getContext();
	}

	/**
	 * @return false if error occured
	 */
	public boolean onScenarioLoaded() {
		return true;
	}
	
	public boolean isAttached() {
		return (scenario != null);
	}
	
	public Context getContext() {
		if (context == null) {
			Log.e("BaseObject", "BaseObject '" + this + "' is not attached to any Scenario");
			throw new RuntimeException();
		}
		
		return context;
	}
	
	public Scenario getScenario() {
		if (scenario == null) {
			Log.e("BaseObject", "BaseObject '" + this + "' is not attached to any Scenario");
			throw new RuntimeException();
		}
			
		return scenario;
	}
	
	public GameHandler getHandler() {
		GameHandler handler = getScenario().getHandler();
		if (handler == null) {
			Log.e("BaseObject", "Scenario have attached no GameHandler");
			throw new RuntimeException();
		}
		
		return handler;
	}
	
}
