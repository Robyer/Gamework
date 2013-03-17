package cz.robyer.gamework.scenario.area;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.util.Point;

public class SoundArea extends PointArea {
	private final String TAG = "SoundArea ("+id+")";
	
	protected String value;
	protected int soundId = -1;
	protected float volume = 1.0f;
	protected float pitch = 1.0f;
	protected int loop = -1; // loop forever
	protected int soundRadius;
	
	public SoundArea(String id, LatLng point, int radius, String value, int soundRadius) {
		super(id, point, radius);
		this.value = value;
		this.soundRadius = soundRadius;
	}
	
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
		
		if (soundId == -1) {
			try {
				AssetFileDescriptor descriptor = getContext().getAssets().openFd(value);
				soundId = getScenario().getSoundPool().load(descriptor, 1);
				descriptor.close();
			} catch (IOException e) {
				Log.e(TAG, "Can't load sound '" + value + "'");
			}			
		}
	}

	private float calcVolume(double distance) {
		return 1.0f - (float)((soundRadius - distance) / soundRadius);  
	}
	
	@Override
	public void updateLocation(double lat, double lon) {
		double distance = Point.distanceBetween(point.latitude, point.longitude, lat, lon);		
		boolean r = distance < (radius + (inArea ? LEAVE_RADIUS : 0));
		float actualVolume = calcVolume(distance);

		Log.d(TAG, "Updating location. We were: " + (inArea ? "inside" : "outsude") + ", we are: " + (r ? "inside" : "outside"));

		SoundPool soundPool = getScenario().getSoundPool();
		
		if (inArea != r) {
			// enter or exit from/to area
			inArea = r;
			callHooks(inArea);
			
			if (soundId != -1) {
				if (inArea) {
					Log.d(TAG, "Started playing sound '" + value + "' at volume '" + actualVolume + "'");
					soundPool.play(soundId, actualVolume, actualVolume, 1, loop, pitch);
				} else {
					Log.d(TAG, "Stopped playing sound '" + value + "'");
					soundPool.stop(soundId);
				}
			}
		} else if (inArea) {
			Log.d(TAG, "Changed volume of sound '" + value + "' to volume '" + actualVolume + "'");
			soundPool.setVolume(soundId, actualVolume, actualVolume);
		}
	}
	
}
