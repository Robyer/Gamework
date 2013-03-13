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
		
		long secs = time / 1000; // from mili to seconds
		boolean isMinute = (secs % 60 == 0);
		boolean isHour = (secs % 3600 == 0);
		
		for (Hook h : hooks) {
			boolean valid = false;
				
			switch (h.getType()) {
			case Hook.TYPE_TIME:				
				if (h.getValue().equalsIgnoreCase("second")) {
					valid = true;
				} else if (h.getValue().equalsIgnoreCase("minute")) {
					valid = isMinute;
				} else if (h.getValue().equalsIgnoreCase("hour")) {
					valid = isHour;
				}
				break;
			}
				
			if (valid)
				h.call();
		}
	}
	
	public void start(boolean reset) {
		if (timer == null) {
			Log.i("TimeUpdater", "Starting timer.");
			// system time clock: System.currentTimeMillis()
			// uptime clock (not ticking in deep sleep): SystemClock.uptimeMillis();
			if (reset || start <= 0)
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
				
			}, 1000, 1000);
		} else {
			Log.e("TimeUpdater", "Timer is running, it can't be started again, until it's stopped.");
		}
	}
	
	public void stop() {
		Log.i("TimeUpdater", "Stopping timer.");
		timer.cancel();
		timer.purge();
		timer = null;
	}

}
