package cz.robyer.gamework.scenario.area;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import android.util.Log;
import cz.robyer.gamework.scenario.Scenario;
import cz.robyer.gamework.util.GPoint;

/**
 * This represents point area with ability to define sound radius, in which area is played defined sound.
 * @author Robert Pösel
 */
public class SoundArea extends PointArea {
	protected String value;
	protected int soundId = -1;
	protected float maxVolume = 1.0f;
	protected float minVolume = 0.0f;
	protected float pitch = 1.0f;
	protected int loop = -1; // loop forever
	protected int soundRadius;
	protected boolean inSoundRadius = false;
	
	/**
	 * Class constructor
	 * @param id - identificator of area
	 * @param point - center point of area
	 * @param radius - radius in meters
	 * @param value - sound to be played
	 * @param soundRadius - radius in which sound will be played, in meters
	 */
	public SoundArea(String id, GPoint point, int radius, String value, int soundRadius) {
		super(id, point, radius);
		this.value = value;
		this.soundRadius = soundRadius;
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.BaseObject#setScenario(cz.robyer.gamework.scenario.Scenario)
	 */
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
		
		if (soundId == -1) {
			AssetFileDescriptor descriptor = null;
			try {
				// TODO: maybe not use soundpool but classic audiomanager for this, as this could be music
				descriptor = getContext().getAssets().openFd(value);
				soundId = getScenario().getSoundPool().load(descriptor, 1);
			} catch (IOException e) {
				Log.e(TAG, String.format("Can't load sound '%s'", value));
			} finally {
				try {
		        	if (descriptor != null)
		        		descriptor.close();
		        } catch (IOException ioe) {
		        	Log.e(TAG, ioe.getMessage(), ioe);
		        }
			}
		}
	}

	/**
	 * Sets volume limit for this sound, must be min < max.
	 * @param min - minimal value, must be >= 0.0f, otherwise will be used 0.0f
	 * @param max - maximal value, must be <= 1.0f, otherwise will be used 1.0f
	 */
	public void setVolumeLimit(float min, float max) {
		if (min >= max) {
			Log.e(TAG, String.format("Volume limit is incorrect - min '%.2f' must be lower than max '%.2f'", min, max));
			return;
		}
		
		this.minVolume = Math.min(Math.max(min, 0.0f), 1.0f);
		this.maxVolume = Math.max(Math.min(max, 1.0f), 0.0f);
	}
	
	/**
	 * Calculate sound volume depending on distance
	 * @param distance in meters
	 * @return volume between minVolume to maxVolume
	 */
	protected float calcVolume(double distance) {
		return minVolume + (maxVolume - minVolume) * (float)((soundRadius - distance) / soundRadius); 
	}
	
	/* (non-Javadoc)
	 * @see cz.robyer.gamework.scenario.area.Area#updateLocation(double, double)
	 */
	@Override
	public void updateLocation(double lat, double lon) {
		// update state of inArea and eventually call hooks
		super.updateLocation(lat, lon);
		
		if (soundId == -1) {
			Log.e(TAG, String.format("Sound '%s' is not loaded", value));
			return;
		}
		
		// start/stop playing sound or update volume
		double distance = GPoint.distanceBetween(point.latitude, point.longitude, lat, lon);
		boolean r = distance < (soundRadius + (inSoundRadius ? LEAVE_RADIUS : 0));
		float actualVolume = calcVolume(distance);

		SoundPool soundPool = getScenario().getSoundPool();

		if (inSoundRadius != r) {
			inSoundRadius = r;
			
			if (inSoundRadius) {
				Log.d(TAG, String.format("Started playing sound '%s' at volume '%.2f'", value, actualVolume));
				soundPool.play(soundId, actualVolume, actualVolume, 1, loop, pitch);
			} else {
				Log.d(TAG, String.format("Stopped playing sound '%s'", value));
				soundPool.stop(soundId);
			}
		} else if (inSoundRadius) {
			// update sound volume 
			Log.d(TAG, String.format("Changed volume of sound '%s' to '%.2f'", value, actualVolume));
			soundPool.setVolume(soundId, actualVolume, actualVolume);
		}
	}
	
}
