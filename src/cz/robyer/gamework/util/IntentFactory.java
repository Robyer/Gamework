package cz.robyer.gamework.util;

import cz.robyer.gamework.game.GameService;
import android.content.Context;
import android.content.Intent;

/**
 * Helper class for easily creating {@link Intent}s.
 * @author Robert Pösel
 */
public class IntentFactory {
	
	public static Intent createGameServiceIntent(Context context) {
		return new Intent(context, GameService.class); 
	}
	
	public static Intent createGameServiceIntent(Context context, String filename) {
		Intent intent = new Intent(context, GameService.class);
		intent.putExtra("filename", filename);
		return intent;
	}
}
