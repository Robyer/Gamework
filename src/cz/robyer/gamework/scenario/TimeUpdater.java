package cz.robyer.gamework.scenario;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import cz.robyer.gamework.hook.Hook;

public class TimeUpdater extends HookableObject {
	
	Timer timer;
	long start;
	
	public TimeUpdater(Scenario scenario) {
		super("Gamework:TIME");
		this.scenario = scenario;
	}
	
	protected void callHooks(long time) {
		if (hooks == null)
			return;
		
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case Hook.TYPE_TIME:
				time /= 1000; // from mili to seconds
				
				if (h.getValue().equalsIgnoreCase("second")) {
					valid = true;
				} else if (h.getValue().equalsIgnoreCase("minute")) {
					valid = (time % 60 == 0);
				} else if (h.getValue().equalsIgnoreCase("hour")) {
					valid = (time % 3600 == 0);
				}

				break;
			}
				
			Log.d("TimeUpdater", valid ? "Calling hooks - " + h.getValue() : "Not calling hooks - " + h.getValue());
				
			if (valid)
				h.call();
		}
	}
	
	public void start() {
		if (timer == null) {
			Log.i("TimeUpdater", "Starting timer.");
			// system time clock: System.currentTimeMillis()
			// uptime clock (not ticking in deep sleep): SystemClock.uptimeMillis();
			start = SystemClock.uptimeMillis();
			
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					long time = SystemClock.uptimeMillis() - start;
					
					callHooks(time);
					
					Message msg = Message.obtain();
					Bundle data = new Bundle();
					data.putLong("time", time);
					msg.setData(data);
					
					getScenario().getHandler().sendMessage(msg);
					//getScenario().getHandler().sendEmptyMessage(0);
				}
				
			}, 0, 1000);
		} else {
			Log.e("TimeUpdater", "Timer is running, it can't be started again, until it's stopped.");
		}
	}
	
	/*public void pause() {
		
	}*/
	
	public void stop() {
		Log.i("TimeUpdater", "Stopping timer.");
		timer.cancel();
		timer.purge();
		timer = null;
	}

}
