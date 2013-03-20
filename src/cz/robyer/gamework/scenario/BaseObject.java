package cz.robyer.gamework.scenario;

import android.content.Context;

public abstract class BaseObject {
	protected Scenario scenario;
	protected Context context;
	
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
		this.context = scenario.getContext();
	}
	
	public boolean isAttached() {
		return (scenario != null);
	}
	
	public Context getContext() {
		if (context == null)
			throw new RuntimeException("BaseObject '" + this + "' is not attached to any Scenario.");
			
		return context;
	}
	
	public Scenario getScenario() {
		if (scenario == null)
			throw new RuntimeException("BaseObject '" + this + "' is not attached to any Scenario.");
			
		return scenario;
	}
	
}
